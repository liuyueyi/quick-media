package com.github.hui.quick.plugin.qrcode.v3.constants;

/**
 * 背景图样式
 *
 * @author yihui
 * @date 22/7/20
 */
public enum BgStyle {
    /**
     * 设置二维码透明度，然后全覆盖背景图；
     * 通常这种适用于二维码与背景大小相同的场景
     * 当二维码大小与背景图大小不一致时，默认放在居中位置；
     */
    OVERRIDE,

    /**
     * 将二维码填充在背景图的指定位置
     * 通常这种适用于将二维码嵌套在其他的一张宣传图中
     */
    FILL,


    /**
     * 背景图穿透显示, 即二维码主题色为透明，由背景图的颜色进行填充，因此当背景图时渐变色彩时，我们可以得到一个渐变的二维码
     */
    PENETRATE,
    ;


    public static BgStyle getStyle(String name) {
        return BgStyle.valueOf(name.toUpperCase());
    }
}
