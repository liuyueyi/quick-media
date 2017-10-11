package com.hust.hui.quickmedia.common.test.img;

import com.hust.hui.quickmedia.common.img.wartermark.WaterMarkOptions;
import com.hust.hui.quickmedia.common.img.wartermark.WaterMarkWrapper;
import com.hust.hui.quickmedia.common.util.ImageUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * 水印测试
 *
 * Created by yihui on 2017/9/28.
 */
public class WaterMarkWrapperTest {

    @Test
    public void testWaterMark() {
            try {
                BufferedImage img = WaterMarkWrapper.of("/Users/yihui/Desktop/litter_out.jpg")
                        .setInline(true)
                        .setWaterLogo("xcx.jpg")
                        .setWaterLogoHeight(50)
                        .setWaterInfo(" 图文小工具\n By 小灰灰Blog")
                        .setStyle(WaterMarkOptions.WaterStyle.FILL_BG)
                        .setWaterColor(Color.LIGHT_GRAY)
                        .setWaterOpacity(0.8f)
                        .setRotate(45)
                        .setPaddingX(80)
                        .setPaddingX(80)
                        .build()
                        .asImage();

                ImageIO.write(img, "jpg", new File("/Users/yihui/Desktop/FILL_BG.jpg"));
                System.out.println(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }



    @Test
    public void testBgWaterMark() {
        try {
            BufferedImage img= WaterMarkWrapper.of("bg.png")
                    .setInline(true)
                    .setWaterLogo("xcx.jpg")
                    .setWaterLogoHeight(50)
                    .setWaterColor(Color.WHITE)
                    .setWaterInfo(" 图文小工具\n By 小灰灰Blog")
                    .setX(50)
                    .setY(50)
                    .setStyle(WaterMarkOptions.WaterStyle.FILL_BG)
                    .setWaterOpacity(0.5f)
                    .setRotate(45)
                    .setPaddingX(60)
                    .setPaddingY(80)
                    .build()
                    .asImage();

            ImageIO.write(img, "png", new File("/Users/yihui/Desktop/mark.png"));
            System.out.println(img);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testRotate() throws IOException {
        BufferedImage bufferedImage = ImageUtil.getImageByPath("bg.png");
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.rotate(Math.toRadians(90), bufferedImage.getWidth() >> 1, bufferedImage.getHeight() >> 1);
        g2d.dispose();


        AffineTransform tx = new AffineTransform();
        tx.rotate(0.5, bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);

        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_BILINEAR);
        bufferedImage = op.filter(bufferedImage, null);
        System.out.println("--------");
    }
}
