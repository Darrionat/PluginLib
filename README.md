# PluginLib ![maven](https://img.shields.io/github/v/release/Darrionat/PluginLib)

A project that aims to make the creation of plugins a faster and easier process. This project supports Minecraft
1.8.8-1.16.x.

## Maven ![maven](https://img.shields.io/github/v/release/Darrionat/PluginLib)

To add this project to your Maven project make sure you have the following repository and dependency.

### Repository

```xml

<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

### Dependency

```xml

<dependency>
    <groupId>com.github.Darrionat.PluginLib</groupId>
    <artifactId>core</artifactId>
    <version>version</version>
</dependency>
```

### Shading

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.1.0</version>
            <configuration>
                <relocations>
                    <relocation>
                        <pattern>me.darrionat.pluginlib</pattern>
                        <!-- Make sure to change the package below -->
                        <shadedPattern>my.plugin.utils</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Documentation [![Website](https://img.shields.io/website?label=wiki&url=https%3A%2F%2Fwiki.darrionatplugins.com%2F)](https://wiki.darrionatplugins.com/libraries/pluginlib)

The [Wiki][wiki] provides detailed information about the API. The JavaDocs are also detailed, please read them before
all uses.

## Legal Notice

This project uses the library XSeries. XSeries is licensed under MIT. The license of XSeries can be
found [here][XSeriesMIT].

<!-- Links -->

[wiki]: https://wiki.darrionatplugins.com/libraries/pluginlib

[XSeriesMIT]: https://github.com/CryptoMorin/XSeries/blob/master/LICENSE.txt