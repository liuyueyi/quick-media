package com.github.hui.quick.plugin.image.wrapper.create;


import com.github.hui.quick.plugin.base.Base64Util;
import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.image.gif.GifHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 逐行生成gif图片
 * Created by yihui on 2017/9/15.
 */
public class LineGifCreateWrapper {

    public static Builder build() {
        return new LineGifCreateWrapper.Builder();
    }


    public static class Builder extends ImgCreateWrapper.Builder {

        private int delay = 300;

        protected List<BufferedImage> frameList = new ArrayList<>();

        public Builder setDelay(int delay) {
            this.delay = delay;
            return this;
        }

        protected Builder createImgFrame(BufferedImage img, int contentSize) {
            int realW, realH, x, y;
            if (getOptions().getDrawStyle() == ImgCreateOptions.DrawStyle.HORIZONTAL) {
                x = y = 0;
                realW = getOptions().getImgW();
                realH = contentSize;
            } else if (getOptions().getDrawStyle() == ImgCreateOptions.DrawStyle.VERTICAL_RIGHT) {
                realW = contentSize;
                realH = getOptions().getImgH();
                x = realW - img.getWidth();
                y = 0;
            } else {
                realW = contentSize;
                realH = getOptions().getImgH();
                x = y = 0;
            }


            BufferedImage bf = GraphicUtil.createImg(realW, realH, x, y, img);
            frameList.add(bf);
            return this;
        }

        public ImgCreateWrapper.Builder drawContent(String content) {
            super.drawContent(content);
            int contentSize = getOptions().getDrawStyle() == ImgCreateOptions.DrawStyle.HORIZONTAL ? getContentH() : getContentW();
            return createImgFrame(getResult(), contentSize);
        }


        public LineGifCreateWrapper.Builder drawImage(BufferedImage bufferedImage) {
            super.drawImage(bufferedImage);
            int contentSize = getOptions().getDrawStyle() == ImgCreateOptions.DrawStyle.HORIZONTAL ? getContentH() : getContentW();
            return createImgFrame(getResult(), contentSize);
        }


        private List<BufferedImage> asImages() {

            List<BufferedImage> gifFrame = new ArrayList<>();

            Point point = new Point();
            BufferedImage bg = createBg(point);
            int bgW = bg.getWidth();
            int x, y=0, w, h;
            int border = 0;

            if (isBorder()) {
                border = getBorderLeftPadding();
                y = getBorderTopPadding();
            }

            Graphics2D g2d;
            BufferedImage tmp;
            gifFrame.add(bg);
            for (BufferedImage img : frameList) {
                tmp = GraphicUtil.createImg(bg.getWidth(), bg.getHeight(), bg);
                g2d = GraphicUtil.getG2d(tmp);

                w = img.getWidth();
                h = img.getHeight();
                if (getOptions().getDrawStyle() == ImgCreateOptions.DrawStyle.VERTICAL_RIGHT) {
                    x = bgW - w - border;
                } else {
                    x = border;
                }

                g2d.drawImage(img, x, y, w, h, null);
                gifFrame.add(tmp);
            }


            return gifFrame;
        }


        public boolean asGif(String file) throws FileNotFoundException {
            GifHelper.saveGif(asImages(), delay, file);
            return true;
        }


        public String asString() {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            GifHelper.saveGif(asImages(), delay, outputStream);
            return Base64Util.encode(outputStream);
        }


        public byte[] asBytes() {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            GifHelper.saveGif(asImages(), delay, outputStream);
            return outputStream.toByteArray();
        }
    }

}
