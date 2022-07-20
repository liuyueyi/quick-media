package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.base.OSUtil;
import com.github.hui.quick.plugin.image.wrapper.pixel.ImgPixelWrapper;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.PixelStyleEnum;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * 图片像素化测试类
 *
 * @author yihui
 * @data 2021/11/7
 */
public class ImgPixelHelperTest {
    private String prefix = "/tmp";

    @Before
    public void init() {
        if (OSUtil.isWinOS()) {
            prefix = "d://quick-media";
        }
    }

    @Test
    public void testImgGrayAlg() {
        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        BufferedImage out = ImgPixelWrapper.build()
                .setSourceImg(img)
                .setPixelType(PixelStyleEnum.GRAY_ALG)
                .build()
                .asBufferedImg();
        System.out.println(out);
    }


    @Test
    public void testImgGrayAvg() throws IOException {
        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        BufferedImage out = ImgPixelWrapper.build().setSourceImg(img).setBlockSize(1).setPixelType(PixelStyleEnum.GRAY_AVG).build().asBufferedImg();
        System.out.println(out);
    }

    @Test
    public void testImgPixel() throws IOException {
        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        BufferedImage out = ImgPixelWrapper.build().setSourceImg(img).setBlockSize(1).setPixelType(PixelStyleEnum.PIXEL_COLOR_AVG).build().asBufferedImg();
        System.out.println(out);
    }

    /**
     * 使用文字来组装图片
     *
     * @throws Exception
     */
    @Test
    public void testCharPicture() throws Exception {
        String pic = "jj3.png";
//        String img = "D://MobileFile/" + pic;
        String img = "http://hbimg.b0.upaiyun.com/2b79e7e15883d8f8bbae0b1d1efd6cf2c0c1ed1b10753-cusHEA_fw236";
        ImgPixelWrapper.build()
                .setSourceImg(img)
                .setChars("小黄人")
                // 字体文件下载地址: https://www.diyiziti.com/Builder/446
                .setFontName(prefix + "/潇洒手写体.ttf")
                .setBlockSize(24)
                .setFontSize(22)
                .setBgPredicate(color -> {
                    if (color == 0) {
                        return true;
                    }

                    Color rc = ColorUtil.int2color(color);
                    // 将白色当作背景色
                    return rc.getRed() >= 245 && rc.getGreen() >= 245 && rc.getBlue() >= 245;
                })
                .setPixelType(PixelStyleEnum.CHAR_SEQ_SCALE_UP)
                .build().asFile(prefix + "/char_pic_" + pic);
        System.out.println("---- over ---");
    }

    /**
     * 彩色字符图
     *
     * @throws IOException
     */
    @Test
    public void testCharImg() throws IOException {
//        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        String img = "http://hbimg.b0.upaiyun.com/2b79e7e15883d8f8bbae0b1d1efd6cf2c0c1ed1b10753-cusHEA_fw236";
        BufferedImage out = ImgPixelWrapper.build().setSourceImg(img).setBlockSize(2)
                .setPixelType(PixelStyleEnum.CHAR_COLOR)
                .setChars("灰")
                .build()
                .asBufferedImg();
        System.out.println(out);
    }

    @Test
    public void testBlackCharImg() throws Exception {
        String file = "http://5b0988e595225.cdn.sohucs.com/images/20200410/76499041d3b144b58d6ed83f307df8a3.jpeg";
        ImgPixelWrapper.build()
                .setSourceImg(file)
                .setBlockSize(4)
                .setPixelType(PixelStyleEnum.CHAR_BLACK)
                .build()
                .asFile("/tmp/o.jpg");
    }

    @Test
    public void testGif() throws Exception {
//        String img = "https://img.zcool.cn/community/01565859a4ea21a801211d251e1cbc.gif";
        String img = "https://c-ssl.duitang.com/uploads/item/202003/29/20200329043918_2FUvk.thumb.400_0.gif";
//        String img = "http://n.sinaimg.cn/sinacn/w390h219/20171231/0ac1-fyqefvw5238474.gif";
        ImgPixelWrapper.build().setSourceImg(img)
                .setBlockSize(7)
                .setPixelType(PixelStyleEnum.CHAR_BLACK)
                .setRate(2d)
                .setFontStyle(Font.BOLD)
                .build()
                .asFile(prefix + "/out3.gif");
        System.out.println("--------");
    }


    @Test
    public void testCharLines() {
        String file = "http://pic.dphydh.com/pic/newspic/2017-12-13/505831-1.png";
//        String file = "https://c-ssl.duitang.com/uploads/item/202003/29/20200329043918_2FUvk.thumb.400_0.gif";
        java.util.List<java.util.List<String>> list = ImgPixelWrapper.build()
                .setSourceImg(file)
                .setBlockSize(3)
                .setRate(0.6)
                .setPixelType(PixelStyleEnum.CHAR_BLACK)
                .build()
                .asChars();
        for (List<String> s : list) {
            for (String t : s) {
                System.out.println(t);
            }

            System.out.println("------- 分割 -------");
        }
    }

    @Test
    public void testSvg() throws Exception {
        String file = "D://quick-media/ddm.jpg";
//        String file = "http://pic.dphydh.com/pic/newspic/2017-12-13/505831-1.png";
//        String file = "https://c-ssl.duitang.com/uploads/item/202003/29/20200329043918_2FUvk.thumb.400_0.gif";
        ImgPixelWrapper.build()
                .setSourceImg(file)
                .setBlockSize(3)
                .setRate(0.6)
                .setChars("$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\\\"^`'  ")
                .setPixelType(PixelStyleEnum.CHAR_BLACK)
                .build()
                .asSvgFile(prefix + "/out.svg");
    }


    /**
     * 尝试自动识别背景图，目前看来是失败的
     *
     * @throws Exception
     */
    @Test
    public void testPickBackground() throws Exception {
//        String url = "http://5b0988e595225.cdn.sohucs.com/images/20200410/76499041d3b144b58d6ed83f307df8a3.jpeg";
        String url = "http://img2.woyaogexing.com/2017/05/24/432c24863a7ab65a!600x600.jpg";
        BufferedImage img = ImageLoadUtil.getImageByPath(url);


        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = GraphicUtil.getG2d(newImg);
        g2d.setColor(ColorUtil.OPACITY);
        g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                Color color = ColorUtil.int2color(rgb);
                if (color.getRed() > 220 && color.getGreen() > 220 && color.getBlue() > 220) {
                    g2d.setColor(ColorUtil.OPACITY);
                } else {
                    g2d.setColor(color);
                }
                g2d.fillRect(x, y, 1, 1);
            }
        }
        g2d.dispose();
        System.out.println("---over----");
    }
}
