package com.github.hui.quick.plugin.qrcode.v3.constants;

/**
 * 渲染资源的类型
 *
 * @author YiHui
 * @date 2022/8/2
 */
public enum RenderDotType {
    DETECT(0) ,
    BG(1) ,
    PRE(2) ,
    ;

    private int type;

    RenderDotType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
