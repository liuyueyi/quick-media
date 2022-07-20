package com.github.hui.quick.plugin.qrcode.helper.v3.entity;
/**
 * 二维矩阵渲染
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class BgRenderDot<T> extends RenderDot<T> {
    private int row, col;

    public BgRenderDot() {
        this.type = 1;
    }

    public int getRow() {
        return row;
    }

    public BgRenderDot<T> setRow(int row) {
        this.row = row;
        return this;
    }

    public int getCol() {
        return col;
    }

    public BgRenderDot<T> setCol(int col) {
        this.col = col;
        return this;
    }
}