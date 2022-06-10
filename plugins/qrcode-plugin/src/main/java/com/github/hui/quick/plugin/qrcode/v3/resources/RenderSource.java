package com.github.hui.quick.plugin.qrcode.v3.resources;

import com.github.hui.quick.plugin.qrcode.v3.canvas.QrCanvas;

/**
 * @author
 * @date 2022/6/10
 */
public interface RenderSource<T> {
    /**
     * 资源渲染
     *
     * @param canvas
     * @param x
     * @param y
     * @param w
     * @param h
     */
    <K> void render(QrCanvas canvas, int x, int y, int w, int h);

    /**
     * 获取资源
     *
     * @return
     */
    T getSource();

};
