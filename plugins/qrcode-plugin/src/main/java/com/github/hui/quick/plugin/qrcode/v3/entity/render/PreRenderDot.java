package com.github.hui.quick.plugin.qrcode.v3.entity.render;

/**
 * 二维矩阵渲染
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class PreRenderDot extends RenderDot {
    private int row, col;

    public PreRenderDot() {
        this.type = 2;
    }

    public int getRow() {
        return row;
    }

    public PreRenderDot setRow(int row) {
        this.row = row;
        return this;
    }

    public int getCol() {
        return col;
    }

    public PreRenderDot setCol(int col) {
        this.col = col;
        return this;
    }
}