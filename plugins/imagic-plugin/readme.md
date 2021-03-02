# imagic-plugin

基于image-magic进行图片编辑的插件工具类


在本地测试之前，需要安装对应的环境

- 环境搭建手册可以参考: [imagemagic安装](https://liuyueyi.github.io/hexblog/2017/08/09/imagemagic%E5%AE%89%E8%A3%85/)


## 1. 依赖

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
    <artifactId>image-plugin</artifactId>
    <version>2.5.2</version>
</dependency>
```

## 2. 服务支持

封装了ImageMagic的常用操作，简化调用成本，目前完成如下接口的封装

- [x] 旋转
- [x] 裁剪
- [x] 镜像
- [x] 翻转
- [x] 压缩
- [x] 水印
- [x] 边框
- [x] 复合操作


## 3. 使用参考

支持针对本地图片、远程图片、二进制流图片的编辑处理，将处理后的图片可以保存为文件、byte数组、BufferedImage、Stream等多种方式，适用于各种使用场景

使用case: 详细使用可以参考: [ImgWrapperTest](https://github.com/liuyueyi/quick-media/blob/master/plugins/imagic-plugin/src/test/java/com/github/hui/quick/plugin/test/ImgWrapperTest.java)

```java
/**
 * 复合操作
 */
@Test
public void testOperate() {
    BufferedImage img;
    try {
        img = ImgWrapper.of(localFile)
                .board(10, 10, "red")
                .flip()
                .rotate(180)
                .crop(0, 0, 1200, 500)
                .asImg();
        System.out.println("--- " + img);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```


