package com.github.hui.quick.plugin.image.wrapper.merge;


import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.image.wrapper.merge.cell.IMergeCell;
import com.github.hui.quick.plugin.image.wrapper.merge.cell.RectFillCell;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihui on 2017/10/13.
 */
public class ImgMergeWrapper {

    public static BufferedImage merge(List<IMergeCell> list, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = GraphicUtil.getG2d(img);
        list.forEach(cell -> cell.draw(g2d));
        return img;
    }


    public static BufferedImage merge(List<IMergeCell> list, int w, int h, Color bgColor) {
        RectFillCell rectFillCell = RectFillCell.builder()
                .x(0)
                .y(0)
                .w(w)
                .h(h)
                .color(bgColor)
                .build();

        List<IMergeCell> l = new ArrayList<>(1 + list.size());
        l.add(rectFillCell);
        l.addAll(list);
        return merge(l, w, h);
    }

}
