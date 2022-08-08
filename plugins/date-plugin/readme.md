## date-plugin

主要提供公历、农历的转换

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
    <version>3.0.0</version>
</dependency>
```

### 2. 使用说明

可以参考： `com.github.hui.quick.plugin.test.ChineseDateTest` 查看使用方式

主要提供了两个类

- 一个是基础的 `ChineseDateTool` 可以实现公历和农历之间的相互转换
- 一个扩展类 `ChineseDateExtendTool` 封装了农历输出


具体使用方法，可以参考方法签名，下面是一个简单的case

```java
@Test
public void testDate2Lunar() {
    LocalDateTime now = LocalDateTime.now();
    System.out.println(now + ">>>" + ChineseDateExtendTool.getNowLunarDate());

    System.out.println(now + ">>>" +
            ChineseDateExtendTool.getLunarDateByTimestamp(now.toInstant(ZoneOffset.ofHours(8)).toEpochMilli()));
}
```

输出结果如下


```bash
2019-06-20T08:08:19.758>>>己亥年伍月壹捌 辰时
2019-06-20T08:08:19.758>>>己亥年伍月壹捌 辰时
```