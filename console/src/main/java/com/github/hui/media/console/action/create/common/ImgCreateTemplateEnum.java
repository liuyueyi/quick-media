package com.github.hui.media.console.action.create.common;


import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 2017/9/18.
 */
public enum ImgCreateTemplateEnum {
    /**
     * 水平文字，上下布局， 左对齐，图片放最后
     */
    HORIZONTAL_LEFT("hl", ImgCreateOptions.DrawStyle.HORIZONTAL, ImgCreateOptions.AlignStyle.LEFT, 1),
    HORIZONTAL_CENTER("hc", ImgCreateOptions.DrawStyle.HORIZONTAL, ImgCreateOptions.AlignStyle.CENTER, 1),
    HORIZONTAL_RIGHT("hr", ImgCreateOptions.DrawStyle.HORIZONTAL, ImgCreateOptions.AlignStyle.RIGHT, 1),


    /**
     * 水平文字，上下布局， 左对齐， 图片放在最前面
     */
    HORIZONTAL_FIRST_IMG_LEFT("hfil", ImgCreateOptions.DrawStyle.HORIZONTAL, ImgCreateOptions.AlignStyle.LEFT, 0),
    HORIZONTAL_FIRST_IMG_CENTER("hfic", ImgCreateOptions.DrawStyle.HORIZONTAL, ImgCreateOptions.AlignStyle.CENTER, 0),
    HORIZONTAL_FIRST_IMG_RIGHT("hfir", ImgCreateOptions.DrawStyle.HORIZONTAL, ImgCreateOptions.AlignStyle.RIGHT, 0),


    /**
     * 垂直文字，从左到右，上对其，图片放最后
     */
    VERTICAL_LEFT_TOP("vlt", ImgCreateOptions.DrawStyle.VERTICAL_LEFT, ImgCreateOptions.AlignStyle.TOP, 1),
    VERTICAL_LEFT_CENTER("vlc", ImgCreateOptions.DrawStyle.VERTICAL_LEFT, ImgCreateOptions.AlignStyle.CENTER, 1),
    VERTICAL_LEFT_BOTTOM("vlb", ImgCreateOptions.DrawStyle.VERTICAL_LEFT, ImgCreateOptions.AlignStyle.BOTTOM, 1),


    /**
     * 垂直文字，从左到右，上对其，图片放最前
     */
    VERTICAL_LEFT_IMG_FIRST_TOP("vlift", ImgCreateOptions.DrawStyle.VERTICAL_LEFT, ImgCreateOptions.AlignStyle.TOP, 0),
    VERTICAL_LEFT_IMG_FIRST_CENTER("vlifc", ImgCreateOptions.DrawStyle.VERTICAL_LEFT, ImgCreateOptions.AlignStyle.CENTER, 0),
    VERTICAL_LEFT_IMG_FIRST_BOTTOM("vlifb", ImgCreateOptions.DrawStyle.VERTICAL_LEFT, ImgCreateOptions.AlignStyle.BOTTOM, 0),


    /**
     * 垂直文字， 从右到左，上对其， 图片放最后
     */
    VERTICAL_RIGHT_TOP("vrt", ImgCreateOptions.DrawStyle.VERTICAL_RIGHT, ImgCreateOptions.AlignStyle.TOP, 1),
    VERTICAL_RIGHT_CENTER("vrc", ImgCreateOptions.DrawStyle.VERTICAL_RIGHT, ImgCreateOptions.AlignStyle.CENTER, 1),
    VERTICAL_RIGHT_BOTTOM("vrb", ImgCreateOptions.DrawStyle.VERTICAL_RIGHT, ImgCreateOptions.AlignStyle.BOTTOM, 1),



    /**
     * 垂直文字， 从右到左，上对其， 图片放最前
     */
    VERTICAL_RIGHT_IMG_FIRST_TOP("vrift", ImgCreateOptions.DrawStyle.VERTICAL_RIGHT, ImgCreateOptions.AlignStyle.TOP, 0),
    VERTICAL_RIGHT_IMG_FIRST_CENTER("vrifc", ImgCreateOptions.DrawStyle.VERTICAL_RIGHT, ImgCreateOptions.AlignStyle.CENTER, 0),
    VERTICAL_RIGHT_IMG_FIRST_BOTTOM("vrifb", ImgCreateOptions.DrawStyle.VERTICAL_RIGHT, ImgCreateOptions.AlignStyle.BOTTOM, 0),
    ;

    private String id;

    private ImgCreateOptions.DrawStyle drawStyle;

    private ImgCreateOptions.AlignStyle alignStyle;

    // 0 表示在前面， 1表示在最后
    private int imgPos;

    ImgCreateTemplateEnum(String id, ImgCreateOptions.DrawStyle drawStyle,
                            ImgCreateOptions.AlignStyle alignStyle,
                            int imgPos) {
        this.id = id;
        this.drawStyle = drawStyle;
        this.alignStyle = alignStyle;
        this.imgPos = imgPos;
    }


    private static Map<String, ImgCreateTemplateEnum> map = new HashMap<>();
    static {
        for(ImgCreateTemplateEnum e: values()) {
            map.put(e.id, e);
        }
    }

    public static ImgCreateTemplateEnum getEnum(String id) {
        return map.get(id);
    }


    public ImgCreateOptions.DrawStyle getDrawStyle() {
       return drawStyle;
    }


    public ImgCreateOptions.AlignStyle getAlignStyle() {
        return alignStyle;
    }

    public String getId() {
        return id;
    }

    public boolean imgEnd() {
        return imgPos == 1;
    }
}
