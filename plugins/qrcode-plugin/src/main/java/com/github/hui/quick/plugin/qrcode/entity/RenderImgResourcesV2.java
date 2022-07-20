package com.github.hui.quick.plugin.qrcode.entity;

import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;

/**
 * v2 版，渲染池
 *
 * @author yihui
 * @date 2021/7/28
 */
public class RenderImgResourcesV2 {
    private static Logger log = LoggerFactory.getLogger(QrCodeGenWrapper.Builder.class);

    /**
     * 所有渲染的资源列表
     */
    private List<RenderSource> sourceList;

    /**
     * 兜底的1x1 渲染图
     */
    private RenderImgResources.ImgDecorate defaultRenderDrawImg;

    /**
     * 兜底的1x1 背景图
     */
    private RenderImgResources.ImgDecorate defaultRenderBgImg;


    public static RenderImgResourcesV2 create() {
        return new RenderImgResourcesV2();
    }

    private RenderImgResourcesV2() {
        sourceList = new ArrayList<>();
    }


    public List<RenderSource> getSourceList() {
        return sourceList;
    }

    public BufferedImage getDefaultDrawImg() {
        return defaultRenderDrawImg.getImg();
    }

    public BufferedImage getDefaultBgImg() {
        return defaultRenderBgImg == null ? null : defaultRenderBgImg.getImg();
    }

    public RenderSource addSource(int row, int col) {
        RenderSource renderSource = new RenderSource(this);
        renderSource.setRow(row);
        renderSource.setCol(col);
        return renderSource;
    }

    public static class RenderSource implements Comparable<RenderSource> {
        private static final int DEFAULT_ORDER = -999;
        private RenderImgResourcesV2 resources;
        private RenderImgResources.ImgDecorate imgDecorate;
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

        /**
         * 图片占用的二维码行数
         */
        private int row;

        /**
         * 图片占用的二维码列数
         */
        private int col;

        /**
         * 优先级，值越大，优先级越高
         */
        private int order;

        /**
         * true 表示全匹配，false 则表示只要有图的地方能满足即可
         */
        private boolean fullMatch;

        public RenderSource(RenderImgResourcesV2 resources) {
            this.resources = resources;
            missMap = new HashMap<>();
            order = DEFAULT_ORDER;
        }

        @Override
        public int compareTo(RenderSource o) {
            if (o == null) {
                return 1;
            }
            if (order == DEFAULT_ORDER) {
                order = row * col;
            }

            if (o.order == DEFAULT_ORDER) {
                o.order = o.row * o.col;
            }

            if (order > o.order) {
                return -1;
            } else if (order == o.order) {
                return 0;
            } else {
                return 1;
            }
        }

        public BufferedImage getImg() {
            return imgDecorate.getImg();
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
            return imgDecorate.empty();
        }

        public boolean isFullMatch() {
            return fullMatch;
        }

        public RenderSource addImg(String img) throws IOException {
            return addImg(img, RenderImgResources.NO_LIMIT_COUNT);
        }

        public RenderSource addImg(String img, int count) throws IOException {
            try {
                return addImg(ImageLoadUtil.getImageByPath(img), count);
            } catch (IOException e) {
                log.error("load img error: {}", img, e);
                throw new IOException("load logo error!", e);
            }
        }

        public RenderSource addImg(InputStream img) throws IOException {
            return addImg(img, RenderImgResources.NO_LIMIT_COUNT);
        }

        public RenderSource addImg(InputStream img, int count) throws IOException {
            try {
                return addImg(ImageIO.read(img), count);
            } catch (IOException e) {
                log.error("load img error!", e);
                throw new IOException("load logo error!", e);
            }
        }

        public RenderSource addImg(BufferedImage img) throws IOException {
            return addImg(img, RenderImgResources.NO_LIMIT_COUNT);
        }

        public RenderSource addImg(BufferedImage img, int count) {
            if (this.imgDecorate == null) {
                this.imgDecorate = new RenderImgResources.ImgDecorate(img, count);
            } else {
                this.imgDecorate.addImg(img, count);
            }
            return this;
        }

        public RenderSource setRow(int row) {
            this.row = row;
            return this;
        }

        public RenderSource setCol(int col) {
            this.col = col;
            return this;
        }

        public RenderSource setOrder(int order) {
            this.order = order;
            return this;
        }

        public RenderSource setMiss(int x, int y) {
            this.missMap.put(new Point(x, y), true);
            return this;
        }

        public RenderSource setFullMatch() {
            this.fullMatch = true;
            return this;
        }

        /**
         * 形如：x1-y1,x2-y2
         * 对于十字图片，传参可以是
         * 0-0,2-0,0-2,2-2
         *
         * @param xy
         * @return
         */
        public RenderSource setMiss(String xy) {
            String[] points = StringUtils.split(xy, ",");
            for (String point : points) {
                String[] cells = StringUtils.split(point, "-");
                if (cells.length != 2) {
                    throw new IllegalArgumentException("传参不满足 x1-y1,x2-y2规范!");
                }

                setMiss(Integer.parseInt(cells[0].trim()), Integer.parseInt(cells[1].trim()));
            }
            return this;
        }

        public RenderImgResourcesV2 build() {
            if (row > 1 || col > 1 || !BooleanUtils.isTrue(missMap.get(new Point(0, 0)))) {
                resources.sourceList.add(this);
            }
            Collections.sort(resources.sourceList);
            initDefaultRenderImg();
            return resources;
        }

        /**
         * 从现有的资源中，初始化兜底的 1x1 渲染图
         */
        private void initDefaultRenderImg() {
            if (row > 1 || col > 1) {
                return;
            }

            if (resources.defaultRenderBgImg != null && resources.defaultRenderDrawImg != null) {
                return;
            }

            RenderImgResources.ImgDecorate decorate = null;
            for (RenderImgResources.RenderImg renderImg : imgDecorate.getImgList()) {
                // 找一个不限制渲染次数的
                if (renderImg.count == RenderImgResources.NO_LIMIT_COUNT) {
                    if (decorate == null) {
                        decorate = new RenderImgResources.ImgDecorate(renderImg.img);
                    } else {
                        decorate.addImg(renderImg.img, RenderImgResources.NO_LIMIT_COUNT);
                    }
                }
            }
            if (decorate == null) {
                // 所有的都是次数限制，则全部加进去
                for (RenderImgResources.RenderImg renderImg : imgDecorate.getImgList()) {
                    if (decorate == null) {
                        decorate = new RenderImgResources.ImgDecorate(renderImg.img);
                    } else {
                        decorate.addImg(renderImg.img, RenderImgResources.NO_LIMIT_COUNT);
                    }
                }
            }

            if (missMap.isEmpty() && resources.defaultRenderDrawImg == null) {
                resources.defaultRenderDrawImg = decorate;
            } else if (BooleanUtils.isTrue(missMap.get(new Point(0, 0))) && resources.defaultRenderBgImg == null) {
                resources.defaultRenderBgImg = decorate;
            }
        }
    }
}
