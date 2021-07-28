package com.github.hui.quick.plugin.qrcode.helper.v2;

import com.github.hui.quick.plugin.qrcode.entity.RenderImgResources;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.qrcode.encoder.ByteMatrix;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * v2版图片渲染辅助类，根据优先级从从最高的网下进行绘制
 *
 * @author yihui
 * @date 2021/7/28
 */
public class ImgRenderV2Helper {
    public static void drawImg(Graphics2D g2d, ByteMatrix matrix, RenderImgResources imgResources, int leftPadding, int topPadding, int infoSize) {
        for (RenderImgResources.RenderSource renderSource : imgResources.getSourceList()) {
            renderSpecialResource(g2d, matrix, renderSource, leftPadding, topPadding, infoSize);
        }

        // 最后走一个兜底的1x1的渲染
        for (int x = 0; x < matrix.getWidth(); x++) {
            for (int y = 0; y < matrix.getHeight(); y++) {
                if (matrix.get(x, y) == 1) {
                    QrCodeOptions.DrawStyle.IMAGE_V2.draw(g2d, leftPadding + x * infoSize,
                            topPadding + y * infoSize, infoSize, infoSize,
                            imgResources.getDefaultImg(), null);
                }
            }
        }
    }

    private static void renderSpecialResource(Graphics2D g2d, ByteMatrix matrix, RenderImgResources.RenderSource renderSource,
                                              int leftPadding, int topPadding, int infoSize) {
        if (renderSource.countOver()) return;
        for (int x = 0; x < matrix.getWidth(); x++) {
            for (int y = 0; y < matrix.getHeight(); y++) {
                if (renderSource.countOver()) {
                    return;
                }

                if (!match(matrix, renderSource, x, y)) {
                    renderImg(g2d, matrix, renderSource, x, y, leftPadding, topPadding, infoSize);
                }
            }
        }
    }

    private static boolean match(ByteMatrix matrix, RenderImgResources.RenderSource renderSource, int startX, int startY) {
        // 要求矩阵的1点图，能完全覆盖 renderSource
        for (int x = 0; x < renderSource.getCol(); x++) {
            for (int y = 0; y < renderSource.getRow(); y++) {
                if (!renderSource.miss(x, y) && matrix.get(startX + x, startY + y) == 0) {
                    // 资源图存在，但是矩阵点不存在，则表示未满足条件，直接退出
                    return false;
                }
            }
        }
        return true;
    }

    private static void renderImg(Graphics2D g2d, ByteMatrix matrix, RenderImgResources.RenderSource renderSource, int x, int y,
                                  int leftPadding, int topPadding, int infoSize) {
        renderSource.autoUpdateCount();
        BufferedImage img = renderSource.getImg();
        // 开始绘制，并将已经绘制过得地方置空
        QrCodeOptions.DrawStyle.IMAGE_V2.draw(g2d, leftPadding + x * infoSize,
                topPadding + y * infoSize,
                renderSource.getCol() * infoSize,
                renderSource.getRow() * infoSize,
                img, null);

        // 将命中的标记为已渲染
        for (int col = 0; col < renderSource.getCol(); col++) {
            for (int row = 0; row < renderSource.getRow(); row++) {
                if (!renderSource.miss(col, row)) {
                    matrix.set(x + col, y + row, 0);
                }
            }
        }
    }
}
