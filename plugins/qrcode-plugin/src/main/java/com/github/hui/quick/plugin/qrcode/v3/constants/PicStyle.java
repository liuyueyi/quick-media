package com.github.hui.quick.plugin.qrcode.v3.constants;

import com.github.hui.quick.plugin.base.awt.ImageOperateUtil;

import java.awt.image.BufferedImage;

/**
 * @author yihui
 * @date 22/7/20
 */
public enum PicStyle {
    /**
     * 圆角
     */
    ROUND {
        @Override
        public BufferedImage process(BufferedImage image, int cornerSize) {
            return ImageOperateUtil.makeRoundedCorner(image, cornerSize);
        }
    },
    /**
     * 不处理
     */
    NORMAL {
        @Override
        public BufferedImage process(BufferedImage image, int cornerSize) {
            return image;
        }
    },
    /**
     * 圆形图
     */
    CIRCLE {
        @Override
        public BufferedImage process(BufferedImage image, int cornerSize) {
            return ImageOperateUtil.makeRoundImg(image, false, null);
        }
    },
    ;

    public static PicStyle getStyle(String name) {
        return PicStyle.valueOf(name.toUpperCase());
    }


    /**
     * 图片处理
     *
     * @param image      待处理的图片
     * @param cornerSize 圆角的大小
     * @return
     */
    public abstract BufferedImage process(BufferedImage image, int cornerSize);

    /**
     * 图片处理
     *
     * @param img
     * @param cornerRadius 圆角比例
     * @return
     */
    public BufferedImage process(BufferedImage img, float cornerRadius) {
        return process(img, (int) (Math.min(img.getWidth(), img.getHeight()) * cornerRadius));
    }
}
