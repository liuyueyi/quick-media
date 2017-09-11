package com.hust.hui.quickmedia.common.html;

import lombok.Data;
import org.w3c.dom.Document;

import java.awt.*;

/**
 * Created by yihui on 2017/9/11.
 */
@Data
public class HtmlRenderOptions {

    /**
     * 输出图片的宽
     */
    private Integer w;


    /**
     * 输出图片的高
     */
    private Integer h;


    /**
     * 是否自适应宽
     */
    private boolean autoW;


    /**
     * 是否自适应高
     */
    private boolean autoH;


    /**
     * 输出图片的格式
     */
    private String outType;

    /**
     * html相关内容
     */
    private Document document;


    /**
     * 字体
     */
    private Font font;


    /**
     * 绘制字体颜色
     */
    private String fontColor;
}
