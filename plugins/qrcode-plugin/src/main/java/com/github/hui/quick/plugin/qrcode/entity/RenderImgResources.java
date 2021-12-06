package com.github.hui.quick.plugin.qrcode.entity;

import java.awt.image.BufferedImage;
import java.util.*;

/**
 * 图片渲染资源
 *
 * @author yihui
 * @date 21/7/27
 */
public class RenderImgResources {
    public static final int NO_LIMIT_COUNT = -1;
    private static Random random = new Random();

    private Map<DotSize, ImgDecorate> imgMapper;

    public RenderImgResources() {
        imgMapper = new HashMap<>(8);
    }

    public BufferedImage getImage(DotSize dotSize) {
        ImgDecorate renderImg = imgMapper.get(dotSize);
        if (renderImg == null) {
            return null;
        }
        try {
            return renderImg.getImg();
        } finally {
            if (renderImg.empty()) {
                imgMapper.remove(dotSize);
            }
        }
    }

    public void addImage(int row, int col, int count, BufferedImage img) {
        DotSize dotSize = new DotSize(row, col);
        ImgDecorate decorate = imgMapper.get(dotSize);
        if (decorate == null) {
            imgMapper.put(dotSize, new ImgDecorate(img, count));
        } else {
            decorate.addImg(img, count);
        }
    }

    public static class ImgDecorate {
        private List<RenderImg> imgList;

        public boolean empty() {
            return imgList.isEmpty();
        }

        public ImgDecorate(BufferedImage img) {
            this(img, NO_LIMIT_COUNT);
        }

        public ImgDecorate(BufferedImage img, int cnt) {
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
                if (img.count == 0) {
                    imgList.remove(index);
                }
                return img.img;
            }

            // 次数用完
            return getImg();
        }

        public List<RenderImg> getImgList() {
            return imgList;
        }
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
}
