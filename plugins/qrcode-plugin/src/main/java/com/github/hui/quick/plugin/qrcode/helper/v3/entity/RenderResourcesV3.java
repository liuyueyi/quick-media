package com.github.hui.quick.plugin.qrcode.helper.v3.entity;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

/**
 * 二维矩阵渲染
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class RenderResourcesV3<T> {
    public static final int NO_LIMIT_COUNT = -1;

    /**
     * 所有渲染的资源列表
     */
    private List<RenderSource<T>> sourceList;

    /**
     * 兜底的1x1 渲染图
     */
    private ResourceDecorate<T> defaultRenderDrawImg;

    /**
     * 兜底的1x1 背景图
     */
    private ResourceDecorate<T> defaultRenderBgImg;


    public static RenderResourcesV3 create() {
        return new RenderResourcesV3<>();
    }

    private RenderResourcesV3() {
        sourceList = new ArrayList<>();
    }


    public List<RenderSource<T>> getSourceList() {
        return sourceList;
    }

    public T getDefaultDrawImg() {
        return defaultRenderDrawImg.getResource();
    }

    public T getDefaultBgImg() {
        return defaultRenderBgImg == null ? null : defaultRenderBgImg.getResource();
    }

    public RenderSource<T> addSource(int row, int col) {
        RenderSource<T> renderSource = new RenderSource<>(this);
        renderSource.setRow(row);
        renderSource.setCol(col);
        return renderSource;
    }

    public static class RenderSource<T> implements Comparable<RenderSource<T>> {
        private static final int DEFAULT_ORDER = -999;
        private RenderResourcesV3<T> resRef;
        private ResourceDecorate<T> resourceDecorate;
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

        public RenderSource(RenderResourcesV3<T> resources) {
            this.resRef = resources;
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

        public T getResource() {
            return resourceDecorate.getResource();
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
            return resourceDecorate.empty();
        }

        public boolean isFullMatch() {
            return fullMatch;
        }

        public RenderSource addImg(T img) throws IOException {
            return addImg(img, NO_LIMIT_COUNT);
        }

        public RenderSource addImg(T img, int count) {
            if (this.resourceDecorate == null) {
                this.resourceDecorate = new ResourceDecorate<>(img, count);
            } else {
                this.resourceDecorate.addResource(img, count);
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
        public RenderSource<T> setMiss(String xy) {
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

        public RenderResourcesV3<T> build() {
            if (row > 1 || col > 1 || !BooleanUtils.isTrue(missMap.get(new Point(0, 0)))) {
                resRef.sourceList.add(this);
            }
            Collections.sort(resRef.sourceList);
            initDefaultRenderImg();
            return resRef;
        }

        /**
         * 从现有的资源中，初始化兜底的 1x1 渲染图
         */
        private void initDefaultRenderImg() {
            if (row > 1 || col > 1) {
                return;
            }

            if (resRef.defaultRenderBgImg != null && resRef.defaultRenderDrawImg != null) {
                return;
            }

            ResourceDecorate<T> decorate = null;
            for (RenderResource<T> renderImg : resourceDecorate.getResourceList()) {
                // 找一个不限制渲染次数的
                if (renderImg.count == NO_LIMIT_COUNT) {
                    if (decorate == null) {
                        decorate = new ResourceDecorate<>(renderImg.resource);
                    } else {
                        decorate.addResource(renderImg.resource, NO_LIMIT_COUNT);
                    }
                }
            }
            if (decorate == null) {
                // 所有的都是次数限制，则全部加进去
                for (RenderResource<T> renderImg : resourceDecorate.getResourceList()) {
                    if (decorate == null) {
                        decorate = new ResourceDecorate<>(renderImg.resource);
                    } else {
                        decorate.addResource(renderImg.resource, NO_LIMIT_COUNT);
                    }
                }
            }

            if (missMap.isEmpty() && resRef.defaultRenderDrawImg == null) {
                resRef.defaultRenderDrawImg = decorate;
            } else if (BooleanUtils.isTrue(missMap.get(new Point(0, 0))) && resRef.defaultRenderBgImg == null) {
                resRef.defaultRenderBgImg = decorate;
            }
        }
    }


    public static class ResourceDecorate<T> {
        private static Random random = new Random();

        private List<RenderResource<T>> resourceList;

        public boolean empty() {
            return resourceList.isEmpty();
        }

        public ResourceDecorate(T resource) {
            this(resource, NO_LIMIT_COUNT);
        }

        public ResourceDecorate(T resource, int cnt) {
            resourceList = new ArrayList<>();
            resourceList.add(new RenderResource<>(resource, cnt));
        }

        public void addResource(T img, int cnt) {
            resourceList.add(new RenderResource<>(img, cnt));
        }

        public T getResource() {
            if (resourceList.size() == 0) {
                return null;
            }

            int index = random.nextInt(resourceList.size());
            RenderResource<T> resource = resourceList.get(index);
            if (resource.count == NO_LIMIT_COUNT) {
                return resource.resource;
            } else if (resource.count > 0) {
                resource.count -= 1;
                if (resource.count == 0) {
                    resourceList.remove(index);
                }
                return resource.resource;
            }

            // 次数用完
            return getResource();
        }

        public List<RenderResource<T>> getResourceList() {
            return resourceList;
        }
    }

    public static class RenderResource<T> {
        /**
         * 绘制图
         */
        T resource;
        /**
         * -1表示不限次数， >1 表示最多出现的次数
         */
        int count;

        public RenderResource(T img, int count) {
            this.resource = img;
            this.count = count;
        }
    }
}
