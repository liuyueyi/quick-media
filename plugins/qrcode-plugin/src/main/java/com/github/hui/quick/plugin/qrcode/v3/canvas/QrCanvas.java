package com.github.hui.quick.plugin.qrcode.v3.canvas;

import com.github.hui.quick.plugin.qrcode.v3.resources.RenderSource;

/**
 * qrcode绘版
 *
 * @author yihui
 * @date 2022/6/10
 */
public interface QrCanvas {
    /**
     * 开始绘制
     *
     * @param resource
     * @param x
     * @param y
     * @param w
     * @param h
     */
    void draw(RenderSource<?> resource, int x, int y, int w, int h);

    <T> T output();
}
