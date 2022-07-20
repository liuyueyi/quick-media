package com.github.hui.quick.plugin.qrcode.v3.entity;

import com.github.hui.quick.plugin.base.gif.GifDecoder;

import java.awt.image.BufferedImage;

/**
 * @author YiHui
 * @date 2022/7/20
 */
public class QrResource {
    /**
     * 动态背景图
     */
    private GifDecoder gifDecoder;

    /**
     * 背景图
     */
    private BufferedImage img;

    /**
     * 文字
     */
    private String text;

    /**
     * svg tag
     */
    private String svg;
}
