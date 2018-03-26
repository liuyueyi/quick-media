package com.github.hui.quick.plugin.image.wrapper.create;

import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.image.helper.ImgDrawHelper;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 逐字绘制的gif图片生成
 * <p>
 * Created by yihui on 2017/9/15.
 */
public class WordGifCreateWrapper {

    public static Builder build() {
        return new Builder();
    }


    public static class Builder extends LineGifCreateWrapper.Builder {
        public ImgCreateWrapper.Builder drawContent(String content) {
            switch (getOptions().getDrawStyle()) {
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


        @Override
        protected void doDrawContent(String[] strs, int fontHeight) {
            BufferedImage result = getResult();
            int contentH = getContentH();

            Graphics2D g2d;
            int index = 0, contentSize;
            BufferedImage temp;
            for (String str : strs) {
                ++index;
                for (int i = 0; i < str.length(); i++) {
                    temp = GraphicUtil.createImg(result.getWidth(), result.getHeight(), result);
                    g2d = GraphicUtil.getG2d(temp);

                    contentSize = ImgDrawHelper.drawContent(g2d, str, i,
                            contentH + (fontHeight + getOptions().getLinePadding()) * index
                            , getOptions());
                    createImgFrame(temp, contentSize);
                }


                g2d = GraphicUtil.getG2d(result);
                ImgDrawHelper.drawContent(g2d, str,
                        contentH + (fontHeight + getOptions().getLinePadding()) * index
                        , getOptions());
            }
        }


        protected void doDrawVerticalLeftContent(String[] strs, int fontSize) {
            BufferedImage result = getResult();
            int contentW = getContentW();


            BufferedImage temp;
            Graphics2D g2d;
            // 绘制文字
            int index = 0, contentSize;
            for (String str : strs) {
                for (int i = 0; i < str.length(); i++) {
                    temp = GraphicUtil.createImg(result.getWidth(), result.getHeight(), result);
                    g2d = GraphicUtil.getG2d(temp);


                    contentSize = ImgDrawHelper.drawVerticalContent(g2d, str, i,
                            contentW + (fontSize + getOptions().getLinePadding()) * (index)
                            , getOptions());

                    createImgFrame(temp, contentSize);
                }


                g2d = GraphicUtil.getG2d(result);
                ImgDrawHelper.drawVerticalContent(g2d, str,
                        contentW + (fontSize + getOptions().getLinePadding()) * (index)
                        , getOptions());
                index++;
            }
        }


        protected void doDrawVerticalRightContent(String[] strs, int fontSize) {

            BufferedImage result = getResult();
            int contentW = getContentW();


            BufferedImage temp;
            Graphics2D g2d;

            // 绘制文字
            int index = 0, contentSize;
            int offsetX = result.getWidth() - contentW;
            for (String str : strs) {
                ++index;
                for (int i = 0; i < str.length(); i++) {
                    temp = GraphicUtil.createImg(result.getWidth(), result.getHeight(), result);
                    g2d = GraphicUtil.getG2d(temp);


                    contentSize = ImgDrawHelper.drawVerticalContent(g2d, str, i,
                            offsetX - (fontSize + getOptions().getLinePadding()) * index
                            , getOptions());
                    contentSize = temp.getWidth() - contentSize;


                    createImgFrame(temp, contentSize);
                }


                g2d = GraphicUtil.getG2d(result);
                ImgDrawHelper.drawVerticalContent(g2d, str,
                        offsetX - (fontSize + getOptions().getLinePadding()) * index
                        , getOptions());
            }
        }
    }

}
