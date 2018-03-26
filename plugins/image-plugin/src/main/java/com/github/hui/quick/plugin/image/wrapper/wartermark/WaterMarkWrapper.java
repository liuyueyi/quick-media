package com.github.hui.quick.plugin.image.wrapper.wartermark;

import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.base.ImageLoadUtil;
import com.github.hui.quick.plugin.image.helper.ImgDrawHelper;
import com.github.hui.quick.plugin.image.util.FontUtil;
import com.google.common.base.Splitter;
import lombok.Getter;
import lombok.ToString;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 水印包装类
 * <p>
 * Created by yihui on 2017/9/28.
 */
public class WaterMarkWrapper {

    private WaterMarkOptions options;

    private WaterMarkWrapper(WaterMarkOptions options) {
        this.options = options;
    }

    public static Builder of(String source) throws IOException {
        return new Builder().setSource(source);
    }

    public static Builder of(BufferedImage sourceImg) {
        return new Builder().setSource(sourceImg);
    }


    public BufferedImage asImage() {
        Graphics2D g2d = GraphicUtil.getG2d(options.getSource());

        // 设置透明度
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, options.getOpacity()));
        if (options.getStyle() != WaterMarkOptions.WaterStyle.FILL_BG) {
            g2d.drawImage(options.getWater(), options.getX(), options.getY(), null);
        } else {
            int offset = (int) (-1.5 * options.getBasicW());
            g2d.drawImage(options.getWater(), offset, offset, null);
        }

        return options.getSource();
    }


    @Getter
    @ToString
    public static class Builder {
        /**
         * 水印绘制的 x 坐标
         */
        private int x;

        /**
         * 水印绘制的 y 坐标
         */
        private int y;


        /**
         * 原图
         */
        private BufferedImage source;


        /**
         * 水印logo
         */
        private BufferedImage waterLogo;

        /**
         * logo的高度，宽度进行等比例; 当不存在时，默认logo和字体等高
         */
        private int waterLogoHeight;


        /**
         * 水印文案
         */
        private List<String> waterInfo = new ArrayList<>();

        private Font waterFont = FontUtil.BIG_DEFAULT_FONT;

        private Color waterColor = Color.WHITE;


        /**
         * 文字和logo是否保证在一行内
         */
        private boolean inline = true;

        /**
         * 透明度 0 - 1
         */
        private float waterOpacity = 1;


        private WaterMarkOptions.WaterStyle style;

        /**
         * 全背景水印，对应的旋转角度，默认不旋转
         */
        private int rotate = 0;


        /**
         * 全背景水印时， 间隔
         */
        private int paddingX = 50;

        private int paddingY = 60;

        private BufferedImage tmpWater;


        public Builder setX(int x) {
            this.x = x;
            return this;
        }

        public Builder setY(int y) {
            this.y = y;
            return this;
        }

        public Builder setSource(String source) throws IOException {
            return setSource(ImageLoadUtil.getImageByPath(source));
        }

        public Builder setSource(BufferedImage source) {
            this.source = source;
            return this;
        }


        public Builder setWaterLogo(String waterLogo) throws IOException {
            return setWaterLogo(ImageLoadUtil.getImageByPath(waterLogo));
        }

        public Builder setWaterLogo(BufferedImage waterLogo) {
            this.waterLogo = waterLogo;
            return this;
        }

        public Builder setWaterLogoHeight(int waterLogoHeight) {
            this.waterLogoHeight = waterLogoHeight;
            return this;
        }

        public Builder setWaterInfo(String waterInfo) {
            this.waterInfo = Splitter.on("\n").splitToList(waterInfo);
            return this;
        }

        public Builder setWaterFont(Font waterFont) {
            this.waterFont = waterFont;
            return this;
        }

        public Builder setWaterColor(Color waterColor) {
            this.waterColor = waterColor;
            return this;
        }

        public Builder setInline(boolean inline) {
            this.inline = inline;
            return this;
        }

        public Builder setWaterOpacity(float waterOpacity) {
            this.waterOpacity = waterOpacity;
            return this;
        }

        public Builder setStyle(String style) {
            return setStyle(WaterMarkOptions.WaterStyle.getStyle(style));
        }

        public Builder setStyle(WaterMarkOptions.WaterStyle style) {
            this.style = style;
            return this;
        }

        public Builder setRotate(int rotate) {
            this.rotate = rotate % 360;
            return this;
        }

        public Builder setPaddingX(int paddingX) {
            this.paddingX = paddingX;
            return this;
        }

        public Builder setPaddingY(int paddingY) {
            this.paddingY = paddingY;
            return this;
        }

        public WaterMarkWrapper build() {
            WaterMarkOptions options = new WaterMarkOptions();
            options.setSource(source);
            BufferedImage water = buildBasicWater();
            options.setBasicW(water.getWidth());

            if (style == WaterMarkOptions.WaterStyle.FILL_BG) {
                water = buildBgWater();
            }
            options.setWater(water);

            options.setOpacity(waterOpacity);

            options.setStyle(style);

            // 重新设置水印位置
            updatePosition();
            options.setX(x);
            options.setY(y);


            return new WaterMarkWrapper(options);
        }


        /**
         * 创建一个基本的水印
         *
         * @return
         */
        private BufferedImage buildBasicWater() {
            FontMetrics fontMetrics = FontUtil.getFontMetric(this.waterFont);

            int logoW = 0, logoH = 0;
            if (waterLogo != null) {
                logoH = waterLogoHeight <= 0 ? Math.min(fontMetrics.getHeight(), waterLogo.getHeight()) : waterLogoHeight;
                logoW = logoH * this.waterLogo.getWidth() / this.waterLogo.getHeight();
            }


            int infoW = 0;
            int infoH = 0;
            if (!waterInfo.isEmpty()) {
                for (String msg : waterInfo) {
                    infoW = Math.max(infoW, fontMetrics.stringWidth(msg));
                }

                infoH = waterInfo.size() * fontMetrics.getHeight();
            }


            int waterW = 0, waterH = 0;
            int waterX = 0;
            if (this.inline) {
                waterW = logoW + infoW;
                waterH = Math.max(logoH, infoH);
            } else {
                waterW = Math.max(logoW, infoW);
                waterH = logoH + infoH;
                waterX = (waterW - logoW) >> 1;
            }


            // 生成水印画布
            tmpWater = new BufferedImage(waterW, waterH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = GraphicUtil.getG2d(tmpWater);


            // 绘制logo
            if (waterLogo != null) {
                g2d.drawImage(waterLogo, waterX, 0, logoW, logoH, null);
            }


            // 绘制签名
            if (!waterInfo.isEmpty()) {
                g2d.setFont(this.waterFont);
                g2d.setColor(waterColor);
                int offsetX = inline ? logoW : 0;
                int offsetY = waterH - infoH + fontMetrics.getAscent();
                for (String msg : waterInfo) {
                    g2d.drawString(msg, offsetX, offsetY);
                    offsetY += fontMetrics.getHeight();
                }
            }


            // 旋转一定的角度
            if (style != WaterMarkOptions.WaterStyle.FILL_BG
                    && rotate != 0) {
                tmpWater = ImgDrawHelper.rotateImg(tmpWater, rotate);
            }


            return tmpWater;
        }


        /**
         * 背景填充时，需要扩充
         *
         * @return
         */
        private BufferedImage buildBgWater() {
            int wW = tmpWater.getWidth();
            int wH = tmpWater.getHeight();


            // 水印图片的大小， 因为旋转的缘故，所以需要放大一些，保证旋转后也不会出现留白问题
            int newSize = Math.max(source.getWidth(), source.getHeight()) + (wW + paddingX) * 2;

            int tmpX = (paddingX >> 1), tmpY = -wH;

            int offsetX = wW + paddingX;
            int offsetY = wH + paddingY;

            BufferedImage water = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = GraphicUtil.getG2d(water);
            while (true) {
                tmpY += offsetY;
                while (true) {
                    g2d.drawImage(tmpWater, tmpX, tmpY, null);

                    if (tmpX + offsetX >= newSize) {
                        tmpX = newSize - tmpX - offsetX;
                        break;
                    } else {
                        tmpX += offsetX;
                    }
                }

                if (tmpY >= newSize) {
                    break;
                }
            }


            this.tmpWater = ImgDrawHelper.rotateImg(water, rotate);
            return tmpWater;
        }

        private void updatePosition() {
            if (x > 0 || y > 0) {
                return;
            }

            // 边距
            int padding = 10;

            int sW = source.getWidth();
            int sH = source.getHeight();

            int wW = tmpWater.getWidth();
            int wH = tmpWater.getHeight();

            switch (style) {
                case OVERRIDE_CENTER:
                    x = (sW - wW) >> 1;
                    y = (sH - wH) >> 1;
                    break;
                case OVERRIDE_LEFT_BOTTOM:
                    x = padding;
                    y = sH - wH - padding;
                    break;
                case OVERRIDE_LEFT_CENTER:
                    x = padding;
                    y = (sH - wH) >> 1;
                    break;
                case OVERRIDE_LEFT_TOP:
                    x = padding;
                    y = padding;
                    break;
                case OVERRIDE_RIGHT_BOTTOM:
                    x = sW - wW - padding;
                    y = sH - wH - padding;
                    break;
                case OVERRIDE_RIGHT_CENTER:
                    x = sW - wW - padding;
                    y = (sH - wH) >> 1;
                    break;
                case OVERRIDE_RIGHT_TOP:
                    x = sW - wW - padding;
                    y = padding;
                    break;
                case OVERRIDE_TOP_CENTER:
                    x = (sW - wW) >> 1;
                    y = padding;
                    break;
                case OVERRIDE_BOTTOM_CENTER:
                    x = (sW - wW) >> 1;
                    y = sH - wH - padding;
                    break;
                case FILL_BG:
                    return;
            }
        }
    }

}
