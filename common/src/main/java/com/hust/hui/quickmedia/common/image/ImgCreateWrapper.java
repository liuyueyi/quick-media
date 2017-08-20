package com.hust.hui.quickmedia.common.image;

import com.hust.hui.quickmedia.common.util.Base64Util;
import com.hust.hui.quickmedia.common.util.ColorUtil;
import com.hust.hui.quickmedia.common.util.GraphicUtil;
import com.hust.hui.quickmedia.common.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

/**
 * Created by yihui on 2017/8/16.
 */
@Slf4j
public class ImgCreateWrapper {


    public static Builder build() {
        return new Builder();
    }


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
        private final int BASE_ADD_H = 1000;


        /**
         * 实际填充的内容高度
         */
        private int contentH;


        private Color bgColor;


        private boolean border;

        private Color borderColor;

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


        public Builder drawContent(String content) {
            String[] strs = StringUtils.split(content, "\n");
            if (strs.length == 0) { // empty line
                strs = new String[1];
                strs[0] = " ";
            }

            int fontSize = options.getFont().getSize();
            int lineNum = calLineNum(strs, options.getImgW(), options.getLeftPadding(), fontSize);
            // 填写内容需要占用的高度
            int height = lineNum * (fontSize + options.getLinePadding());

            if (result == null) {
                result = GraphicUtil.createImg(options.getImgW(),
                        Math.max(height + options.getTopPadding() + options.getBottomPadding(), BASE_ADD_H),
                        null);
            } else if (result.getHeight() < contentH + height + options.getBottomPadding()) {
                // 超过原来图片高度的上限, 则需要扩充图片长度
                result = GraphicUtil.createImg(options.getImgW(),
                        result.getHeight() + Math.max(height + options.getBottomPadding(), BASE_ADD_H),
                        result);
            }


            // 绘制文字
            Graphics2D g2d = GraphicUtil.getG2d(result);
            int index = 0;
            for (String str : strs) {
                GraphicUtil.drawContent(g2d, str,
                        contentH + (fontSize + options.getLinePadding()) * (++index)
                        , options);
            }
            g2d.dispose();

            contentH += height;
            return this;
        }


        public Builder drawImage(String img) {
            BufferedImage bfImg;
            try {
                bfImg = ImageUtil.getImageByPath(img);
            } catch (IOException e) {
                log.error("load draw img error! img: {}, e:{}", img, e);
                throw new IllegalStateException("load draw img error! img: " + img, e);
            }

            return drawImage(bfImg);
        }


        public Builder drawImage(BufferedImage bufferedImage) {

            if (result == null) {
                result = GraphicUtil.createImg(options.getImgW(),
                        Math.max(bufferedImage.getHeight() + options.getBottomPadding() + options.getTopPadding(), BASE_ADD_H),
                        null);
            } else if (result.getHeight() < contentH + bufferedImage.getHeight() + options.getBottomPadding()) {
                // 超过阀值
                result = GraphicUtil.createImg(options.getImgW(),
                        result.getHeight() + Math.max(bufferedImage.getHeight() + options.getBottomPadding() + options.getTopPadding(), BASE_ADD_H),
                        result);
            }

            // 更新实际高度
            int h = GraphicUtil.drawImage(result,
                    bufferedImage,
                    contentH,
                    options);
            contentH += h + options.getLinePadding();
            return this;
        }


        public BufferedImage asImage() {
            int leftPadding = 0;
            int topPadding = 0;
            int bottomPadding = 0;
            if(border) {
                leftPadding = this.borderLeftPadding;
                topPadding = this.borderTopPadding;
                bottomPadding = this.borderBottomPadding;
            }


            int x = leftPadding;
            int y = topPadding;


            // 实际生成图片的宽
            int realW = options.getImgW();
            // 实际生成图片的高
            int realH = contentH + options.getBottomPadding();

            BufferedImage bf = new BufferedImage((leftPadding << 1) + realW, realH + topPadding + bottomPadding, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = GraphicUtil.getG2d(bf);


            // 绘制边框
            if (border) {
                g2d.setColor(borderColor == null ? ColorUtil.OFF_WHITE : borderColor);
                g2d.fillRect(0, 0, realW + (leftPadding << 1), realH + topPadding + bottomPadding);


                // 绘制签名
                g2d.setColor(Color.GRAY);


                String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                borderSignText = borderSignText + "  " + date;

                int fSize = Math.min(15, realW / (borderSignText.length()));
                int addY = (borderBottomPadding - fSize) >> 1;
                g2d.setFont(new Font(ImgCreateOptions.DEFAULT_FONT.getName(), ImgCreateOptions.DEFAULT_FONT.getStyle(), fSize));
                g2d.drawString(borderSignText, x, y + addY + realH + g2d.getFontMetrics().getAscent());
            }


            // 绘制背景
            if (options.getBgImg() == null) {
                g2d.setColor(bgColor == null ? Color.WHITE : bgColor);
                g2d.fillRect(x, y, realW, realH);
            } else {
                g2d.drawImage(options.getBgImg(), x, y, realW, realH, null);
            }


            // 绘制内容
            g2d.drawImage(result, x, y,null);
            g2d.dispose();
            return bf;
        }


        public String asString() throws IOException {
            BufferedImage img = asImage();
            return Base64Util.encode(img, "png");
        }

        /**
         * 计算总行数
         *
         * @param strs     字符串列表
         * @param w        生成图片的宽
         * @param padding  渲染内容的左右边距
         * @param fontSize 字体大小
         * @return
         */
        private int calLineNum(String[] strs, int w, int padding, int fontSize) {
            // 每行的字符数
            double lineFontLen = Math.floor((w - (padding << 1)) / (double) fontSize);


            int totalLine = 0;
            for (String str : strs) {
                totalLine += Math.ceil(str.length() / lineFontLen);
            }

            return totalLine;
        }
    }


}
