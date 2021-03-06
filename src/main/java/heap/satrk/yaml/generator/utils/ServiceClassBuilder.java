package heap.satrk.yaml.generator.utils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * yamlgen
 * Created by wangzhilei3 on 2018/1/9.
 */
public class ServiceClassBuilder {

    private final List<String> CLASS_LIST = new ArrayList<>();
    private final String S = File.separator;

    /**
     * 获取某包下所有类
     *
     * @param packageName 包名
     * @return 类的完整名称
     */
    public List<Class> getClassByPackageName(String packageName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", S);
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            if (type.equals("file")) {
                getClassNameByFile(url.getPath());
            } else if (type.equals("jar")) {
                getClassNameByJar(url.getPath());
            }
        } else {
            getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath);
        }

        List<Class> classList = new ArrayList<Class>();
        for (String s : CLASS_LIST) {
            try {
                classList.add(Class.forName(s));
            } catch (ClassNotFoundException e) {
            }
        }
        return classList;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath 文件路径
     * @return 类的完整名称
     */
    private void getClassNameByFile(String filePath) {
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                getClassNameByFile(childFile.getPath());
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf(S + "classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace(S, ".");
                    CLASS_LIST.add(childFilePath);
                }
            }
        }

    }

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath jar文件路径
     * @return 类的完整名称
     */
    private void getClassNameByJar(String jarPath) {
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf(S));
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {

                    if (entryName.startsWith(packagePath)) {
                        entryName = entryName.replace(S, ".").substring(0, entryName.lastIndexOf("."));
                        CLASS_LIST.add(entryName);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     *
     * @param urls        URL集合
     * @param packagePath 包路径
     * @return 类的完整名称
     */
    private void getClassNameByJars(URL[] urls, String packagePath) {
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                String urlPath = url.getPath();
                // 不必搜索classes文件夹
                if (urlPath.endsWith("classes" + S)) {
                    continue;
                }
                String jarPath = urlPath + "!" + S + packagePath;
                getClassNameByJar(jarPath);
            }
        }
    }
}
