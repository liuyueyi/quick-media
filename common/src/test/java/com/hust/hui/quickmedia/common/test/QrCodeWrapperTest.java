package com.hust.hui.quickmedia.common.test;

import com.hust.hui.quickmedia.common.qrcode.QrCodeGenWrapper;
import com.hust.hui.quickmedia.common.qrcode.QrCodeOptions;
import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.awt.*;

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
                    .setLogoBgColor(Color.gray)
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
                    .setLogoBgColor(Color.green)
                    .asFile("src/test/qrcode/gen_300x300_logo_v2.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            Assert.assertTrue(false);
        }
    }
}
