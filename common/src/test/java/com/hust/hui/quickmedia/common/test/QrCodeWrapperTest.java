package com.hust.hui.quickmedia.common.test;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hust.hui.quickmedia.common.qrcode.QrCodeGenWrapper;
import com.hust.hui.quickmedia.common.qrcode.QrCodeOptions;
import com.hust.hui.quickmedia.common.util.Base64Util;
import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * Created by yihui on 2017/7/17.
 */
@Slf4j
public class QrCodeWrapperTest {


    /**
     * base64 编码的图片: <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAIAAAAiOjnJAAAEIklEQVR42u3bMXbbQAwEUN3/0k6fIs+xMANQ/igtWuQuPosx1q8vpQL1sgUKLAWWAkspsBRYCqy/f9qq/7rvvy9+Zwlv7eDcigaXsNVBsMACCyywwAILLLC2YU2GhdjO5gTnfndwvdc6CBZYYIEFFlhggQXWDVi1BQ9GuZyG2sXv9KjfQbDAAgsssMACCyywPh1WbUqT811Lo7X4CRZYYIEFFlhggQUWWOGRTu6ramFtyzdYYIEFFlhggQUWWB8Eq3Ca59Ro5RFR7loHwQILLLDAAgsssMBagpWrrUj12z6tdRAssMDyKVhggQUWWHVYT6ytHFSbO9XWO7NpYIEFFlhggQUWWGCdgFU765PryuDgJXeWq+YssVdggQUWWGCBBRZYYN2AtbWkrQYPPkaOe+41WxvpgAUWWGCBBRZYYIH1LVivG5V7qlxXjkxpcqDBAgsssMACCyywwDoGayskDs6Oti6uTcO2Rljf7R1YYIEFFlhggQUWWDuwblq56aw28NkacE2mQrDAAgsssMACCyyw5mFt5aBazwY/rUmqvWZggQUWWGCBBRZYYN2G9fHTksHY+4rV1lgGLLDAAgsssMACC6wPgpULXLmuPGK0khth9W8EFlhggQUWWGCBBdbJ81jH48nuSa+tp6oFarDAAgsssMACCyywtmE9YvKw5TtnpRY/nzHSAQsssMACCyywwALrJ6lwMFPU5j9b/a4FzCNjN7DAAgsssMACCyywbsM6Iik3HjmyG1uTtN6sECywwAILLLDAAgus+WMzN519LVUuYOaCXn/bwQILLLDAAgsssMBqwRp0thVeBh8jd9QpR/YZ57HAAgsssMACCyywwDoNKxdPcrBqqTBntD8rAwsssMACCyywwALrBqzcsaFaeMk981YovjbCAgsssMACCyywwALrJKzavh9p/yD3wQNYg0F+5GKwwAILLLDAAgsssJZgbT30E4Pe4GPcDKdggQUWWGCBBRZYYB2DlQtruaNOuY4eyW5HXgawwAILLLDAAgsssI7Bqm30O0vamnjcvFGN7HcfACywwAILLLDAAgusEqyttJKLgY84CPWI6Lr25wawwAILLLDAAgsssK78u+2pgU9tAJL7dF0/WGCBBRZYYIEFFlhLqXArYT1ispRr4U2yYIEFFlhggQUWWGDdhrWV+2rRpvZuDFqppcIfLg0ssMACCyywwAILrBOwtvJX/1BRGsfnvSpggQUWWGCBBRZYYP0aWLnwkhsl5W6U2/ZCGgULLLDAAgsssMAC69Nh1YzW8mYO9FZGBgsssMACCyywwALrUbC20lluPJKz8gFTmp99FVhggQUWWGCBBRZYS7COLPhmDKxNaba6EBzpgAUWWGCBBRZYYIE1AEupd+O/LVBgKbAUWEqBpcBSYCk1W38ApvoJhIelIpkAAAAASUVORK5CYII="/>
     */
    @Test
    public void testGenQrCode1() {
        String msg = "https://my.oschina.net/u/566591/blog/1359432";

        try {
            String out = QrCodeGenWrapper.of(msg).asString();
            System.out.println(out);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            Assert.assertTrue(false);
        }
    }

