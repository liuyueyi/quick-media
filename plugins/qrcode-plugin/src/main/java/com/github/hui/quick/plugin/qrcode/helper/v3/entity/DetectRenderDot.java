package com.github.hui.quick.plugin.qrcode.helper.v3.entity;

import com.github.hui.quick.plugin.qrcode.helper.QrCodeRenderHelper;
/**
 * 二维矩阵渲染
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class DetectRenderDot<T> extends RenderDot<T> {
    private QrCodeRenderHelper.DetectLocation location;
    private int size;
    /**
     * 当size == 1时，用这个来区分当前这个是在码眼外层的还是内层的框上
     */
    private Boolean outBorder;

    public DetectRenderDot() {
        this.type = 0;
    }

    public QrCodeRenderHelper.DetectLocation getLocation() {
        return location;
    }

    public DetectRenderDot<T> setLocation(QrCodeRenderHelper.DetectLocation location) {
        this.location = location;
        return this;
    }

    public int getSize() {
        return size;
    }

    public DetectRenderDot<T> setSize(int size) {
        this.size = size;
        return this;
    }

    public Boolean getOutBorder() {
        return outBorder;
    }

    public DetectRenderDot<T> setOutBorder(Boolean outBorder) {
        this.outBorder = outBorder;
        return this;
    }
}