## 图片服务

### 1. 图片生成服务

目前图片生成服务主要提供两种支持方式：

- 水平文字，上下布局
- 垂直文字，左右布局


**使用姿势**

```java
 @Test
public void testLocalGenVerticalImg() throws IOException {
    int h = 300;
    int leftPadding = 10;
    int topPadding = 10;
    int bottomPadding = 10;
    int linePadding = 10;
    Font font = new Font("宋体", Font.PLAIN, 18);

    ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
            .setImgH(h)
            .setDrawStyle(ImgCreateOptions.DrawStyle.VERTICAL_RIGHT)
            .setLeftPadding(leftPadding)
            .setTopPadding(topPadding)
            .setBottomPadding(bottomPadding)
            .setLinePadding(linePadding)
            .setFont(font)
            .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
            .setBgColor(Color.WHITE)
            .setBorder(true)
            .setBorderColor(0xFFF7EED6)
            ;


    BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
    String line;
    while ((line = reader.readLine()) != null) {
        build.drawContent(line);
    }

    build.setAlignStyle(ImgCreateOptions.AlignStyle.BOTTOM)
            .drawImage("https://avatars3.githubusercontent.com/u/5125892?v=4&s=88");
//        build.setFontColor(Color.BLUE).drawContent("后缀签名").drawContent("灰灰自动生成");

    BufferedImage img = build.asImage();
    ImageIO.write(img, "png", new File("/Users/yihui/Desktop/2out.png"));
}
```

**实例图如下**

水平，上下

![https://static.oschina.net/uploads/img/201708/18180717_MrRM.png](https://static.oschina.net/uploads/img/201708/18180717_MrRM.png)


垂直，从左到右

![https://static.oschina.net/uploads/img/201709/05182105_2smp.jpg](https://static.oschina.net/uploads/img/201709/05182105_2smp.jpg)


垂直，从右到左

![https://static.oschina.net/uploads/img/201709/05182138_My1E.png](https://static.oschina.net/uploads/img/201709/05182138_My1E.png)