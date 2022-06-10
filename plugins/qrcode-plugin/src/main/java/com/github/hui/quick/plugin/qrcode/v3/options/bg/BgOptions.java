package com.github.hui.quick.plugin.qrcode.v3.options.bg;

import com.github.hui.quick.plugin.qrcode.v3.resources.RenderSource;

/**
 * the render options for background of qrcode
 *
 * @author yihui
 * @date 2022/6/10
 */
public class BgOptions {
    /**
     * background width
     */
    private int bgW;
    /**
     * background height
     */
    private int bgH;

    /**
     * background style
     */
    private BgStyle bgStyle;

    /**
     * background resource to render qrcode
     */
    private RenderSource source;
}
