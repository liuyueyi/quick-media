package com.github.hui.quick.plugin.image.wrapper.emoticon;


import com.github.hui.quick.plugin.base.Base64Util;
import com.github.hui.quick.plugin.base.ColorUtil;
import com.github.hui.quick.plugin.base.FileReadUtil;
import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.image.gif.GifDecoder;
import com.github.hui.quick.plugin.image.gif.GifHelper;
import com.github.hui.quick.plugin.image.helper.CalculateHelper;
import com.github.hui.quick.plugin.image.util.PunctuationUtil;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import com.github.hui.quick.plugin.image.wrapper.create.LineGifCreateWrapper;
import com.github.hui.quick.plugin.image.wrapper.create.WordGifCreateWrapper;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 表情包封装类，目前只支持静态文字，动态图片
 * <p>
 * 对于动态文字，静态图片的，采用 {@link WordGifCreateWrapper}, {@link LineGifCreateWrapper}
 * <p>
 * Created by yihui on 2017/9/19.
 */
public class EmotionWrapper {

    private EmotionOptions options;

    private EmotionWrapper(EmotionOptions options) {
        this.options = options;
    }

    public static Builder ofContent(String content) {
        Builder builder = new Builder();
        return builder.setContent(content);
    }


    public static Builder ofImage(String img) throws IOException {
        Builder builder = new Builder();
        return builder.setGif(img);
    }


