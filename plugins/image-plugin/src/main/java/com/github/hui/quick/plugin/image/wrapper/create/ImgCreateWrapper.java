package com.github.hui.quick.plugin.image.wrapper.create;


import com.github.hui.quick.plugin.base.Base64Util;
import com.github.hui.quick.plugin.base.ColorUtil;
import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.base.ImageLoadUtil;
import com.github.hui.quick.plugin.image.helper.CalculateHelper;
import com.github.hui.quick.plugin.image.helper.ImgDrawHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by yihui on 2017/8/16.
 */
@Slf4j
public class ImgCreateWrapper {


    public static Builder build() {
        return new Builder();
    }


    @Getter
    public static class Builder {
        /**
         * 生成的图片创建参数
         */
        private ImgCreateOptions options = new ImgCreateOptions();


        /**
         * 输出的结果
         */
        private BufferedImage result;


        /**
         * 图片的高度基本增量值， 即每次扩容时，最少加 {@link #BASE_ADD_H} 的高度
         */
        private final int BASE_ADD_H = 400;


        /**
         * 实际填充的内容高度
         */
        private int contentH;


        /**
         * 实际填充的内容宽度
         */
        private int contentW;


        private Color bgColor;


        private boolean border;

        private Color borderColor;

        private BufferedImage borderImage;

        private int borderTopPadding = 30;

        private int borderLeftPadding = 20;

        private int borderBottomPadding = 40;

        private String borderSignText = "  by QuickMedia @YiHui";


        public Builder setBorderColor(int color) {
            return setBorderColor(ColorUtil.int2color(color));
        }


