package com.hust.hui.quickmedia.common.html;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 通过phantomjs 来实现html转图片的功能，需要先安装phantomjs
 *
 * {@link /doc/image/html2img.md}
 *
 * <p>
 * Created by yihui on 2017/12/1.
 */
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
