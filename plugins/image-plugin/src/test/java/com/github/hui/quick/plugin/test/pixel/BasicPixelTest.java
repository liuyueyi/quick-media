package com.github.hui.quick.plugin.test.pixel;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.image.wrapper.pixel.ImgPixelWrapper;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.PixelStyleEnum;
import org.junit.Test;

import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

/**
 * 基本使用姿势
 *
 * @author YiHui
 * @date 2025/2/11
 */
public class BasicPixelTest {


    @Test
    public void testGrayAlg() {
        try {
            String img = "https://pic.rmb.bdstatic.com/bjh/down/cbfbc690d1ea27f3afbe3733f49d7dac.jpeg";
            ImgPixelWrapper.build()
                    .setSourceImg(img)
                    .setPixelType(PixelStyleEnum.GRAY_ALG)
                    .build()
                    .asFile("/tmp/gray_alg.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGrayAvg() {
        try {
            String img = "https://pic.rmb.bdstatic.com/bjh/down/cbfbc690d1ea27f3afbe3733f49d7dac.jpeg";
            ImgPixelWrapper.build()
                    .setSourceImg(img)
                    .setPixelType(PixelStyleEnum.GRAY_AVG)
                    .build()
                    .asFile("/tmp/gray_avg.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPixelColorAvg() {
        try {
            String img = "https://pic.rmb.bdstatic.com/bjh/down/cbfbc690d1ea27f3afbe3733f49d7dac.jpeg";
            ImgPixelWrapper.build()
                    .setSourceImg(img)
                    .setBlockSize(8)
                    .setPixelType(PixelStyleEnum.PIXEL_COLOR_AVG)
                    .build()
                    .asFile("/tmp/pixelColorAvg.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCharColor() {
        try {
            String img = "https://pic.rmb.bdstatic.com/bjh/down/cbfbc690d1ea27f3afbe3733f49d7dac.jpeg";
            ImgPixelWrapper.build()
                    .setSourceImg(img)
                    .setBlockSize(20)
                    .setPixelType(PixelStyleEnum.CHAR_COLOR)
                    .build()
                    .asFile("/tmp/charColor.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCharGray() {
        try {
            String img = "https://pic.rmb.bdstatic.com/bjh/down/cbfbc690d1ea27f3afbe3733f49d7dac.jpeg";
            ImgPixelWrapper.build()
                    .setSourceImg(img)
                    .setBlockSize(20)
                    .setPixelType(PixelStyleEnum.CHAR_GRAY)
                    .build()
                    .asFile("/tmp/charGray.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCharBlack() {
        try {
//            String img = "https://pic.rmb.bdstatic.com/bjh/down/cbfbc690d1ea27f3afbe3733f49d7dac.jpeg";
            // 使用抠图后的人物图
            String img = "pixel/nezha.png";
            ImgPixelWrapper.build()
                    .setSourceImg(img)
                    .setBlockSize(3)
                    .setRate(0.6)
                    .setChars("$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\\\"^`'  ")
                    .setPixelType(PixelStyleEnum.CHAR_BLACK)
                    .setBgPredicate(c -> {
                        return new Color(c, true).getAlpha() < 10;
                    })
                    .build()
                    .asSvgFile("d://tmp/CharGray.svg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 使用文字来组装图片
     *
     * @throws Exception
     */
    @Test
    public void testCharPicture() throws Exception {
        try {
            String img = "pixel/nezha-mini.png";
            ImgPixelWrapper.build()
                    .setSourceImg(img)
                    .setChars("哪吒魔童降世")
                    // 字体文件下载地址: https://www.diyiziti.com/Builder/446
                    .setFontName("font/潇洒体.ttf")
                    .setBlockSize(12)
                    .setFontSize(12)
                    .setBgPredicate(color -> {
                        if (color == 0) {
                            return true;
                        }
                        Color rc = ColorUtil.int2color(color);
                        // 将白色当作背景色
                        return (rc.getRed() >= 245 && rc.getGreen() >= 245 && rc.getBlue() >= 245) || rc.getAlpha() < 10;
                    })
                    .setPixelType(PixelStyleEnum.CHAR_SEQ_SCALE_UP)
                    .build()
                    .asFile("/tmp/charSeq.png");
            System.out.println("---- over ---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCharBorder() throws Exception {
        try {
            String img = "pixel/slake.jpeg";
            List<List<String>> out = ImgPixelWrapper.build().setSourceImg(img)
                    .setBlockSize(30)
                    .setChars("1")
                    .setBgChar(' ')
                    .setPixelType(PixelStyleEnum.BLACK_CHAR_BORDER)
                    .setBgPredicate(new Predicate<Integer>() {
                        @Override
                        public boolean test(Integer color) {
                            if (color == 0) {
                                return true;
                            }

                            Color rc = ColorUtil.int2color(color);
                            if (rc.getAlpha() < 10) {
                                // 透明的直接过滤掉
                                return true;
                            }
                            // 将白色当作背景色
                            return rc.getRed() >= 40 && rc.getGreen() >= 40 && rc.getBlue() >= 40;
                        }
                    })
                    .build().asChars();

            for (String t : out.get(0)) {
                System.out.println(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGif() throws Exception {
        String img = "https://img.zcool.cn/community/01565859a4ea21a801211d251e1cbc.gif";
        ImgPixelWrapper.build().setSourceImg(img)
                .setBlockSize(7)
                .setPixelType(PixelStyleEnum.CHAR_COLOR)
                .setRate(2d)
                .setFontStyle(Font.BOLD)
                .build()
                .asFile("/tmp/outV1.gif");
        System.out.println("--------");
    }

    @Test
    public void testGifV2() throws Exception {
        String img = "https://img.zcool.cn/community/01565859a4ea21a801211d251e1cbc.gif";
        ImgPixelWrapper.build().setSourceImg(img)
                .setBlockSize(8)
                .setPixelType(PixelStyleEnum.CHAR_SEQ_SCALE_UP)
                .setChars("小黄人")
                .setBgPredicate(c -> {
                    Color color = new Color(c, true);
                    return color.getAlpha() < 10 || (color.getBlue() > 245 && color.getRed() > 245 && color.getGreen() > 245);
                })
                .setFontStyle(Font.BOLD)
                .build()
                .asFile("/tmp/outV2.gif");
        System.out.println("--------");
    }
}
