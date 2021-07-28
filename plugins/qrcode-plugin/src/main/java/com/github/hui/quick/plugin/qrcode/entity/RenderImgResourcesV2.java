package com.github.hui.quick.plugin.qrcode.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * v2 版，渲染池
 *
 * @author yihui
 * @date 2021/7/28
 */
public class RenderImgResourcesV2 {

    private List<RenderSource> sourceList;

    /**
     * 兜底的1x1的渲染图
     */
    private RenderImgResources.ImgDecorate defaultImgDecorate;

    public List<RenderSource> getSourceList() {
        return sourceList;
    }

    public BufferedImage getDefaultImg() {
        return defaultImgDecorate.getImg(DotSize.SIZE_1_1);
    }

    public static class RenderSource implements Comparable<RenderSource> {
        private BufferedImage img;
        /**
         * 表示 row * col 的图片资源中，哪些地方是有素材填充的（坐标从左上角开始）
         * 比如一个十字行的素材，如下，0表示空白，1表示有资源
         * 0 1 0
         * 1 1 1
         * 0 1 0
         * 对应的missMap 值为:
         * (0, 0) -> true,  (0, 1) -> false/null, (0, 2) -> true
         * (1, 0) -> false/null, (1, 1) -> false/null, (1,2) -> false/null
         * (2, 0) -> true, (2, 1) -> false/null, (2,2) -> true
         * <p>
         * 特殊case，若fillMap为空，则表示完整填充，不存在缺失的场景
         */
        private Map<Point, Boolean> missMap;

        private int row;

        private int col;

        /**
         * 最多出现的次数，若为-1，表示可以一直出现
         */
        private int count;

        /**
         * 优先级，值越大，优先级越高
         */
        private int order;

        @Override
        public int compareTo(RenderSource o) {
            if (o == null) {
                return 1;
            }
            if (order > o.order) {
                return 1;
            } else if (order == o.order) {
                return 0;
            } else {
                return -1;
            }
        }

        public BufferedImage getImg() {
            return img;
        }

        public Map<Point, Boolean> getMissMap() {
            return missMap;
        }

        /**
         * 判断对应区域是否存在绘图资源
         *
         * @param x
         * @param y
         * @return
         */
        public boolean miss(int x, int y) {
            Boolean miss = missMap.get(new Point(x, y));
            if (miss == null) {
                // 不存在，表示没有问题
                return false;
            }
            return miss;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public boolean countOver() {
            return count == 0;
        }

        public int getOrder() {
            return order;
        }

        public void autoUpdateCount() {
            if (count == RenderImgResources.NO_LIMIT_COUNT) {
                return;
            }
            count -= 1;
        }
    }

}