        public Builder setBorderColor(Color borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder setBorderImage(BufferedImage bf) {
            this.borderImage = bf;
            return this;
        }


        public Builder setBorder(boolean border) {
            this.border = border;
            return this;
        }

        public Builder setBorderTopPadding(int borderTopPadding) {
            this.borderTopPadding = borderTopPadding;
            return this;
        }

        public Builder setBorderLeftPadding(int borderLeftPadding) {
            this.borderLeftPadding = borderLeftPadding;
            return this;
        }

        public Builder setBorderBottomPadding(int borderBottomPadding) {
            this.borderBottomPadding = borderBottomPadding;
            return this;
        }

        public Builder setBorderSignText(String borderSignText) {
            this.borderSignText = borderSignText;
            return this;
        }

        public Builder setBgColor(int color) {
            return setBgColor(ColorUtil.int2color(color));
        }

        /**
         * 设置背景图
         *
         * @param bgColor
         * @return
         */
        public Builder setBgColor(Color bgColor) {
            this.bgColor = bgColor;
            return this;
        }


        public Builder setBgImg(BufferedImage bgImg) {
            options.setBgImg(bgImg);
            return this;
        }


        public Builder setImgW(int w) {
            options.setImgW(w);
            return this;
        }


        public Builder setImgH(int h) {
            options.setImgH(h);
            return this;
        }


        public Builder setFont(Font font) {
            options.setFont(font);
            return this;
        }

        public Builder setFontName(String fontName) {
            Font font = options.getFont();
            options.setFont(new Font(fontName, font.getStyle(), font.getSize()));
            return this;
        }


        public Builder setFontColor(int fontColor) {
            return setFontColor(ColorUtil.int2color(fontColor));
        }

        public Builder setFontColor(Color fontColor) {
            options.setFontColor(fontColor);
            return this;
        }

        public Builder setFontSize(Integer fontSize) {
            Font font = options.getFont();
            options.setFont(new Font(font.getName(), font.getStyle(), fontSize));
            return this;
        }

        public Builder setLeftPadding(int leftPadding) {
            options.setLeftPadding(leftPadding);
            return this;
        }


        public Builder setRightPadding(int rightPadding) {
            options.setRightPadding(rightPadding);
            return this;
        }

        public Builder setTopPadding(int topPadding) {
            options.setTopPadding(topPadding);
            contentH = topPadding;
            return this;
        }

        public Builder setBottomPadding(int bottomPadding) {
            options.setBottomPadding(bottomPadding);
            return this;
        }

        public Builder setLinePadding(int linePadding) {
            options.setLinePadding(linePadding);
            return this;
        }

        public Builder setAlignStyle(String style) {
            return setAlignStyle(ImgCreateOptions.AlignStyle.getStyle(style));
        }

        public Builder setAlignStyle(ImgCreateOptions.AlignStyle alignStyle) {
            options.setAlignStyle(alignStyle);
            return this;
        }


        public Builder setDrawStyle(String style) {
            return setDrawStyle(ImgCreateOptions.DrawStyle.getStyle(style));
        }


        public Builder setDrawStyle(ImgCreateOptions.DrawStyle drawStyle) {
            options.setDrawStyle(drawStyle);
            return this;
        }


        public Builder drawContent(String content) {
            if(content == null) {
                return this;
            }

            switch (options.getDrawStyle()) {
                case HORIZONTAL:
                    return drawHorizontalContent(content);
                case VERTICAL_LEFT:
                    return drawVerticalLeftContent(content);
                case VERTICAL_RIGHT:
                    return drawVerticalRightContent(content);
                default:
                    return this;
            }
        }


        protected Builder drawHorizontalContent(String content) {
            String[] strs = StringUtils.split(content, "\n");
            if (strs.length == 0) { // empty line
                strs = new String[1];
                strs[0] = " ";
            }

            Graphics2D g2d = GraphicUtil.getG2d(result);
            g2d.setFont(options.getFont());
            FontMetrics fontMetrics = g2d.getFontMetrics();


            int fontHeight = fontMetrics.getHeight();
            int lineNum = CalculateHelper.calLineNum(strs, options.getImgW() - options.getLeftPadding() - options.getRightPadding(), fontMetrics);


            // 填写内容需要占用的高度
            int height = lineNum * (fontHeight + options.getLinePadding());


            if (result == null) { // 首次绘制内容时
                result = GraphicUtil.createImg(options.getImgW(),
                        Math.max(height + options.getTopPadding() + options.getBottomPadding(), BASE_ADD_H),
                        null);
                g2d = GraphicUtil.getG2d(result);
            } else if (result.getHeight() < contentH + height + options.getBottomPadding()) {
                // 超过原来图片高度的上限, 则需要扩充图片长度
                result = GraphicUtil.createImg(options.getImgW(),
                        result.getHeight() + Math.max(height + options.getBottomPadding(), BASE_ADD_H),
                        result);
                g2d = GraphicUtil.getG2d(result);
            }


            // 绘制文字
            doDrawContent(strs, fontHeight);

            contentH += height;
            return this;
        }


        protected void doDrawContent(String[] strs, int fontHeight) {
            Graphics2D g2d = GraphicUtil.getG2d(result);
            ;
            int index = 0;
            for (String str : strs) {
                ImgDrawHelper.drawContent(g2d, str,
                        contentH + (fontHeight + options.getLinePadding()) * (++index)
                        , options);
            }
            g2d.dispose();
        }


        protected Builder drawVerticalLeftContent(String content) {
            if (contentW == 0) { // 初始化边距
                contentW = options.getLeftPadding();
            }

            Graphics2D g2d = GraphicUtil.getG2d(result);
            g2d.setFont(options.getFont());
            FontMetrics fontMetrics = g2d.getFontMetrics();


            String[] strs = StringUtils.split(content.replaceAll("\n\n", "\n \n"), "\n");
            if (strs.length == 0) { // empty line
                strs = new String[1];
                strs[0] = " ";
            }

            int fontSize = fontMetrics.getFont().getSize();
            int lineNum = CalculateHelper.calVerticalLineNum(strs, options.getImgH() - options.getBottomPadding() - options.getTopPadding(), fontMetrics);

            // 计算填写内容需要占用的宽度
            int width = lineNum * (fontSize + options.getLinePadding());


            if (result == null) {
                result = GraphicUtil.createImg(
                        Math.max(width + options.getRightPadding() + options.getLeftPadding(), BASE_ADD_H),
                        options.getImgH(),
                        null);
                g2d = GraphicUtil.getG2d(result);
            } else if (result.getWidth() < contentW + width + options.getRightPadding()) {
                // 超过原来图片宽度的上限, 则需要扩充图片长度
                result = GraphicUtil.createImg(
                        result.getWidth() + Math.max(width + options.getRightPadding(), BASE_ADD_H),
                        options.getImgH(),
                        result);
                g2d = GraphicUtil.getG2d(result);
            }


            doDrawVerticalLeftContent(strs, fontSize);

            contentW += width;
            return this;
        }


        protected void doDrawVerticalLeftContent(String[] strs, int fontSize) {
            Graphics2D g2d = GraphicUtil.getG2d(result);
            // 绘制文字
            int index = 0;
            for (String str : strs) {
                ImgDrawHelper.drawVerticalContent(g2d, str,
                        contentW + (fontSize + options.getLinePadding()) * (index++)
                        , options);
            }
            g2d.dispose();
        }


        protected Builder drawVerticalRightContent(String content) {
            if (contentW == 0) {
                contentW = options.getRightPadding();
            }

            Graphics2D g2d = GraphicUtil.getG2d(result);
            g2d.setFont(options.getFont());
            FontMetrics fontMetrics = g2d.getFontMetrics();


            String[] strs = StringUtils.split(content.replaceAll("\n\n", "\n \n"), "\n");
            if (strs.length == 0) { // empty line
                strs = new String[1];
                strs[0] = " ";
            }

            int fontSize = fontMetrics.getFont().getSize();
            int lineNum = CalculateHelper.calVerticalLineNum(strs, options.getImgH() - options.getBottomPadding() - options.getTopPadding(), fontMetrics);

            // 计算填写内容需要占用的宽度
            int width = lineNum * (fontSize + options.getLinePadding());


            if (result == null) {
                result = GraphicUtil.createImg(
                        Math.max(width + options.getRightPadding() + options.getLeftPadding(), BASE_ADD_H),
                        options.getImgH(),
                        null);
                g2d = GraphicUtil.getG2d(result);
            } else if (result.getWidth() < contentW + width + options.getLeftPadding()) {
                // 超过原来图片宽度的上限, 则需要扩充图片长度
                int newW = result.getWidth() + Math.max(width + options.getLeftPadding(), BASE_ADD_H);
                result = GraphicUtil.createImg(
                        newW,
                        options.getImgH(),
                        newW - result.getWidth(),
                        0,
                        result);
                g2d = GraphicUtil.getG2d(result);
            }


            // 绘制文字
            doDrawVerticalRightContent(strs, fontSize);

            contentW += width;
            return this;
        }


        protected void doDrawVerticalRightContent(String[] strs, int fontSize) {
            Graphics2D g2d = GraphicUtil.getG2d(result);
            // 绘制文字
            int index = 0;
            int offsetX = result.getWidth() - contentW;
            for (String str : strs) {
                ImgDrawHelper.drawVerticalContent(g2d, str,
                        offsetX - (fontSize + options.getLinePadding()) * (++index)
                        , options);
            }
            g2d.dispose();
        }


        public Builder drawImage(String img) {
            BufferedImage bfImg;
            try {
                bfImg = ImageLoadUtil.getImageByPath(img);
            } catch (IOException e) {
                log.error("load draw img error! img: {}, e:{}", img, e);
                throw new IllegalStateException("load draw img error! img: " + img, e);
            }

            return drawImage(bfImg);
        }


        public Builder drawImage(BufferedImage bufferedImage) {
            if (options.getDrawStyle() == ImgCreateOptions.DrawStyle.HORIZONTAL) {
                return drawHorizontalImage(bufferedImage);
            } else {
                return drawVerticalImage(bufferedImage);
            }
        }


        private Builder drawHorizontalImage(BufferedImage bufferedImage) {
            // 绘制图片的实际高度
            int bfImgH = bufferedImage.getWidth() > options.getImgW() ? bufferedImage.getHeight() * options.getImgW() / bufferedImage.getWidth() : bufferedImage.getHeight();

            if (result == null) {
                result = GraphicUtil.createImg(options.getImgW(),
                        Math.max(bfImgH + options.getBottomPadding() + options.getTopPadding(), BASE_ADD_H),
                        null);
            } else if (result.getHeight() < contentH + bfImgH + options.getBottomPadding()) {
                // 超过阀值
                result = GraphicUtil.createImg(options.getImgW(),
                        result.getHeight() + Math.max(bfImgH + options.getBottomPadding() + options.getTopPadding(), BASE_ADD_H),
                        result);
            }

            // 更新实际高度
            int h = ImgDrawHelper.drawImage(result,
                    bufferedImage,
                    contentH,
                    options);
            contentH += h + options.getLinePadding();
            return this;
        }


        private Builder drawVerticalImage(BufferedImage bufferedImage) {
            int padding = options.getDrawStyle() == ImgCreateOptions.DrawStyle.VERTICAL_RIGHT ? options.getLeftPadding() : options.getRightPadding();

            // 实际绘制图片的宽度
            int bfImgW = bufferedImage.getHeight() > options.getImgH() ? bufferedImage.getWidth() * options.getImgH() / bufferedImage.getHeight() : bufferedImage.getWidth();
            if (result == null) {
                result = GraphicUtil.createImg(
                        Math.max(bfImgW + options.getLeftPadding() + options.getRightPadding(), BASE_ADD_H),
                        options.getImgH(),
                        null);
            } else if (result.getWidth() < contentW + bfImgW + padding) {
                int realW = result.getWidth() + Math.max(bfImgW + options.getLeftPadding() + options.getRightPadding(), BASE_ADD_H);
                int offsetX = options.getDrawStyle() == ImgCreateOptions.DrawStyle.VERTICAL_RIGHT ? realW - result.getWidth() : 0;
                result = GraphicUtil.createImg(
                        realW,
                        options.getImgH(),
                        offsetX,
                        0,
                        result);
            }

            int w = ImgDrawHelper.drawVerticalImage(result, bufferedImage, contentW, options);
            contentW += w + options.getLinePadding();
            return this;
        }


        public BufferedImage asImage() {
            Point point = new Point();
            BufferedImage bf = createBg(point);

            Graphics2D g2d = GraphicUtil.getG2d(bf);
            g2d.drawImage(result, (int) point.getX(), (int) point.getY(), null);
            g2d.dispose();
            return bf;
        }


        public String asString() throws IOException {
            BufferedImage img = asImage();
            return Base64Util.encode(img, "png");
        }


        protected BufferedImage createBg(Point point) {
            int leftPadding = 0;
            int topPadding = 0;
            int bottomPadding = 0;
            if (border) {
                leftPadding = this.borderLeftPadding;
                topPadding = this.borderTopPadding;
                bottomPadding = this.borderBottomPadding;
            }


            int x = leftPadding;
            int y = topPadding;


            // 实际生成图片的宽， 高
            int realW, realH;
            if (options.getDrawStyle() == ImgCreateOptions.DrawStyle.HORIZONTAL) {// 水平文本输出
                realW = options.getImgW();
                realH = contentH + options.getBottomPadding();
            } else {// 垂直文本输出
                realW = contentW + options.getLeftPadding() + options.getRightPadding();
                realH = options.getImgH();
            }

            BufferedImage bf = new BufferedImage((leftPadding << 1) + realW, realH + topPadding + bottomPadding, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = GraphicUtil.getG2d(bf);


            // 绘制边框
            if (border) {
                if (this.borderImage != null) {
                    g2d.drawImage(borderImage, 0, 0, realW + (leftPadding << 1), realH + topPadding + bottomPadding, null);
                } else {
                    g2d.setColor(borderColor == null ? ColorUtil.OFF_WHITE : borderColor);
                    g2d.fillRect(0, 0, realW + (leftPadding << 1), realH + topPadding + bottomPadding);
                }

                // 绘制签名
//                g2d.setColor(Color.GRAY);


//                String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
//                borderSignText = borderSignText + "  " + date;

//                int fSize = Math.min(15, realW / (borderSignText.length()));
//                int addY = (borderBottomPadding - fSize) >> 1;
//                g2d.setFont(new Font(ImgCreateOptions.DEFAULT_FONT.getName(), ImgCreateOptions.DEFAULT_FONT.getStyle(), fSize));
//                g2d.drawString(borderSignText, x, y + addY + realH + g2d.getFontMetrics().getAscent());
            }


            // 绘制背景
            if (options.getBgImg() == null) {
                g2d.setColor(bgColor == null ? Color.WHITE : bgColor);
                g2d.fillRect(x, y, realW, realH);
            } else {
                g2d.drawImage(options.getBgImg(), x, y, realW, realH, null);
            }


            // 绘制内容
            if (options.getDrawStyle() == ImgCreateOptions.DrawStyle.VERTICAL_RIGHT) {
                x = bf.getWidth() - result.getWidth() - x;
            }

            point.setLocation(x, y);
            return bf;
        }

    }


}
