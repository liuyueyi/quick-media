package com.hust.hui.quickmedia.common.img.merge;

import com.hust.hui.quickmedia.common.img.merge.cell.IMergeCell;
import com.hust.hui.quickmedia.common.util.GraphicUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by yihui on 2017/10/13.
 */
public class ImgMergeWrapper {

    public static BufferedImage merge(List<IMergeCell> list, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = GraphicUtil.getG2d(img);
        list.forEach(cell -> cell.draw(g2d));
        return img;
    }

}