    public void asFile(String out) throws IOException {
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(out));
        draw(outputStream);
        outputStream.close();
    }


    public String asString() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        draw(outputStream);
        return Base64Util.encode(outputStream);
    }

    private void draw(OutputStream outputStream) {
        int w = options.getW(), h = options.getH();
        int gifW = options.getGifW(), gifH = options.getGifH();
        int leftPadding = options.getLeftPadding(), rightPadding = options.getRightPadding(), topPadding = options.getTopPadding(), bottomPadding = options.getBottomPadding();
        int contentSize = options.getContentSize();

        BufferedImage result = new BufferedImage(options.getW(), options.getH(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = GraphicUtil.getG2d(result);
        // 先绘制背景
        g2d.setFont(options.getFont());
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(options.getFontColor());


        int imgX = 0, imgY = 0;
        int contentX = 0, contentY = 0;

        if (options.getDrawStyle() == ImgCreateOptions.DrawStyle.HORIZONTAL) {
            // 水平时
            imgX = (options.getW() - options.getGifW()) >> 1;
            if (options.isImgFirst()) {
                imgY = options.getTopPadding();
                contentY = ((h - topPadding - bottomPadding - contentSize - gifH) >> 1) + topPadding + gifH + g2d.getFont().getSize();
            } else {
                imgY = options.getH() - options.getBottomPadding() - options.getGifH();
                contentY = ((h - topPadding - bottomPadding - contentSize - gifH) >> 1) + topPadding + g2d.getFont().getSize();
            }
        } else if (options.getDrawStyle() == ImgCreateOptions.DrawStyle.VERTICAL_LEFT) {
            imgY = (options.getH() - options.getGifH()) >> 1;
            if (options.isImgFirst()) { // 从左到右， 图片优先
                imgX = options.getLeftPadding();
                contentX = imgX + gifW + ((w - imgX - gifW - contentSize) >> 1);
            } else {
                imgX = w - rightPadding - gifW;
                contentX = leftPadding + ((imgX - contentSize - leftPadding) >> 1);
            }

        } else if (options.getDrawStyle() == ImgCreateOptions.DrawStyle.VERTICAL_RIGHT) {
            imgY = (options.getH() - options.getGifH()) >> 1;
            if (options.isImgFirst()) {
                imgX = w - rightPadding - gifW;
                contentX = imgX - ((imgX - leftPadding - contentSize) >> 1) - g2d.getFont().getSize() - options.getLinePadding();
            } else {
                imgX = options.getLeftPadding();
                contentX = w - rightPadding - ((w - rightPadding - gifW - leftPadding - contentSize) >> 1) - g2d.getFont().getSize() - options.getLinePadding();
            }
        }

        // 绘制文本
        drawContent(g2d, contentX, contentY, options.getDrawStyle());


        // 绘制图片
        List<BufferedImage> frames = new ArrayList<>();
        for (BufferedImage img : options.getImgs()) {
            frames.add(drawImage(result, img, imgX, imgY, gifW, gifH));
        }


        GifHelper.saveGif(frames, options.getDelay(), outputStream);
    }


    private void drawContent(Graphics2D g2d, int x, int y, ImgCreateOptions.DrawStyle drawStyle) {
        int lineSize;
        if (drawStyle == ImgCreateOptions.DrawStyle.HORIZONTAL) {
            lineSize = options.getW() - options.getLeftPadding() - options.getRightPadding();
            for (String msg : options.getContent()) {
                y = doDrawContent(g2d, msg, y, lineSize, options.getLinePadding());
            }
        } else if (drawStyle == ImgCreateOptions.DrawStyle.VERTICAL_LEFT) {
            lineSize = options.getH() - options.getTopPadding() - options.getBottomPadding();
            for (String msg : options.getContent()) {
                x = doDrawVerticalContent(g2d, msg, lineSize, x, options.getLinePadding(), false);
            }
        } else {
            lineSize = options.getH() - options.getTopPadding() - options.getBottomPadding();
            for (String msg : options.getContent()) {
                x = doDrawVerticalContent(g2d, msg, lineSize, x, options.getLinePadding(), true);
            }
        }
    }


    private BufferedImage drawImage(BufferedImage bg, BufferedImage source, int x, int y, int w, int h) {
        BufferedImage copy = GraphicUtil.createImg(bg.getWidth(), bg.getHeight(), bg);
        Graphics2D g2d = GraphicUtil.getG2d(copy);
        g2d.drawImage(source, x, y, w, h, null);
        g2d.dispose();
        return copy;
    }


    private int doDrawContent(Graphics2D g2d, String content, int y, int lineSize, int linePadding) {
        String[] strs = CalculateHelper.splitStr(content, lineSize, g2d.getFontMetrics());
        int index = 0;
        int x;
        int lineWidth = linePadding + g2d.getFontMetrics().getHeight();
        for (String tmp : strs) {
            x = (lineSize - g2d.getFontMetrics().stringWidth(tmp)) >> 1;
            g2d.drawString(tmp, options.getLeftPadding() + x, y + lineWidth * index);
            index++;
        }
        return y + lineWidth * index;
    }


    public int doDrawVerticalContent(Graphics2D g2d,
                                     String content,
                                     int lineLen,
                                     int x,
                                     int linePadding,
                                     boolean isRight) {

        FontMetrics fontMetrics = g2d.getFontMetrics();

        // 实际填充内容的高度， 需要排除上下间距
        String[] strs = CalculateHelper.splitVerticalStr(content, lineLen, fontMetrics);
        int fontSize = g2d.getFont().getSize();
        int fontWidth = fontSize + linePadding;
        if (isRight) { // 从右往左绘制时，偏移量为负
            fontWidth = -fontWidth;
        }


        int lastX = x, lastY, startY, tmpCharOffsetX = 0;
        int initY = options.getTopPadding() + fontMetrics.getAscent();
        for (String tmp : strs) {
            lastY = initY;
            startY = (lineLen - (fontMetrics.stringWidth(tmp) + fontMetrics.getDescent() * (tmp.length() - 1))) >> 1;

            for (int i = 0; i < tmp.length(); i++) {
                tmpCharOffsetX = PunctuationUtil.isPunctuation(tmp.charAt(i)) ? fontSize >> 1 : 0;

                g2d.drawString(tmp.charAt(i) + "",
                        lastX + tmpCharOffsetX,
                        startY + lastY);

                lastY += fontMetrics.charWidth(tmp.charAt(i)) + fontMetrics.getDescent();
            }
            lastX += fontWidth;
        }

        return lastX;
    }


    @Getter
    public static class Builder {
        private EmotionOptions options = new EmotionOptions();

        private int tempGifW;
        private int tempGifH;


        public Builder setW(int w) {
            this.options.setW(w);
            return this;
        }

        public Builder setH(int h) {
            this.options.setH(h);
            return this;
        }

        public Builder setFont(Font font) {
            this.options.setFont(font);
            return this;
        }

        public Builder setFontColor(Color color) {
            this.options.setFontColor(color);
            return this;
        }

        public Builder setFontColor(int color) {
            return setFontColor(ColorUtil.int2color(color));
        }

        public Builder setContent(String content) {
            content = content.replaceAll("\n\n", "\n \n");
            this.options.setContent(StringUtils.split(content, "\n"));
            return this;
        }


        public Builder setGif(String gif) throws IOException {
            return setGif(FileReadUtil.getStreamByFileName(gif));
        }

        public Builder setGif(InputStream inputStream) {
            GifDecoder gifDecoder = new GifDecoder();
            gifDecoder.read(inputStream);

            List<BufferedImage> frames = new ArrayList<>();
            BufferedImage bf;
            int tempDelay = 0;
            for (int i = 0; i < gifDecoder.getFrameCount(); i++) {
                bf = gifDecoder.getFrame(i);
                frames.add(bf);

                tempDelay = Math.max(tempDelay, gifDecoder.getDelay(i));
            }

            this.options.setDelay(tempDelay);
            this.options.setImgs(frames);

            this.tempGifW = gifDecoder.getImage().getWidth();
            this.tempGifH = gifDecoder.getImage().getHeight();
            return this;
        }

        public Builder setGif(List<BufferedImage> imgs) {
            int tmpW = Integer.MAX_VALUE,  tmpH = Integer.MAX_VALUE;

            for (BufferedImage img : imgs) {
                if (tmpW < img.getWidth()) {
                    tmpW = img.getWidth();
                }

                if(tmpH < img.getHeight()) {
                    tmpH = img.getHeight();
                }
            }


            this.options.setImgs(imgs);
            this.tempGifW = tmpW;
            this.tempGifH = tmpH;

            return this;
        }


        public Builder setImgFirst(boolean imgFirst) {
            this.options.setImgFirst(imgFirst);
            return this;
        }


        public Builder setDrawStyle(String drawStyle) {
            return setDrawStyle(ImgCreateOptions.DrawStyle.getStyle(drawStyle));
        }

        public Builder setDrawStyle(ImgCreateOptions.DrawStyle drawStyle) {
            this.options.setDrawStyle(drawStyle);
            return this;
        }


        public Builder setLeftPadding(int leftPadding) {
            this.options.setLeftPadding(leftPadding);
            return this;
        }

        public Builder setRightPadding(int rightPadding) {
            this.options.setRightPadding(rightPadding);
            return this;
        }

        public Builder setTopPadding(int topPadding) {
            this.options.setTopPadding(topPadding);
            return this;
        }

        public Builder setBottomPadding(int bottomPadding) {
            this.options.setBottomPadding(bottomPadding);
            return this;
        }

        public Builder setLinePadding(int linePadding) {
            this.options.setLinePadding(linePadding);
            return this;
        }


        public Builder setDelay(int delay) {
            this.options.setDelay(delay);
            return this;
        }

        public void calRealSize() {
            int realW, realH;
            int gifW, gifH;

            BufferedImage result = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = GraphicUtil.getG2d(result);
            g2d.setFont(options.getFont());

            FontMetrics fontMetrics = g2d.getFontMetrics();
            int fontSize = fontMetrics.getHeight();


            if (options.getDrawStyle() == ImgCreateOptions.DrawStyle.HORIZONTAL) {
                realW = options.getW();

                int padding = options.getLeftPadding() + options.getRightPadding();
                int lineNum = CalculateHelper.calLineNum(options.getContent(), realW - padding, fontMetrics);
                int contentSize = (fontSize + options.getLinePadding()) * lineNum + options.getLinePadding();
                options.setContentSize(contentSize - (options.getLinePadding() << 1)); // 实际的文本高度

                if (tempGifW + padding < realW) {
                    //  gif的大小为实际大小
                    gifW = tempGifW;
                    gifH = tempGifH;
                } else {
                    gifW = realW - padding;
                    gifH = tempGifH * gifW / tempGifW;

                }

                realH = gifH + options.getTopPadding() + options.getBottomPadding() + contentSize;
                realH = Math.max(realH, options.getH());
            } else { // 竖排文字
                realH = options.getH();

                int padding = options.getTopPadding() + options.getBottomPadding();
                int lineNum = CalculateHelper.calVerticalLineNum(options.getContent(), realH - padding, fontMetrics);
                int contentSize = (fontSize + options.getLinePadding()) * lineNum + options.getLinePadding();
                options.setContentSize(contentSize - (options.getLinePadding() << 1)); // 实际的文本宽度

                if (tempGifH + padding < realH) {
                    gifW = tempGifW;
                    gifH = tempGifH;
                } else {
                    gifH = realH - padding;
                    gifW = tempGifW * gifH / tempGifH;
                }
                realW = gifW + options.getLeftPadding() + options.getRightPadding() + contentSize;
                realW = Math.max(realW, options.getW());
            }


            options.setW(realW);
            options.setH(realH);

            options.setGifW(gifW);
            options.setGifH(gifH);
        }


        public EmotionWrapper build() {
            // 计算最终的图片宽高
            calRealSize();
            return new EmotionWrapper(options);
        }
    }


}
