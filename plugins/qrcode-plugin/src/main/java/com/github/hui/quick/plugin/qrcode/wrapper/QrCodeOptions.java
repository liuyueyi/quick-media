package com.github.hui.quick.plugin.qrcode.wrapper;

import com.github.hui.quick.plugin.base.gif.GifDecoder;
import com.github.hui.quick.plugin.qrcode.constants.QuickQrUtil;
import com.github.hui.quick.plugin.qrcode.entity.DotSize;
import com.github.hui.quick.plugin.qrcode.entity.RenderImgResourcesV2;
import com.github.hui.quick.plugin.qrcode.entity.RenderImgResources;
import com.github.hui.quick.plugin.qrcode.helper.QrCodeRenderHelper;
import com.google.zxing.EncodeHintType;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by yihui on 2017/7/17.
 */
public class QrCodeOptions {
    /**
     * 塞入二维码的信息
     */
    private String msg;

    /**
     * 生成二维码的宽
     */
    private Integer w;


    /**
     * 生成二维码的高
     */
    private Integer h;


    /**
     * 二维码信息(即传统二维码中的黑色方块) 绘制选项
     */
    private DrawOptions drawOptions;

    /**
     * 前置图选项
     */
    private FrontImgOptions ftImgOptions;

    /**
     * 背景图样式选项
     */
    private BgImgOptions bgImgOptions;

    /**
     * logo 样式选项
     */
    private LogoOptions logoOptions;

    /**
     * todo 后续可以考虑三个都可以自配置
     * <p>
     * 三个探测图形的样式选项
     */
    private DetectOptions detectOptions;


    private Map<EncodeHintType, Object> hints;


    /**
     * 生成二维码图片的格式 png, jpg
     */
    private String picType;


