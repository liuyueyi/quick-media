package com.github.hui.quick.plugin.qrcode.helper.v3.entity.render;

import com.github.hui.quick.plugin.qrcode.helper.v3.entity.QrResource;

/**
 * 二维矩阵渲染
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class RenderDot {
    protected int x, y;
    /**
     * 资源类型 0: 探测图形  1: 背景点  2: 信息点
     */
    protected int type;

    private QrResource resource;

    public int getX() {
        return x;
    }

    public RenderDot setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public RenderDot setY(int y) {
        this.y = y;
        return this;
    }

    public int getType() {
        return type;
    }

    public QrResource getResource() {
        return resource;
    }

    public RenderDot setResource(QrResource resource) {
        this.resource = resource;
        return this;
    }
}