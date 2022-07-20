package com.github.hui.quick.plugin.md.helper;

import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.md.entity.HtmlRenderOptions;
import org.xhtmlrenderer.simple.Graphics2DRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/9/11.
 */
public class HtmlRenderHelper {

    /**
     * 输出图片
     *
     * @param options
     * @return
     */
    public static BufferedImage parseImage(HtmlRenderOptions options) {
        int width = options.getW();
        int height = options.getH() == null ? 1024 : options.getH();
        Graphics2DRenderer renderer = new Graphics2DRenderer();
        renderer.setDocument(options.getDocument(), options.getDocument().getDocumentURI());


        Dimension dimension = new Dimension(width, height);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = GraphicUtil.getG2d(bufferedImage);


        if (options.isAutoH() || options.getH() == null) {
            // do layout with temp buffer
            renderer.layout(graphics2D, new Dimension(width, height));
            graphics2D.dispose();

            Rectangle size = renderer.getMinimumSize();
            final int autoWidth = options.isAutoW() ? (int) size.getWidth() : width;
            final int autoHeight = (int) size.getHeight();
            bufferedImage = new BufferedImage(autoWidth, autoHeight, BufferedImage.TYPE_INT_RGB);
            dimension = new Dimension(autoWidth, autoHeight);

            graphics2D = GraphicUtil.getG2d(bufferedImage);
        }


        renderer.layout(graphics2D, dimension);
        renderer.render(graphics2D);
        graphics2D.dispose();
        return bufferedImage;
    }
}
