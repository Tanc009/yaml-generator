# 一个简单的yaml文档生成工具
```
之前在京东云参与接入OpenApi网关改造,需要提供yaml文档给网关组生成sdk
为了减少工足量以及后面方便维护,实现了一个简单的yaml文档生成工具
```
## 使用说明,一个pom文件实例

```
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix></classpathPrefix>
                            <mainClass>com.xx.xx.xx</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy</id>
                        <phase>install</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>
                                ${project.build.directory}
                            </outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>heap-stark</groupId>
                <artifactId>yaml-generator</artifactId>
                <version>0.1-SNAPSHOT</version>
                <configuration>
                    <controllerPackage>heap.stark.controller.api</controllerPackage>
                    <resultPath>/home/wzl/Downloads/heap-stark/src/main/resources
                    </resultPath>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>heap-stark</groupId>
                        <artifactId>heap-stark-web</artifactId>
                        <version>1.0-SNAPSHOT</version>
                        <type>jar</type>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <id>first</id>
                        <phase>compile</phase>
                        <goals>
                            <goal>yaml</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```


