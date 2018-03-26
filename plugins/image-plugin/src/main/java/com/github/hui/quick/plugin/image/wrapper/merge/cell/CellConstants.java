package com.github.hui.quick.plugin.image.wrapper.merge.cell;

import java.awt.*;

/**
 * Created by yihui on 2017/10/13.
 */
public interface CellConstants {

    /**
     * 默认虚线样式
     */
    Stroke LINE_DEFAULT_STROKE = new BasicStroke(2,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
            3.5f,
            new float[]{12, 6, 6, 6},
            0f);


    /**
     * 默认矩形虚线样式
     */
    Stroke RECT_DEFAULT_DASH = new BasicStroke(2,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
            3.5f,
            new float[]{4, 2,},
            0f);
}
