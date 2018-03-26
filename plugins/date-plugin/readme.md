## date-plugin

主要提供音频相关操作，目前实现音频转码

### 1. 依赖

外部如需使用，请引入下面的源

```xml
<repositories>
    <repository>
        <id>yihui-maven-repo</id>
        <url>https://raw.githubusercontent.com/liuyueyi/maven-repository/master/repository</url>
    </repository>
</repositories>
```

jar包引入，请注意使用最新的版本

```xml
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>date-plugin</artifactId>
    <version>1.0</version>
</dependency>
```

### 2. 使用说明

可以参考： `com.github.hui.quick.plugin.test.ChineseDateTest` 查看使用方式

主要提供了两个类

- 一个是基础的 `ChineseDateTool` 可以实现公历和农历之间的相互转换
- 一个扩展类 `ChineseDateExtendTool` 封装了农历输出


具体使用方法，可以参考方法签名