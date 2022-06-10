package com.github.hui.quick.plugin.qrcode.v3.options.bg;

/**
 * @author
 * @date 2022/6/10
 */
public enum ImgBgStyle implements BgStyle {
    /**
     * 设置二维码透明度，然后全覆盖背景图
     */
    OVERRIDE,

    /**
     * 将二维码填充在背景图的指定位置
     */
    FILL,


    /**
     * 背景图穿透显示, 即二维码主题色为透明，由背景图的颜色进行填充
     */
    PENETRATE,
    ;
}
