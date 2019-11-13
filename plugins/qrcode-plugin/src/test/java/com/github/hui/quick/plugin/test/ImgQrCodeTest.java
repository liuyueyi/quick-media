package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.Base64Util;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * 根据给定的图片来填充二维码的实例
 *
 * Created by @author yihui in 19:19 19/11/7.
 */
public class ImgQrCodeTest {

    @Test
    public void testGenLoveQrCode() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";

        int size = 500;
        try {
            BufferedImage img = QrCodeGenWrapper.of(msg)
                    .setW(size)
                    .setH(size)
                    .setDetectImg("jihe/PDP.png")
                    .setErrorCorrection(ErrorCorrectionLevel.M)
                    .setDrawEnableScale(true)
                    .setDrawStyle(QrCodeOptions.DrawStyle.RECT)
                    .addImg(1, 1, "love/003_02.png")
                    .addImg(1, 4, "love/004.png")
                    .addImg(4, 1, "love/004_02.png")
                    .asBufferedImage();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png", outputStream);
            String img64 = Base64Util.encode(outputStream);
            System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGenJiheQrCode() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";

        int size = 500;
        try {
            String bg = "http://img11.hc360.cn/11/busin/109/955/b/11-109955021.jpg";

            BufferedImage img = QrCodeGenWrapper.of(msg)
                    .setW(size)
                    .setH(size)
                    .setDetectImg("jihe/PDP.png")
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE)
                    .addImg(1, 1, "jihe/a.png")
                    .addImg(3, 1, "jihe/b.png")
                    .addImg(1, 3, "jihe/c.png")
                    .addImg(3, 2, "jihe/e.png")
                    .addImg(2, 3, "jihe/f.png")
                    .addImg(2, 2, "jihe/g.png")
                    .addImg(3, 4, "jihe/h.png")
                    .setBgStyle(QrCodeOptions.BgImgStyle.PENETRATE)
                    .setBgOpacity(1.0f)
                    .setBgImg(bg)
                    .setBgStartX(10)
                    .setBgStartY(100)
                    .asBufferedImage();

            ImageIO.write(img, "png", new File("/tmp/q1.png"));

            //            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //            ImageIO.write(img, "png", outputStream);
            //            String img64 = Base64Util.encode(outputStream);
            //            System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
