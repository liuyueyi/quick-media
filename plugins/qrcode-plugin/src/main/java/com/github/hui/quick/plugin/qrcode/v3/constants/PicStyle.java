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
        public BufferedImage process(BufferedImage image, int rate) {
            return ImageOperateUtil.makeRoundedCorner(image, rate);
        }
    },
    /**
     * 不处理
     */
    NORMAL {
        @Override
        public BufferedImage process(BufferedImage image, int rate) {
            return image;
        }
    },
    /**
     * 圆形图
     */
    CIRCLE {
        @Override
        public BufferedImage process(BufferedImage image, int rate) {
            return ImageOperateUtil.makeRoundImg(image, false, null);
        }
    },
    ;

    public static PicStyle getStyle(String name) {
        return PicStyle.valueOf(name.toUpperCase());
    }


    public abstract BufferedImage process(BufferedImage image, int rate);
}
