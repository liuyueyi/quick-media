package com.github.hui.quick.plugin.qrcode.v3.entity;

import com.github.hui.quick.plugin.qrcode.v3.req.DrawOptions;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * 二维矩阵渲染资源池
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class QrResourcePool {
    public static final int NO_LIMIT_COUNT = -1;

    private final DrawOptions ref;

    /**
     * 全局共享资源，如svg的通用样式；图片二维码中的全局字体信息等
     */
    private QrResource globalResource;

    /**
     * 所有渲染的资源列表
     */
    private final List<QrResourcesDecorate> sourceList;

    /**
     * 兜底的1x1 渲染图
     */
    private QrResourceSet defaultRenderDrawResource;

    /**
     * 兜底的1x1 背景图
     */
    private QrResourceSet defaultRenderBgResource;


    public static QrResourcePool create(DrawOptions ref) {
        return new QrResourcePool(ref);
    }

    private QrResourcePool(DrawOptions ref) {
        this.ref = ref;
        sourceList = new ArrayList<>();
    }

    public DrawOptions over() {
        return this.ref;
    }

    public QrResource getGlobalResource() {
        return globalResource;
    }

    public QrResourcePool setGlobalResource(QrResource globalResource) {
        this.globalResource = globalResource;
        return this;
    }

    public List<QrResourcesDecorate> getSourceList() {
        return sourceList;
    }

    /**
     * 默认的兜底绘制图
     *
     * @return
     */
    public QrResource getDefaultDrawResource() {
        return defaultRenderDrawResource == null ? null : defaultRenderDrawResource.getResource();
    }

    /**
     * 默认的背景图
     *
     * @return
     */
    public QrResource getDefaultBgResource() {
        return defaultRenderBgResource == null ? null : defaultRenderBgResource.getResource();
    }

    /**
     * 注意，将width, height, miss相同的资源，放在一个QrResourcesDecorate内；每次调用createSources都会创建一个新的QrResourcesDecorate
     *
     * @param width
     * @param height
     * @param resource
     * @return
     */
    public QrResourcesDecorate createSource(int width, int height, QrResource resource) {
        return new QrResourcesDecorate(this).setWidth(width).setHeight(height).addResource(resource);
    }

    public QrResourcesDecorate createSource(int width, int height) {
        return new QrResourcesDecorate(this).setWidth(width).setHeight(height);
    }

    /**
     * 资源装饰类, 用于定义每个资源对应的矩阵类型
     */
    public static class QrResourcesDecorate implements Comparable<QrResourcesDecorate> {
        private static final int DEFAULT_ORDER = -999;
        private final QrResourcePool resRef;
        private QrResourceSet resourceDecorate;
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
        private final Map<Point, Boolean> missMap;

        /**
         * 图片占用的二维码行数
         */
        private int height;

        /**
         * 图片占用的二维码列数
         */
        private int width;

        /**
         * 优先级，值越大，优先级越高
         */
        private int order;

        /**
         * true 表示全匹配
         * false 则表示只要有图的地方能满足即可
         * <p>
         * 举例说明，现在有一个 十字图片
         * 当 fullMatch = true, 则对应一个 3 x 3 的区域的，当 (0,0),(0,2),(2,0),(2,2) 四个位置为0  且 (0,1),(1,0),(1,1),(1,2),(2,1) 五个位置为1时，这个资源才能使用
         * 当 fullMatch = false, 则对应 3 x 3 区域，只要 (0,1),(1,0),(1,1),(1,2),(2,1) 五个位置为1，这个资源就可以使用
         */
        private boolean fullMatch;

        public QrResourcesDecorate(QrResourcePool resources) {
            this.resRef = resources;
            missMap = new HashMap<>();
            order = DEFAULT_ORDER;
        }

        @Override
        public int compareTo(QrResourcesDecorate o) {
            if (o == null) {
                return 1;
            }
            if (order == DEFAULT_ORDER) {
                order = height * width;
            }

            if (o.order == DEFAULT_ORDER) {
                o.order = o.height * o.width;
            }

            if (order > o.order) {
                return -1;
            } else if (order == o.order) {
                return 0;
            } else {
                return 1;
            }
        }

        public QrResource getResource() {
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

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public boolean countOver() {
            return resourceDecorate.empty();
        }

        public boolean isFullMatch() {
            return fullMatch;
        }

        public QrResourcesDecorate addResource(String img) {
            return addResource(new QrResource(img));
        }

        public QrResourcesDecorate addResource(QrResource img) {
            return addResource(img, NO_LIMIT_COUNT);
        }

        public QrResourcesDecorate addResource(String img, int count) {
            return addResource(new QrResource(img), count);
        }

        public QrResourcesDecorate addResource(QrResource img, int count) {
            if (this.resourceDecorate == null) {
                this.resourceDecorate = new QrResourceSet(img, count);
            } else {
                this.resourceDecorate.addResource(img, count);
            }
            return this;
        }

        public QrResourcesDecorate setHeight(int height) {
            this.height = height;
            return this;
        }

        public QrResourcesDecorate setWidth(int width) {
            this.width = width;
            return this;
        }

        public QrResourcesDecorate setOrder(int order) {
            this.order = order;
            return this;
        }

        public QrResourcesDecorate setMiss(int x, int y) {
            this.missMap.put(new Point(x, y), true);
            return this;
        }

        public QrResourcesDecorate setFullMatch() {
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
        public QrResourcesDecorate setMiss(String xy) {
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

        public QrResourcePool build() {
            if (height > 1 || width > 1 || !BooleanUtils.isTrue(missMap.get(new Point(0, 0)))) {
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
            if (height > 1 || width > 1) {
                return;
            }

            if (resRef.defaultRenderBgResource != null && resRef.defaultRenderDrawResource != null) {
                return;
            }

            QrResourceSet decorate = null;
            for (QrCountResource renderImg : resourceDecorate.getResourceList()) {
                // 找一个不限制渲染次数的
                if (renderImg.count == NO_LIMIT_COUNT) {
                    if (decorate == null) {
                        decorate = new QrResourceSet(renderImg.resource);
                    } else {
                        decorate.addResource(renderImg.resource, NO_LIMIT_COUNT);
                    }
                }
            }
            if (decorate == null) {
                // 所有的都是次数限制，则全部加进去
                for (QrCountResource renderImg : resourceDecorate.getResourceList()) {
                    if (decorate == null) {
                        decorate = new QrResourceSet(renderImg.resource);
                    } else {
                        decorate.addResource(renderImg.resource, NO_LIMIT_COUNT);
                    }
                }
            }

            if (missMap.isEmpty() && resRef.defaultRenderDrawResource == null) {
                resRef.defaultRenderDrawResource = decorate;
            } else if (BooleanUtils.isTrue(missMap.get(new Point(0, 0))) && resRef.defaultRenderBgResource == null) {
                resRef.defaultRenderBgResource = decorate;
            }
        }
    }


    /**
     * 资源集合
     */
    public static class QrResourceSet {
        private static final Random RAND = new Random();

        private final List<QrCountResource> resourceList;

        public boolean empty() {
            return resourceList.isEmpty();
        }

        public QrResourceSet(QrResource resource) {
            this(resource, NO_LIMIT_COUNT);
        }

        public QrResourceSet(QrResource resource, int cnt) {
            resourceList = new ArrayList<>();
            resourceList.add(new QrCountResource(resource, cnt));
        }

        public void addResource(QrResource img, int cnt) {
            resourceList.add(new QrCountResource(img, cnt));
        }

        public QrResource getResource() {
            if (resourceList.size() == 0) {
                return null;
            }

            int index = RAND.nextInt(resourceList.size());
            QrCountResource resource = resourceList.get(index);
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

        public List<QrCountResource> getResourceList() {
            return resourceList;
        }
    }

    /**
     * 待计数的资源
     */
    public static class QrCountResource {
        /**
         * 绘制图
         */
        QrResource resource;
        /**
         * -1表示不限次数， >1 表示最多出现的次数
         */
        int count;

        public QrCountResource(QrResource img, int count) {
            this.resource = img;
            this.count = count;
        }
    }
}
