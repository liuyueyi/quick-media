package com.github.hui.quick.plugin.qrcode.v3.options.source;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源配置
 *
 * @author
 * @date 2022/6/10
 */
public class SourceOptions {
    private Color color;

    private Font font;

    private Integer fontSize;

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
     * 特殊case，若missMap为空，则表示完整填充，不存在缺失的场景
     */
    private Map<Point, Boolean> missMap = new HashMap<>();

    /**
     * true 表示全匹配，即有图和空白的地方都需要完全匹配
     * false 则表示只要有图的地方能满足即可
     * 如：上面的3*3，十字有图
     * 当fullMatch = true, 现在有一个 3x3 全是1的区域，无法匹配
     * 当fullMatch = false, 则可以匹配
     */
    private boolean fullMatch;

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

    public SourceOptions() {
        row = 1;
        col = 1;
        order = 0;
        fullMatch = true;
    }

    public Color getColor() {
        return color;
    }

    public SourceOptions setColor(Color color) {
        this.color = color;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public SourceOptions setFont(Font font) {
        this.font = font;
        return this;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public SourceOptions setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public Map<Point, Boolean> getMissMap() {
        return missMap;
    }

    public SourceOptions setMissMap(Map<Point, Boolean> missMap) {
        this.missMap = missMap;
        return this;
    }

    public boolean isFullMatch() {
        return fullMatch;
    }

    public SourceOptions setFullMatch(boolean fullMatch) {
        this.fullMatch = fullMatch;
        return this;
    }

    public int getRow() {
        return row;
    }

    public SourceOptions setRow(int row) {
        this.row = row;
        return this;
    }

    public int getCol() {
        return col;
    }

    public SourceOptions setCol(int col) {
        this.col = col;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public SourceOptions setOrder(int order) {
        this.order = order;
        return this;
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

}
