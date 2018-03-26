package com.github.hui.quick.plugin.image.wrapper.wartermark;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 2017/9/28.
 */
@Data
public class WaterMarkOptions {


    private BufferedImage source;

    private BufferedImage water;

    private WaterStyle style;

    private int x;

    private int y;

    private float opacity;

    private int basicW ;


    public enum WaterStyle {
        /**
         * 背景填充方式
         */
        FILL_BG,


        /**
         * 在指定的位置直接绘制
         */

        // 左上角
        OVERRIDE_LEFT_TOP,
        // 上居中
        OVERRIDE_TOP_CENTER,
        // 右上角
        OVERRIDE_RIGHT_TOP,

        // 左中
        OVERRIDE_LEFT_CENTER,
        // 正中
        OVERRIDE_CENTER,
        // 右中
        OVERRIDE_RIGHT_CENTER,

        // 左下
        OVERRIDE_LEFT_BOTTOM,
        // 下中
        OVERRIDE_BOTTOM_CENTER,
        // 右下
        OVERRIDE_RIGHT_BOTTOM,
        ;


        private static Map<String, WaterStyle> map = new HashMap<>();
        static {
            for(WaterStyle style: values()) {
                map.put(style.name(), style);
            }
        }



        public static WaterStyle getStyle(String style) {
            if(style == null) {
                return OVERRIDE_RIGHT_BOTTOM;
            }

            WaterStyle ws = map.get(style.toUpperCase());
            return ws == null ? OVERRIDE_RIGHT_BOTTOM : ws;
        }
    }

}
