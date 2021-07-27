package com.github.hui.quick.plugin.qrcode.entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author yihui
 * @date 21/7/27
 */
public class RenderImgDecorate {
    public static final int NO_LIMIT_COUNT = -1;
    private static Random random = new Random();

    private List<RenderImg> imgList;

    public boolean empty() {
        return imgList.isEmpty();
    }

    public static class RenderImg {
        /**
         * 绘制图
         */
        BufferedImage img;
        /**
         * -1表示不限次数， >1 表示最多出现的次数
         */
        int count;

        public RenderImg(BufferedImage img, int count) {
            this.img = img;
            this.count = count;
        }
    }

    public RenderImgDecorate(BufferedImage img) {
        this(img, NO_LIMIT_COUNT);
    }

    public RenderImgDecorate(BufferedImage img, int cnt) {
        imgList = new ArrayList<>();
        imgList.add(new RenderImg(img, cnt));
    }

    public void addImg(BufferedImage img, int cnt) {
        imgList.add(new RenderImg(img, cnt));
    }

    public BufferedImage getImg() {
        if (imgList.size() == 0) {
            return null;
        }

        int index = random.nextInt(imgList.size());
        RenderImg img = imgList.get(index);
        if (img.count == NO_LIMIT_COUNT) {
            return img.img;
        } else if (img.count > 0) {
            img.count -= 1;
            return img.img;
        } else {
            // 次数用完
            imgList.remove(index);
            return getImg();
        }
    }

}
