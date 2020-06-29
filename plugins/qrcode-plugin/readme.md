## qrcode-plugin

> 更丰富的使用教程，请参考: [二维码插件使用手册](https://liuyueyi.github.io/quick-media/#/%E6%8F%92%E4%BB%B6/%E4%BA%8C%E7%BB%B4%E7%A0%81/%E4%BA%8C%E7%BB%B4%E7%A0%81%E6%8F%92%E4%BB%B6%E4%BD%BF%E7%94%A8%E6%89%8B%E5%86%8C)

封装了二维码的生成和解析，深度定制了二维码的生成，支持各种酷炫二维码的产生

- [x] 二维码生成
- [x] 个性二维码生成
    - 支持logo
    - 支持logo样式 （圆角logo， 边框）
    - 支持二维码颜色设置
    - 支持探测图形颜色设置
    - 支持背景图
    - 支持base64格式的二维码图片
    - 支持二维码定制绘制信息样式
     - 三角形
     - 矩形
     - 五边形 （五角星待支持）
     - 六边形
     - 八边形
     - 圆
     - 自定义图片
- [x] 二维码信息解析

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
    <artifactId>qrcode-plugin</artifactId>
    <version>2.4.1</version>
</dependency>
```

### 2. 使用说明


对二维码的生成，进行了深度定制，可以对二维码中的所有元素进行修改，包括是哪个探测定位点，二维码内部矩阵，背景，logo等

想了解如何使用，推荐参考下测试类里面的各种使用姿势: [QrCodeWrapperTest](https://github.com/liuyueyi/quick-media/blob/master/plugins/qrcode-plugin/src/test/java/com/github/hui/quick/plugin/test/QrCodeWrapperTest.java)

一个简单的生成二维码的case，指定了颜色，指定二维码矩阵改成小圆点，支持了logo

```java
private String msg = "https://liuyueyi.github.io/hexblog/2018/03/23/mysql之锁与事务详解/";

@Test
public void testGenWxQrcode() {
    String logo = "logo.jpg";

    int size = 500;
    try {
        BufferedImage img = QrCodeGenWrapper.of(msg)
                .setW(size)
                .setH(size)
//                    .setDetectImg("detect.png")
                .setDrawPreColor(0xff008e59)
//                    .setDrawPreColor(0xff002fa7)
                .setErrorCorrection(ErrorCorrectionLevel.M)
                .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                .setLogoBgColor(Color.LIGHT_GRAY)
                .setLogo(logo)
                .setLogoRate(10)
                .setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE)
                .setDrawEnableScale(true)
                .asBufferedImage();
        ImageIO.write(img, "png", new File("src/test/qrcode/style.png"));
    } catch (IOException e) {
        e.printStackTrace();
    } catch (WriterException e) {
        e.printStackTrace();
    }
}
```

输出图片如: 

![qrcode](https://raw.githubusercontent.com/liuyueyi/quick-media/master/plugins/qrcode-plugin/src/test/qrcode/style.png)
