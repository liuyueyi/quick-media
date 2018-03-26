package com.github.hui.quick.plugin.image.wrapper.expressionbk;


import com.github.hui.quick.plugin.base.ImageLoadUtil;
import com.github.hui.quick.plugin.image.gif.GifHelper;
import com.github.hui.quick.plugin.image.util.FontUtil;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 表情包生成封装类
 * <p>
 * 干掉图片生成的复杂逻辑，表情包分成以下几种
 * <p>
 * 纯文字 --》 动图
 * 文字 -》动态  +  静态图
 * 文字 -》 动态 + 一张动态logo图
 * 文字 -》 静态 + 动态图
 * 纯动态图
 * <p>
 * Created by yihui on 2017/9/14.
 */
public class ExpressionCreateWrapper {


    private ExpressionCreateOptions options;

    private ExpressionCreateWrapper(ExpressionCreateOptions options) {
        this.options = options;
    }


    public String asString() {
        return null;
    }

    public boolean asFile() {
        return true;
    }

    public OutputStream asStream() {


        return null;
    }


    @Getter
    public static class Builder {

        private List<BufferedImage> globalImgs = new ArrayList<>();

        private List<String> contents = new ArrayList<>();

        private Integer bgW;

        private Integer bgH;

        private Color bgColor;

        private BufferedImage bgImg;

        private ImgCreateOptions.DrawStyle style = ImgCreateOptions.DrawStyle.HORIZONTAL;


        private Font font = FontUtil.DEFAULT_FONT;


        private Color fontColor = Color.BLACK;

        private int delay;

        private int lastImgIndex;

        private List<ExpressionFrameEntity> frameEntities = new ArrayList<>();

        public Builder setBgW(Integer bgW) {
            this.bgW = bgW;
            return this;
        }

        public Builder setBgH(Integer bgH) {
            this.bgH = bgH;
            return this;
        }

        public Builder setBgColor(Color bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public Builder setBgImg(BufferedImage bgImg) {
            this.bgImg = bgImg;
            return this;
        }

        public Builder setStyle(ImgCreateOptions.DrawStyle style) {
            this.style = style;
            return this;
        }

        public Builder setFont(Font font) {
            this.font = font;
            return this;
        }

        public Builder setFontColor(Color fontColor) {
            this.fontColor = fontColor;
            return this;
        }

        public Builder setDelay(int delay) {
            this.delay = delay;
            return this;
        }

        public Builder setGif(String gif) throws IOException {
            delay = GifHelper.loadGif(gif, globalImgs);
            return this;
        }


        public Builder addImage(String img) throws IOException {
            return addImage(ImageLoadUtil.getImageByPath(img));
        }

        public Builder addImage(BufferedImage bf) {
            globalImgs.add(bf);
            return this;
        }


        public Builder setContent(String content) {


            contents.add(content);
            return frame();
        }


        public Builder appendContent(String content) {
            if (contents.size() > 0) {
                content = contents.get(contents.size() - 1) + content;
            }

            return setContent(content);
        }


        public Builder frame() {
            ExpressionFrameEntity frameEntity = new ExpressionFrameEntity();
            frameEntity.setDrawStyle(style);
            if (contents.size() > 0) {
                frameEntity.setFontColor(fontColor);
                frameEntity.setContent(contents.get(contents.size() - 1));
            }

            if (lastImgIndex >= globalImgs.size()) {
                lastImgIndex %= globalImgs.size();
            }
            frameEntity.setBg(globalImgs.get(lastImgIndex++));
            frameEntities.add(frameEntity);
            return this;
        }


        public ExpressionCreateWrapper builder() {
            ExpressionCreateOptions options = new ExpressionCreateOptions();
            options.setBgColor(bgColor);
            options.setW(bgW);
            options.setH(bgH);
            options.setDelay(delay);
            options.setFont(font);


            // 简单点实现，若图片还没完，则最后一条文字一直播放到最后一张动图
            ExpressionFrameEntity last;
            if (frameEntities.size() > 0) {
                last = frameEntities.get(frameEntities.size() - 1);
            } else {
                last = new ExpressionFrameEntity();
            }
            while (lastImgIndex < globalImgs.size()) {
                ExpressionFrameEntity frame = new ExpressionFrameEntity(last);
                frame.setBg(globalImgs.get(lastImgIndex++));
                frameEntities.add(frame);
            }

            options.setFrameList(frameEntities);
            return new ExpressionCreateWrapper(options);
        }


        private void calculateSize(ExpressionCreateOptions options) {
            int minImgW = Integer.MAX_VALUE, minImgH = Integer.MAX_VALUE;

            for (BufferedImage img : globalImgs) {
                if (minImgH > img.getHeight()) {
                    minImgH = img.getHeight();
                }

                if (minImgW > img.getWidth()) {
                    minImgW = img.getWidth();
                }
            }


            BufferedImage bf = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bf.createGraphics();
            g2d.setFont(font);
            FontMetrics fontMetrics = g2d.getFontMetrics();

            int maxContentRow;

        }
    }


}