    /**
     * true 表示生成的是动图
     *
     * @return
     */
    public boolean gifQrCode() {
        boolean ans = bgImgOptions != null && bgImgOptions.getGifDecoder() != null;
        if (ans) {
            return true;
        }

        return ftImgOptions != null && ftImgOptions.getGifDecoder() != null;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public DrawOptions getDrawOptions() {
        return drawOptions;
    }

    public void setDrawOptions(DrawOptions drawOptions) {
        this.drawOptions = drawOptions;
    }

    public BgImgOptions getBgImgOptions() {
        return bgImgOptions;
    }

    public void setBgImgOptions(BgImgOptions bgImgOptions) {
        this.bgImgOptions = bgImgOptions;
    }

    public FrontImgOptions getFtImgOptions() {
        return ftImgOptions;
    }

    public void setFtImgOptions(FrontImgOptions ftImgOptions) {
        this.ftImgOptions = ftImgOptions;
    }

    public LogoOptions getLogoOptions() {
        return logoOptions;
    }

    public void setLogoOptions(LogoOptions logoOptions) {
        this.logoOptions = logoOptions;
    }

    public DetectOptions getDetectOptions() {
        return detectOptions;
    }

    public void setDetectOptions(DetectOptions detectOptions) {
        this.detectOptions = detectOptions;
    }

    public Map<EncodeHintType, Object> getHints() {
        return hints;
    }

    public void setHints(Map<EncodeHintType, Object> hints) {
        this.hints = hints;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QrCodeOptions that = (QrCodeOptions) o;
        return Objects.equals(msg, that.msg) &&
                Objects.equals(w, that.w) &&
                Objects.equals(h, that.h) &&
                Objects.equals(drawOptions, that.drawOptions) &&
                Objects.equals(ftImgOptions, that.ftImgOptions) &&
                Objects.equals(bgImgOptions, that.bgImgOptions) &&
                Objects.equals(logoOptions, that.logoOptions) &&
                Objects.equals(detectOptions, that.detectOptions) &&
                Objects.equals(hints, that.hints) &&
                Objects.equals(picType, that.picType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(msg, w, h, drawOptions, ftImgOptions, bgImgOptions, logoOptions, detectOptions, hints, picType);
    }

    /**
     * logo 的配置信息
     */
    public static class LogoOptions {

        /**
         * logo 图片
         */
        private BufferedImage logo;

        /**
         * logo 样式
         */
        private LogoStyle logoStyle;

        /**
         * logo 占二维码的比例， rate 要求 > 4
         */
        private int rate;

        /**
         * true 表示有边框，
         * false 表示无边框
         */
        private boolean border;

        /**
         * 边框颜色
         */
        private Color borderColor;

        /**
         * 外围边框颜色
         */
        private Color outerBorderColor;

        /**
         * 用于设置logo的透明度
         */
        private Float opacity;

        /**
         * true 表示将logo区域的二维码移除掉
         * false logo区域的二维码不做任何处理
         */
        private boolean clearLogoArea;

        public LogoOptions() {
        }

        public LogoOptions(BufferedImage logo, LogoStyle logoStyle, int rate, boolean border, Color borderColor,
                           Color outerBorderColor, Float opacity, boolean clearLogoArea) {
            this.logo = logo;
            this.logoStyle = logoStyle;
            this.rate = rate;
            this.border = border;
            this.borderColor = borderColor;
            this.outerBorderColor = outerBorderColor;
            this.opacity = opacity;
            this.clearLogoArea = clearLogoArea;
        }

        public BufferedImage getLogo() {
            return logo;
        }

        public void setLogo(BufferedImage logo) {
            this.logo = logo;
        }

        public LogoStyle getLogoStyle() {
            return logoStyle;
        }

        public void setLogoStyle(LogoStyle logoStyle) {
            this.logoStyle = logoStyle;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

        public boolean isBorder() {
            return border;
        }

        public void setBorder(boolean border) {
            this.border = border;
        }

        public Color getBorderColor() {
            return borderColor;
        }

        public void setBorderColor(Color borderColor) {
            this.borderColor = borderColor;
        }

        public Color getOuterBorderColor() {
            return outerBorderColor;
        }

        public void setOuterBorderColor(Color outerBorderColor) {
            this.outerBorderColor = outerBorderColor;
        }

        public Float getOpacity() {
            return opacity;
        }

        public void setOpacity(Float opacity) {
            this.opacity = opacity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            LogoOptions that = (LogoOptions) o;
            return rate == that.rate && border == that.border && Objects.equals(logo, that.logo) &&
                    logoStyle == that.logoStyle && Objects.equals(borderColor, that.borderColor) &&
                    Objects.equals(outerBorderColor, that.outerBorderColor) && Objects.equals(opacity, that.opacity);
        }

        @Override
        public int hashCode() {

            return Objects.hash(logo, logoStyle, rate, border, borderColor, outerBorderColor, opacity);
        }

        @Override
        public String toString() {
            return "LogoOptions{" +
                    "logo=" + logo +
                    ", logoStyle=" + logoStyle +
                    ", rate=" + rate +
                    ", border=" + border +
                    ", borderColor=" + borderColor +
                    ", outerBorderColor=" + outerBorderColor +
                    ", opacity=" + opacity +
                    ", clearLogoArea=" + clearLogoArea +
                    '}';
        }

        public static LogoOptionsBuilder builder() {
            return new LogoOptionsBuilder();
        }

        public static class LogoOptionsBuilder {
            /**
             * logo 图片
             */
            private BufferedImage logo;

            /**
             * logo 样式
             */
            private LogoStyle logoStyle;

            /**
             * logo 占二维码的比例， 默认为12
             */
            private int rate;

            /**
             * true 表示有边框，
             * false 表示无边框
             */
            private boolean border;

            /**
             * 边框颜色
             */
            private Color borderColor;

            /**
             * 外围边框颜色
             */
            private Color outerBorderColor;

            /**
             * 用于设置logo的透明度
             */
            private Float opacity;

            /**
             * true 会将logo区域的二维码清除掉
             */
            private boolean clearLogoArea = true;

            public LogoOptionsBuilder logo(BufferedImage logo) {
                this.logo = logo;
                return this;
            }

            public LogoOptionsBuilder logoStyle(LogoStyle logoStyle) {
                this.logoStyle = logoStyle;
                return this;
            }

            public LogoOptionsBuilder rate(int rate) {
                this.rate = rate;
                return this;
            }

            public LogoOptionsBuilder border(boolean border) {
                this.border = border;
                return this;
            }

            public LogoOptionsBuilder borderColor(Color borderColor) {
                this.borderColor = borderColor;
                return this;
            }

            public LogoOptionsBuilder outerBorderColor(Color outerBorderColor) {
                this.outerBorderColor = outerBorderColor;
                return this;
            }

            public LogoOptionsBuilder opacity(Float opacity) {
                this.opacity = opacity;
                return this;
            }

            public LogoOptionsBuilder clearLogoArea(boolean clearLogoArea) {
                this.clearLogoArea = clearLogoArea;
                return this;
            }

            public LogoOptions build() {
                return new LogoOptions(logo, logoStyle, rate, border, borderColor, outerBorderColor, opacity, clearLogoArea);
            }
        }
    }


    /**
     * 背景图的配置信息
     */
    public static class BgImgOptions {
        /**
         * 背景图
         */
        private BufferedImage bgImg;

        /**
         * 背景图样式
         */
        private ImgStyle imgStyle;

        /**
         * 圆角弧度，默认为宽高中较小值的 1/8
         */
        private float radius;

        /**
         * 动态背景图
         */
        private GifDecoder gifDecoder;

        /**
         * 背景图宽
         */
        private int bgW;

        /**
         * 背景图高
         */
        private int bgH;

        /**
         * 背景图样式
         */
        private BgImgStyle bgImgStyle;

        /**
         * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.OVERRIDE，
         * 用于设置二维码的透明度
         */
        private float opacity;


        /**
         * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.FILL
         * <p>
         * 用于设置二维码的绘制在背景图上的x坐标
         */
        private int startX;


        /**
         * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.FILL
         * <p>
         * 用于设置二维码的绘制在背景图上的y坐标
         */
        private int startY;

        public BgImgOptions() {
        }

        public BgImgOptions(BufferedImage bgImg, ImgStyle imgStyle, float radius, GifDecoder gifDecoder, int bgW,
                            int bgH, BgImgStyle bgImgStyle, float opacity, int startX, int startY) {
            this.bgImg = bgImg;
            this.imgStyle = imgStyle;
            this.radius = radius;
            this.gifDecoder = gifDecoder;
            this.bgW = bgW;
            this.bgH = bgH;
            this.bgImgStyle = bgImgStyle;
            this.opacity = opacity;
            this.startX = startX;
            this.startY = startY;
        }

        public int getBgW() {
            if (bgImgStyle == BgImgStyle.FILL && bgW == 0) {
                if (bgImg != null) {
                    return bgImg.getWidth();
                } else {
                    return gifDecoder.getFrame(0).getWidth();
                }
            }
            return bgW;
        }

        public int getBgH() {
            if (bgImgStyle == BgImgStyle.FILL && bgH == 0) {
                if (bgImg != null) {
                    return bgImg.getHeight();
                } else {
                    return gifDecoder.getFrame(0).getHeight();
                }
            }
            return bgH;
        }

        public BufferedImage getBgImg() {
            return bgImg;
        }

        public void setBgImg(BufferedImage bgImg) {
            this.bgImg = bgImg;
        }

        public GifDecoder getGifDecoder() {
            return gifDecoder;
        }

        public void setGifDecoder(GifDecoder gifDecoder) {
            this.gifDecoder = gifDecoder;
        }

        public void setBgW(int bgW) {
            this.bgW = bgW;
        }

        public void setBgH(int bgH) {
            this.bgH = bgH;
        }

        public BgImgStyle getBgImgStyle() {
            return bgImgStyle;
        }

        public void setBgImgStyle(BgImgStyle bgImgStyle) {
            this.bgImgStyle = bgImgStyle;
        }

        public float getOpacity() {
            return opacity;
        }

        public void setOpacity(float opacity) {
            this.opacity = opacity;
        }

        public int getStartX() {
            return startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public int getStartY() {
            return startY;
        }

        public void setStartY(int startY) {
            this.startY = startY;
        }

        public ImgStyle getImgStyle() {
            return imgStyle;
        }

        public void setImgStyle(ImgStyle imgStyle) {
            this.imgStyle = imgStyle;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BgImgOptions that = (BgImgOptions) o;
            return Float.compare(that.radius, radius) == 0 && bgW == that.bgW && bgH == that.bgH &&
                    Float.compare(that.opacity, opacity) == 0 && startX == that.startX && startY == that.startY &&
                    Objects.equals(bgImg, that.bgImg) && imgStyle == that.imgStyle &&
                    Objects.equals(gifDecoder, that.gifDecoder) && bgImgStyle == that.bgImgStyle;
        }

        @Override
        public int hashCode() {
            return Objects.hash(bgImg, imgStyle, radius, gifDecoder, bgW, bgH, bgImgStyle, opacity, startX, startY);
        }

        @Override
        public String toString() {
            return "BgImgOptions{" + "bgImg=" + bgImg + ", imgStyle=" + imgStyle + ", radius=" + radius +
                    ", gifDecoder=" + gifDecoder + ", bgW=" + bgW + ", bgH=" + bgH + ", bgImgStyle=" + bgImgStyle +
                    ", opacity=" + opacity + ", startX=" + startX + ", startY=" + startY + '}';
        }

        public static BgImgOptionsBuilder builder() {
            return new BgImgOptionsBuilder();
        }

        public static class BgImgOptionsBuilder {
            /**
             * 背景图
             */
            private BufferedImage bgImg;

            /**
             * 背景图样式，圆角or矩形
             */
            private ImgStyle imgStyle;

            /**
             * 圆角背景时的角度 ，不填时，默认为 1/8 的圆角样式
             */
            private Float cornerRadius;

            /**
             * 动态背景图
             */
            private GifDecoder gifDecoder;

            /**
             * 背景图宽
             */
            private int bgW;

            /**
             * 背景图高
             */
            private int bgH;

            /**
             * 背景图样式
             */
            private BgImgStyle bgImgStyle;

            /**
             * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.OVERRIDE，
             * 用于设置二维码的透明度
             */
            private float opacity;


            /**
             * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.FILL
             * <p>
             * 用于设置二维码的绘制在背景图上的x坐标
             */
            private int startX;


            /**
             * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.FILL
             * <p>
             * 用于设置二维码的绘制在背景图上的y坐标
             */
            private int startY;

            public BgImgOptionsBuilder bgImg(BufferedImage bgImg) {
                this.bgImg = bgImg;
                return this;
            }

            public BgImgOptionsBuilder imgStyle(ImgStyle imgStyle) {
                this.imgStyle = imgStyle;
                return this;
            }

            public BgImgOptionsBuilder cornerRadius(float radius) {
                this.cornerRadius = radius;
                return this;
            }

            public BgImgOptionsBuilder gifDecoder(GifDecoder gifDecoder) {
                this.gifDecoder = gifDecoder;
                return this;
            }

            public BgImgOptionsBuilder bgW(int bgW) {
                this.bgW = bgW;
                return this;
            }

            public BgImgOptionsBuilder bgH(int bgH) {
                this.bgH = bgH;
                return this;
            }

            public BgImgOptionsBuilder bgImgStyle(BgImgStyle bgImgStyle) {
                this.bgImgStyle = bgImgStyle;
                return this;
            }

            public BgImgOptionsBuilder opacity(float opacity) {
                this.opacity = opacity;
                return this;
            }

            public BgImgOptionsBuilder startX(int startX) {
                this.startX = startX;
                return this;
            }

            public BgImgOptionsBuilder startY(int startY) {
                this.startY = startY;
                return this;
            }

            public BgImgOptions build() {
                if (imgStyle == null) {
                    imgStyle = ImgStyle.NORMAL;
                }

                if (cornerRadius == null) {
                    cornerRadius = 0.125f;
                }

                return new BgImgOptions(bgImg, imgStyle, cornerRadius, gifDecoder, bgW, bgH, bgImgStyle, opacity, startX,
                        startY);
            }
        }
    }


    /**
     * 前置图的配置信息
     */
    public static class FrontImgOptions {
        /**
         * 前置图
         */
        private BufferedImage ftImg;

        /**
         * 背景图样式
         */
        private ImgStyle imgStyle;

        /**
         * 圆角弧度，默认为宽高中较小值的 1/8
         */
        private float radius;

        /**
         * 动态前置图
         */
        private GifDecoder gifDecoder;

        /**
         * 背景图宽
         */
        private int ftW;

        /**
         * 背景图高
         */
        private int ftH;

        /**
         * 用于设置二维码的绘制在前置图上的x坐标
         */
        private int startX;


        /**
         * 用于设置二维码的绘制在前置图上的y坐标
         */
        private int startY;

        /**
         * 二维码大小比最终输出的图片小时，用来指定二维码周边的填充色，不存在时，默认透明
         */
        private Color fillColor;

        public BufferedImage getFtImg() {
            return ftImg;
        }

        public ImgStyle getImgStyle() {
            return imgStyle;
        }

        public float getRadius() {
            return radius;
        }

        public GifDecoder getGifDecoder() {
            return gifDecoder;
        }

        public int getFtW() {
            if (ftW > 0) {
                return ftW;
            }

            if (ftImg != null) {
                return ftImg.getWidth();
            } else {
                return gifDecoder.getFrame(0).getWidth();
            }
        }

        public int getFtH() {
            if (ftH > 0) {
                return ftH;
            }

            if (ftImg != null) {
                return ftImg.getHeight();
            } else {
                return gifDecoder.getFrame(0).getHeight();
            }
        }

        public int getStartX() {
            return startX;
        }

        public int getStartY() {
            return startY;
        }

        public Color getFillColor() {
            return fillColor;
        }

        public FrontImgOptions() {
        }

        public FrontImgOptions(BufferedImage ftImg, ImgStyle imgStyle, float radius, GifDecoder gifDecoder, int ftW,
                               int ftH, int startX, int startY, Color fillColor) {
            this.ftImg = ftImg;
            this.imgStyle = imgStyle;
            this.radius = radius;
            this.gifDecoder = gifDecoder;
            this.ftW = ftW;
            this.ftH = ftH;
            this.startX = startX;
            this.startY = startY;
            this.fillColor = fillColor;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FrontImgOptions that = (FrontImgOptions) o;
            return Float.compare(that.radius, radius) == 0 &&
                    ftW == that.ftW &&
                    ftH == that.ftH &&
                    startX == that.startX &&
                    startY == that.startY &&
                    Objects.equals(ftImg, that.ftImg) &&
                    imgStyle == that.imgStyle &&
                    Objects.equals(gifDecoder, that.gifDecoder) &&
                    Objects.equals(fillColor, that.fillColor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ftImg, imgStyle, radius, gifDecoder, ftW, ftH, startX, startY, fillColor);
        }

        public static FtImgOptionsBuilder builder() {
            return new FtImgOptionsBuilder();
        }

        public static class FtImgOptionsBuilder {
            /**
             * 前置图
             */
            private BufferedImage ftImg;

            /**
             * 背景图样式
             */
            private ImgStyle imgStyle;

            /**
             * 圆角弧度，默认为宽高中较小值的 1/8
             */
            private Float radius;

            /**
             * 动态前置图
             */
            private GifDecoder gifDecoder;

            /**
             * 背景图宽
             */
            private int ftW;

            /**
             * 背景图高
             */
            private int ftH;

            /**
             * 用于设置二维码的绘制在前置图上的x坐标
             */
            private int startX;


            /**
             * 用于设置二维码的绘制在前置图上的y坐标
             */
            private int startY;

            /**
             * 填充色
             */
            private Color fillColor;

            public FtImgOptionsBuilder ftImg(BufferedImage img) {
                this.ftImg = img;
                return this;
            }

            public FtImgOptionsBuilder imgStyle(ImgStyle imgStyle) {
                this.imgStyle = imgStyle;
                return this;
            }

            public FtImgOptionsBuilder radius(Float radius) {
                this.radius = radius;
                return this;
            }

            public FtImgOptionsBuilder gifDecoder(GifDecoder gifDecoder) {
                this.gifDecoder = gifDecoder;
                return this;
            }

            public FtImgOptionsBuilder ftW(int ftW) {
                this.ftW = ftW;
                return this;
            }

            public FtImgOptionsBuilder ftH(int ftH) {
                this.ftH = ftH;
                return this;
            }

            public FtImgOptionsBuilder startX(int startX) {
                this.startX = startX;
                return this;
            }

            public FtImgOptionsBuilder startY(int startY) {
                this.startY = startY;
                return this;
            }

            public FtImgOptionsBuilder fillImg(Color fillColor) {
                this.fillColor = fillColor;
                return this;
            }

            public FrontImgOptions build() {
                if (imgStyle == null) {
                    imgStyle = ImgStyle.NORMAL;
                }

                if (radius == null) {
                    radius = 0.125f;
                }

                return new FrontImgOptions(ftImg, imgStyle, radius, gifDecoder, ftW, ftH, startX,
                        startY, fillColor);
            }
        }
    }


    /**
     * 探测图形的配置信息
     */
    public static class DetectOptions {
        private Color outColor;

        private Color inColor;

        /**
         * 默认探测图形，优先级高于颜色的定制（即存在图片时，用图片绘制探测图形）
         */
        private BufferedImage detectImg;

        /**
         * 左上角的探测图形
         */
        private BufferedImage detectImgLT;

        /**
         * 右上角的探测图形
         */
        private BufferedImage detectImgRT;

        /**
         * 左下角的探测图形
         */
        private BufferedImage detectImgLD;

        /**
         * true 表示探测图形单独处理
         * false 表示探测图形的样式更随二维码的主样式
         */
        private Boolean special;

        public Boolean getSpecial() {
            if (special == null) {
                // 默认不特殊处理
                special = false;
            }
            return special;
        }

        public DetectOptions() {
        }

        public DetectOptions(Color outColor, Color inColor, BufferedImage detectImg, BufferedImage detectImgLT,
                             BufferedImage detectImgRT, BufferedImage detectImgLD, Boolean special) {
            this.outColor = outColor;
            this.inColor = inColor;
            this.detectImg = detectImg;
            this.detectImgLT = detectImgLT;
            this.detectImgRT = detectImgRT;
            this.detectImgLD = detectImgLD;
            this.special = special;
        }

        public Color getOutColor() {
            return outColor;
        }

        public void setOutColor(Color outColor) {
            this.outColor = outColor;
        }

        public Color getInColor() {
            return inColor;
        }

        public void setInColor(Color inColor) {
            this.inColor = inColor;
        }

        public BufferedImage getDetectImg() {
            return detectImg;
        }

        public void setDetectImg(BufferedImage detectImg) {
            this.detectImg = detectImg;
        }

        public BufferedImage getDetectImgLT() {
            return detectImgLT;
        }

        public void setDetectImgLT(BufferedImage detectImgLT) {
            this.detectImgLT = detectImgLT;
        }

        public BufferedImage getDetectImgRT() {
            return detectImgRT;
        }

        public void setDetectImgRT(BufferedImage detectImgRT) {
            this.detectImgRT = detectImgRT;
        }

        public BufferedImage getDetectImgLD() {
            return detectImgLD;
        }

        public void setDetectImgLD(BufferedImage detectImgLD) {
            this.detectImgLD = detectImgLD;
        }

        public void setSpecial(Boolean special) {
            this.special = special;
        }

        public BufferedImage chooseDetectedImg(QrCodeRenderHelper.DetectLocation detectLocation) {
            switch (detectLocation) {
                case LD:
                    return detectImgLD == null ? detectImg : detectImgLD;
                case LT:
                    return detectImgLT == null ? detectImg : detectImgLT;
                case RT:
                    return detectImgRT == null ? detectImg : detectImgRT;
                default:
                    return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DetectOptions that = (DetectOptions) o;
            return Objects.equals(outColor, that.outColor) && Objects.equals(inColor, that.inColor) &&
                    Objects.equals(detectImg, that.detectImg) && Objects.equals(detectImgLT, that.detectImgLT) &&
                    Objects.equals(detectImgRT, that.detectImgRT) && Objects.equals(detectImgLD, that.detectImgLD) &&
                    Objects.equals(special, that.special);
        }

        @Override
        public int hashCode() {

            return Objects.hash(outColor, inColor, detectImg, detectImgLT, detectImgRT, detectImgLD, special);
        }

        @Override
        public String toString() {
            return "DetectOptions{" + "outColor=" + outColor + ", inColor=" + inColor + ", detectImg=" + detectImg +
                    ", detectImgLT=" + detectImgLT + ", detectImgRT=" + detectImgRT + ", detectImgLD=" + detectImgLD +
                    ", special=" + special + '}';
        }

        public static DetectOptionsBuilder builder() {
            return new DetectOptionsBuilder();
        }

        public static class DetectOptionsBuilder {
            private Color outColor;

            private Color inColor;

            private BufferedImage detectImg;

            private BufferedImage detectImgLT;

            private BufferedImage detectImgRT;

            private BufferedImage detectImgLD;

            private Boolean special;

            public DetectOptionsBuilder outColor(Color outColor) {
                this.outColor = outColor;
                return this;
            }

            public DetectOptionsBuilder inColor(Color inColor) {
                this.inColor = inColor;
                return this;
            }

            public DetectOptionsBuilder detectImg(BufferedImage detectImg) {
                this.detectImg = detectImg;
                return this;
            }

            public DetectOptionsBuilder detectImgLT(BufferedImage detectImgLT) {
                this.detectImgLT = detectImgLT;
                return this;
            }

            public DetectOptionsBuilder detectImgRT(BufferedImage detectImgRT) {
                this.detectImgRT = detectImgRT;
                return this;
            }

            public DetectOptionsBuilder detectImgLD(BufferedImage detectImgLD) {
                this.detectImgLD = detectImgLD;
                return this;
            }

            public DetectOptionsBuilder special(Boolean special) {
                this.special = special;
                return this;
            }

            public DetectOptions build() {
                return new DetectOptions(outColor, inColor, detectImg, detectImgLT, detectImgRT, detectImgLD, special);
            }
        }
    }


    /**
     * 绘制二维码的配置信息
     */
    public static class DrawOptions {
        /**
         * 着色颜色
         */
        private Color preColor;

        /**
         * 背景颜色
         */
        private Color bgColor;

        /**
         * 背景图
         */
        private BufferedImage bgImg;

        /**
         * 绘制样式
         */
        private DrawStyle drawStyle;

        /**
         * 生成文字二维码时的候字符池
         */
        private String text;

        /**
         * 生成文字二维码时的字体
         */
        private String fontName;

        /**
         * 文字二维码渲染模式
         */
        private TxtMode txtMode;

        /**
         * 字体样式
         * <p>
         * {@link Font#PLAIN} 0
         * {@link Font#BOLD}  1
         * {@link Font#ITALIC} 2
         */
        private int fontStyle;

        /**
         * true 时表示支持对相邻的着色点进行合并处理 （即用一个大图来绘制相邻的两个着色点）
         * <p>
         * 说明： 三角形样式关闭该选项，因为留白过多，对识别有影响
         */
        private boolean enableScale;

        /**
         * 图片透明处填充，true则表示透明处用bgColor填充； false则透明处依旧透明
         */
        private boolean diaphaneityFill;

        /**
         * 渲染图
         */
        private RenderImgResources imgResources;

        /**
         * 生成二维码的图片样式，一般来讲不推荐使用圆形，默认为normal
         */
        private ImgStyle qrStyle;

        /**
         * 圆角的弧度，默认为 1 / 8
         */
        private Float cornerRadius;
        /**
         * v2 版本图片渲染资源
         */
        private RenderImgResourcesV2 imgResourcesForV2;

        public BufferedImage getImage(int row, int col) {
            return getImage(DotSize.create(row, col));
        }

        public BufferedImage getImage(DotSize dotSize) {
            if(imgResources == null) {
                return null;
            }

            return imgResources.getImage(dotSize);
        }

        /**
         * 获取二维码绘制的文字
         *
         * @return
         */
        public String getDrawQrTxt() {
            return QuickQrUtil.qrTxt(text, txtMode != null && txtMode == TxtMode.RANDOM);
        }

        public Color getPreColor() {
            return preColor;
        }

        public void setPreColor(Color preColor) {
            this.preColor = preColor;
        }

        public Color getBgColor() {
            return bgColor;
        }

        public void setBgColor(Color bgColor) {
            this.bgColor = bgColor;
        }

        public BufferedImage getBgImg() {
            return bgImg;
        }

        public void setBgImg(BufferedImage bgImg) {
            this.bgImg = bgImg;
        }

        public DrawStyle getDrawStyle() {
            return drawStyle;
        }

        public void setDrawStyle(DrawStyle drawStyle) {
            this.drawStyle = drawStyle;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getFontName() {
            return fontName;
        }

        public void setFontName(String fontName) {
            this.fontName = fontName;
        }

        public TxtMode getTxtMode() {
            return txtMode;
        }

        public void setTxtMode(TxtMode txtMode) {
            this.txtMode = txtMode;
        }

        public int getFontStyle() {
            return fontStyle;
        }

        public void setFontStyle(int fontStyle) {
            this.fontStyle = fontStyle;
        }

        public boolean isEnableScale() {
            return enableScale;
        }

        public void setEnableScale(boolean enableScale) {
            this.enableScale = enableScale;
        }

        public boolean isDiaphaneityFill() {
            return diaphaneityFill;
        }

        public void setDiaphaneityFill(boolean diaphaneityFill) {
            this.diaphaneityFill = diaphaneityFill;
        }

        public RenderImgResources getImgResources() {
            return imgResources;
        }

        public void setImgResources(RenderImgResources imgResources) {
            this.imgResources = imgResources;
        }

        public void setImgResourcesForV2(RenderImgResourcesV2 imgResourcesForV2) {
            this.imgResourcesForV2 = imgResourcesForV2;
        }

        public RenderImgResourcesV2 getImgResourcesForV2() {
            return imgResourcesForV2;
        }

        public ImgStyle getQrStyle() {
            return qrStyle;
        }

        public void setQrStyle(ImgStyle qrStyle) {
            this.qrStyle = qrStyle;
        }

        public Float getCornerRadius() {
            return cornerRadius;
        }

        public void setCornerRadius(Float cornerRadius) {
            this.cornerRadius = cornerRadius;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DrawOptions that = (DrawOptions) o;
            return fontStyle == that.fontStyle && enableScale == that.enableScale &&
                    diaphaneityFill == that.diaphaneityFill && Objects.equals(preColor, that.preColor) &&
                    Objects.equals(bgColor, that.bgColor) && Objects.equals(bgImg, that.bgImg) &&
                    drawStyle == that.drawStyle && Objects.equals(text, that.text) &&
                    Objects.equals(fontName, that.fontName) && txtMode == that.txtMode &&
                    Objects.equals(imgResources, that.imgResources) && qrStyle == that.qrStyle &&
                    Objects.equals(cornerRadius, that.cornerRadius);
        }

        @Override
        public int hashCode() {
            return Objects.hash(preColor, bgColor, bgImg, drawStyle, text, fontName, txtMode, fontStyle, enableScale,
                    diaphaneityFill, imgResources, qrStyle, cornerRadius);
        }

        @Override
        public String toString() {
            return "DrawOptions{" + "preColor=" + preColor + ", bgColor=" + bgColor + ", bgImg=" + bgImg +
                    ", drawStyle=" + drawStyle + ", text='" + text + '\'' + ", fontName='" + fontName + '\'' +
                    ", txtMode=" + txtMode + ", fontStyle=" + fontStyle + ", enableScale=" + enableScale +
                    ", diaphaneityFill=" + diaphaneityFill + ", imgMapper=" + imgResources + ", qrStyle=" + qrStyle +
                    ", cornerRadius=" + cornerRadius + '}';
        }

        public static DrawOptionsBuilder builder() {
            return new DrawOptionsBuilder();
        }

        public static class DrawOptionsBuilder {
            /**
             * 二维码居中 1对应的着色颜色
             */
            private Color preColor;

            /**
             * 二维码矩阵中 0对应的背景颜色
             */
            private Color bgColor;

            /**
             * 透明度填充，如绘制二维码的图片中存在透明区域，若这个参数为true，则会用bgColor填充透明的区域；若为false，则透明区域依旧是透明的
             */
            private boolean diaphaneityFill;

            /**
             * 文字二维码中，用于渲染的文字库，支持按字符顺序or随机两种展现方式（说明：英文不友好）
             */
            private String text;

            /**
             * 文字二维码，渲染模式
             */
            private TxtMode txtMode;

            /**
             * 文字二维码，字体名
             */
            private String fontName;

            /**
             * 字体样式
             * <p>
             * {@link Font#PLAIN} 0
             * {@link Font#BOLD}  1
             * {@link Font#ITALIC} 2
             */
            private Integer fontStyle;

            /**
             * 二维码矩阵中，0点对应绘制的背景图片， 1点对应绘制的图片在 imgMapper 中
             */
            private BufferedImage bgImg;

            /**
             * 二维码绘制样式
             */
            private DrawStyle drawStyle;


            /**
             * true 时表示支持对相邻的着色点进行合并处理 （即用一个大图来绘制相邻的两个着色点）
             * <p>
             * 说明： 三角形样式关闭该选项，因为留白过多，对识别有影响
             */
            private boolean enableScale;

            /**
             * 渲染图
             */
            private RenderImgResources imgResources;

            /**
             * v2版渲染图
             */
            private RenderImgResourcesV2 imgResourcesV2;

            /**
             * 生成二维码的图片样式，可以是圆角 或 矩形
             */
            private ImgStyle qrStyle;

            /**
             * 圆角弧度，默认宽高的 1/8
             */
            private Float cornerRadius;

            public DrawOptionsBuilder preColor(Color preColor) {
                this.preColor = preColor;
                return this;
            }

            public DrawOptionsBuilder bgColor(Color bgColor) {
                this.bgColor = bgColor;
                return this;
            }

            public DrawOptionsBuilder diaphaneityFill(boolean fill) {
                this.diaphaneityFill = fill;
                return this;
            }

            public DrawOptionsBuilder bgImg(BufferedImage image) {
                this.bgImg = image;
                return this;
            }

            public DrawOptionsBuilder drawStyle(DrawStyle drawStyle) {
                this.drawStyle = drawStyle;
                return this;
            }

            public DrawOptionsBuilder text(String text) {
                this.text = text;
                return this;
            }

            public DrawOptionsBuilder txtMode(TxtMode txtMode) {
                this.txtMode = txtMode;
                return this;
            }

            public DrawOptionsBuilder fontName(String fontName) {
                this.fontName = fontName;
                return this;
            }

            public DrawOptionsBuilder fontStyle(int fontStyle) {
                this.fontStyle = fontStyle;
                return this;
            }

            public DrawOptionsBuilder enableScale(boolean enableScale) {
                this.enableScale = enableScale;
                return this;
            }

            public DrawOptionsBuilder drawImg(int row, int column, BufferedImage image) {
                return drawImg(row, column, image, RenderImgResources.NO_LIMIT_COUNT);
            }

            public DrawOptionsBuilder drawImg(int row, int column, BufferedImage image, int count) {
                if (imgResources == null) {
                    imgResources = new RenderImgResources();
                }
                imgResources.addImage(row, column, count, image);
                return this;
            }

            public void setImgResourcesV2(RenderImgResourcesV2 imgResourcesV2) {
                this.imgResourcesV2 = imgResourcesV2;
            }

            public DrawOptionsBuilder qrStyle(ImgStyle qrStyle) {
                this.qrStyle = qrStyle;
                return this;
            }

            public DrawOptionsBuilder cornerRadius(Float cornerRadius) {
                this.cornerRadius = cornerRadius;
                return this;
            }

            public DrawOptions build() {
                DrawOptions drawOptions = new DrawOptions();
                drawOptions.setBgColor(this.bgColor);
                drawOptions.setBgImg(this.bgImg);
                drawOptions.setPreColor(this.preColor);
                drawOptions.setDrawStyle(this.drawStyle);
                drawOptions.setEnableScale(this.enableScale);
                drawOptions.setImgResources(this.imgResources);
                drawOptions.setImgResourcesForV2(this.imgResourcesV2);
                drawOptions.setDiaphaneityFill(this.diaphaneityFill);
                drawOptions.setText(text == null ? QuickQrUtil.DEFAULT_QR_TXT : text);
                drawOptions.setTxtMode(txtMode == null ? TxtMode.ORDER : txtMode);
                drawOptions.setFontName(fontName == null ? QuickQrUtil.DEFAULT_FONT_NAME : fontName);
                drawOptions.setFontStyle(fontStyle == null ? QuickQrUtil.DEFAULT_FONT_STYLE : fontStyle);
                drawOptions.setQrStyle(qrStyle == null ? ImgStyle.NORMAL : qrStyle);
                drawOptions.setCornerRadius(cornerRadius == null ? 0.125F : cornerRadius);
                return drawOptions;
            }
        }
    }

    /**
     * 图片样式
     */
    public enum ImgStyle {
        ROUND, NORMAL, CIRCLE;

        public static ImgStyle getStyle(String name) {
            return ImgStyle.valueOf(name.toUpperCase());
        }
    }

    /**
     * logo的样式
     */
    public enum LogoStyle {
        ROUND, NORMAL, CIRCLE;

        public static LogoStyle getStyle(String name) {
            return LogoStyle.valueOf(name.toUpperCase());
        }
    }


    /**
     * 背景图样式
     */
    public enum BgImgStyle {
        /**
         * 设置二维码透明度，然后全覆盖背景图
         */
        OVERRIDE,

        /**
         * 将二维码填充在背景图的指定位置
         */
        FILL,


        /**
         * 背景图穿透显示, 即二维码主题色为透明，由背景图的颜色进行填充
         */
        PENETRATE,
        ;


        public static BgImgStyle getStyle(String name) {
            return "fill".equalsIgnoreCase(name) ? FILL : OVERRIDE;
        }
    }


    /**
     * 绘制二维码信息的样式
     */
    public enum DrawStyle {
        RECT { // 矩形
            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                g2d.fillRect(x, y, w, h);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        }, MINI_RECT {
            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                int offsetX = w / 5, offsetY = h / 5;
                int width = w - offsetX * 2, height = h - offsetY * 2;
                g2d.fillRect(x + offsetX, y + offsetY, width, height);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return false;
            }
        }, CIRCLE { // 圆点

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                g2d.fill(new Ellipse2D.Float(x, y, w, h));
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        }, TRIANGLE { // 三角形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                int px[] = {x, x + (w >> 1), x + w};
                int py[] = {y + w, y, y + w};
                g2d.fillPolygon(px, py, 3);
            }

            @Override
            public boolean expand(DotSize expandType) {
                return false;
            }
        }, DIAMOND { // 五边形-钻石

            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img, String txt) {
                int cell4 = size >> 2;
                int cell2 = size >> 1;
                int px[] = {x + cell4, x + size - cell4, x + size, x + cell2, x};
                int py[] = {y, y, y + cell2, y + size, y + cell2};
                g2d.fillPolygon(px, py, 5);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        }, SEXANGLE { // 六边形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img, String txt) {
                int add = size >> 2;
                int px[] = {x + add, x + size - add, x + size, x + size - add, x + add, x};
                int py[] = {y, y, y + add + add, y + size, y + size, y + add + add};
                g2d.fillPolygon(px, py, 6);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        }, OCTAGON { // 八边形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img, String txt) {
                int add = size / 3;
                int px[] = {x + add, x + size - add, x + size, x + size, x + size - add, x + add, x, x};
                int py[] = {y, y, y + add, y + size - add, y + size, y + size, y + size - add, y + add};
                g2d.fillPolygon(px, py, 8);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        }, IMAGE { // 自定义图片

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                g2d.drawImage(img.getScaledInstance(w, h, Image.SCALE_SMOOTH), x, y, null);
            }

            @Override
            public boolean expand(DotSize expandType) {
                return true;
            }
        },

        TXT { // 文字绘制

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                Font oldFont = g2d.getFont();
                if (oldFont.getSize() != w) {
                    Font newFont = QuickQrUtil.font(oldFont.getName(), oldFont.getStyle(), w);
                    g2d.setFont(newFont);
                }
                g2d.drawString(txt, x, y + w);
                g2d.setFont(oldFont);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        },

        IMAGE_V2 {
            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                g2d.drawImage(img.getScaledInstance(w, h, Image.SCALE_SMOOTH), x, y, null);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return true;
            }
        }
        ;

        private static Map<String, DrawStyle> map;

        static {
            map = new HashMap<>(10);
            for (DrawStyle style : DrawStyle.values()) {
                map.put(style.name(), style);
            }
        }

        public static DrawStyle getDrawStyle(String name) {
            if (name == null || name.isEmpty()) { // 默认返回矩形
                return RECT;
            }


            DrawStyle style = map.get(name.toUpperCase());
            return style == null ? RECT : style;
        }


        public abstract void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt);


        /**
         * 返回是否支持绘制自定义图形的扩展
         *
         * @param dotSize
         * @return
         */
        public abstract boolean expand(DotSize dotSize);
    }


    public enum TxtMode {
        /***
         * 文字二维码，随机模式
         */
        RANDOM,
        /**
         * 文字二维码，顺序模式
         */
        ORDER;
    }
}
