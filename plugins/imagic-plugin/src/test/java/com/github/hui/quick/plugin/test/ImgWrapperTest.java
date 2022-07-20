package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.imagic.ImgWrapper;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by yihui on 2018/4/17.
 */
public class ImgWrapperTest {

    private static final String url = "http://a.hiphotos.baidu.com/image/pic/item/14ce36d3d539b6006a6cc5d0e550352ac65cb733.jpg";

    @Test
    public void testCutImg() {

        try {
            // 保存到本地
            ImgWrapper.of(URI.create(url))
                    .crop(10, 20, 500, 500)
                    .toFile();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            // 保存到本地
            InputStream stream = ImgWrapper.of(URI.create(url)).crop(0, 0, 300, 500).asStream();

            BufferedImage img = ImageIO.read(stream);
            System.out.println(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 图片缩放
     */
    @Test
    public void testScaleImg() {
        try {
            // 保存到本地
            ImgWrapper.of(URI.create(url))
                    .crop(0, 0, 1280, 853)
                    .toFile("/tmp/quickmedia/scale_3840x5760.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ImgWrapper.of(URI.create(url)).scale(1125, null, null)
                    .toFile("/tmp/quickmedia/scale_out1.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            ImgWrapper.of(URI.create(url)).scale(1125, null, 50)
                    .toFile("/tmp/quickmedia/scale_out2.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            ImgWrapper.of(URI.create(url)).scale(1125, null, 80)
                    .toFile("/tmp/quickmedia/scale_out3.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            ImgWrapper.of(URI.create(url)).scale(0.5, null)
                    .toFile("/tmp/quickmedia/scale_out4.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            ImgWrapper.of(URI.create(url)).scale(0.5, 80)
                    .toFile("/tmp/quickmedia/scale_out12.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static final String localFile = "blogInfoV2.png";

    /**
     * 图片旋转
     */
    @Test
    public void testRotateImg() {
        try {
            InputStream stream = FileReadUtil.getStreamByFileName(localFile);
            BufferedImage img = ImgWrapper.of(stream).rotate(90).asImg();
            System.out.println("----" + img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testFlip() {
        BufferedImage img;
        try {
            img = ImgWrapper.of(localFile)
                    .flip()
                    .asImg();
            System.out.println("--- " + img);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            img = ImgWrapper.of(localFile)
                    .flop()
                    .asImg();
            System.out.println("--- " + img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testBorder() {
        BufferedImage img;
        try {
            img = ImgWrapper.of(localFile)
                    .board(10, 10, "red")
                    .asImg();
            System.out.println("--- " + img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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


    @Test
    public void testWater() {
        BufferedImage img;
        try {
            img = ImgWrapper.of(URI.create(url))
                    .board(10, 10, "red")
                    .water(localFile, 100, 100)
                    .asImg();
            System.out.println("--- " + img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
