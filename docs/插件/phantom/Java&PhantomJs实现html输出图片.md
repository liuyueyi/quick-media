## Java & PhantomJs 实现html输出图片
> 借助phantomJs来实现将html网页输出为图片


## 前提准备

### 1. phantom.js 安装

```sh
# 1. 下载

## mac 系统
wget https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-macosx.zip


## linux 系统
wget https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-linux-x86_64.tar.bz2

## windows 系统
## 就不要玩了，没啥意思


# 2. 解压

sudo su 
tar -jxvf phantomjs-2.1.1-linux-x86_64.tar.bz2

# 如果解压报错，则安装下面的
# yum -y install bzip2

# 3. 安装

## 简单点，移动到bin目录下

cp phantomjs-2.1.1-linux-x86_64/bin/phantomjs /usr/local/bin

# 4. 验证是否ok
phantomjs --version

# 输出版本号，则表示ok
```

### 2. java依赖配置

maven 配置添加依赖

```xml
<!--phantomjs -->
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>2.53.1</version>
</dependency>
<dependency>
    <groupId>com.github.detro</groupId>
    <artifactId>ghostdriver</artifactId>
    <version>2.1.0</version>
</dependency>



<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```


## 开动

主要调用phantomjs来实现html渲染图片的逻辑如下

```java
public class Html2ImageByJsWrapper {

    private static PhantomJSDriver webDriver = getPhantomJs();

    private static PhantomJSDriver getPhantomJs() {
        //设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", true);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持（第二参数表明的是你的phantomjs引擎所在的路径，which/whereis phantomjs可以查看）
        // fixme 这里写了执行， 可以考虑判断系统是否有安装，并获取对应的路径 or 开放出来指定路径
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/local/bin/phantomjs");
        //创建无界面浏览器对象
        return new PhantomJSDriver(dcaps);
    }


    public static BufferedImage renderHtml2Image(String url) throws IOException {
        webDriver.get(url);
        File file = webDriver.getScreenshotAs(OutputType.FILE);
        return ImageIO.read(file);
    }

}
```

## 测试case

```java
@Test
public void testRender() throws IOException {
    BufferedImage img = null;
    for (int i = 0; i < 20; ++i) {
        String url = "https://my.oschina.net/u/566591/blog/1580020";
        long start = System.currentTimeMillis();
        img = Html2ImageByJsWrapper.renderHtml2Image(url);
        long end = System.currentTimeMillis();
        System.out.println("cost:  " + (end - start));
    }

    System.out.println(DomUtil.toDomSrc(Base64Util.encode(img, "png"), MediaType.ImagePng));

}
```