    /**
     * 测试二维码
     */
    @Test
    public void testGenQrCode() {
        String msg = "https://my.oschina.net/u/566591/blog/1359432";

        try {

            boolean ans = QrCodeGenWrapper.of(msg).asFile("src/test/qrcode/gen.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            Assert.assertTrue(false);
        }


        //生成红色的二维码 300x300, 无边框
        try {
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(300)
                    .setPreColor(0xffff0000)
                    .setBgColor(0xffffffff)
                    .setPadding(0)
                    .asFile("src/test/qrcode/gen_300x300.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            Assert.assertTrue(false);
        }


        // 生成带logo的二维码
        try {
            String logo = "https://static.oschina.net/uploads/user/283/566591_100.jpeg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(300)
                    .setPreColor(0xffff0000)
                    .setBgColor(0xffffffff)
                    .setPadding(0)
                    .setLogo(logo)
                    .setLogoBgColor(0xff808080)
                    .asFile("src/test/qrcode/gen_300x300_logo.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            Assert.assertTrue(false);
        }


        // 根据本地文件生成待logo的二维码
        try {
            String logo = "logo.jpg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(300)
                    .setPreColor(0xffff0000)
                    .setBgColor(0xffffffff)
                    .setPadding(0)
                    .setLogo(logo)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(0xff00ff00)
                    .asFile("src/test/qrcode/gen_300x300_logo_v2.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            Assert.assertTrue(false);
        }


        // 根据本地文件生成待logo的二维码， 重新着色位置探测图像
        try {
            String logo = "logo.jpg";
            String bg = "bg.png";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(300)
                    .setPreColor(0xff0000ff)
                    .setBgColor(0xffffffff)
                    .setDetectCornerPreColor(0xff00FF00)
                    .setPadding(0)
                    .setLogo(logo)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(0xff00ff00)
                    .setBackground(bg)
                    .asFile("src/test/qrcode/gen_300x300_logo_v3.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            Assert.assertTrue(false);
        }
    }


    @Test
    public void testGenColorCode() {
        String msg = "http://blog.zbang.online:8080";
        // 根据本地文件生成待logo的二维码， 重新着色位置探测图像
        try {
            String logo = "mg.jpg";
            String bg = "bg.png";
            BufferedImage img = QrCodeGenWrapper.of(msg)
                    .setW(500)
                    .setPreColor(0xff002fa7) // 宝石蓝
//                    .setBgColor(0xffeeeeee)
                    .setDetectCornerPreColor(0xff00ff00)
                    .setDetectCornerBgColor(0xffff0000)
                    .setPadding(1)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setLogo(logo)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(0xff00cc00)
                    .setBackground(bg)
                    .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE.name())
                    .setDrawImg("xhr.jpg")
                    .asBufferedImage();


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png", outputStream);
            String img64 = Base64Util.encode(outputStream);
            System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            Assert.assertTrue(false);
        }
    }



    @Test
    public void testGenStyleCode() {
        String msg = "http://blog.zbang.online:8080/articles/2017/07/18/1500369136069.html";
        // 根据本地文件生成待logo的二维码， 重新着色位置探测图像

        for(QrCodeOptions.DrawStyle style : QrCodeOptions.DrawStyle.values()) {

            try {
                String logo = "mg.jpg";
                String bg = "bg.png";
                BufferedImage img = QrCodeGenWrapper.of(msg)
                        .setW(500)
                        .setPreColor(0xff002fa7) // 宝石蓝
                        .setDetectCornerPreColor(0xff0000ff)
                        .setPadding(1)
                        .setErrorCorrection(ErrorCorrectionLevel.H)
                        .setLogo(logo)
                        .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                        .setLogoBgColor(0xff00cc00)
                        .setBackground(bg)
                        .setDrawStyle(style.name())
                        .setDrawImg("logo.jpg")
                        .asBufferedImage();


                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(img, "png", outputStream);
                String img64 = Base64Util.encode(outputStream);
                System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");
            } catch (Exception e) {
                System.out.println("create qrcode error! e: " + e);
                Assert.assertTrue(false);
            }
        }
    }
}
