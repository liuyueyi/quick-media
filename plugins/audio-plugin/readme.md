## audio-plugin

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
    <artifactId>audio-plugin</artifactId>
    <version>1.0</version>
</dependency>
```

### 2. 使用说明

可以参考： `com.github.hui.quick.plugin.test.AudioWrapperTest` 查看使用方式


```java
@Test
public void testAudioParse() {
    String[] arys = new String[]{
            "test.amr",
            "http://s11.mogucdn.com/mlcdn/c45406/170713_3g25ec8fak8jch5349jd2dcafh61c.amr"
    };

    for (String src : arys) {
        try {
            String output = AudioWrapper.of(src)
                    .setInputType("amr")
                    .setOutputType("mp3")
                    .asFile();
            System.out.println(output);
            log.info("the parse audio abs path: {}", output);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("parse audio error! e: {}", e);
        }
    }
}
```

