package com.github.hui.quick.plugin.qrcode.wrapper;

import com.google.zxing.qrcode.encoder.ByteMatrix;

/**
 * 扩展的二维码矩阵信息， 主要新增了三个位置探测图形的判定
 * <p>
 * Created by yihui on 2017/7/27.
 */
public class BitMatrixEx {
    /**
     * 实际生成二维码的宽
     */
    private int width;


    /**
     * 实际生成二维码的高
     */
    private int height;


    /**
     * 左白边大小
     */
    private int leftPadding;

    /**
     * 上白边大小
     */
    private int topPadding;

    /**
     * 矩阵信息缩放比例
     */
    private int multiple;

    private ByteMatrix byteMatrix;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public ByteMatrix getByteMatrix() {
        return byteMatrix;
    }

    public void setByteMatrix(ByteMatrix byteMatrix) {
        this.byteMatrix = byteMatrix;
    }
}
