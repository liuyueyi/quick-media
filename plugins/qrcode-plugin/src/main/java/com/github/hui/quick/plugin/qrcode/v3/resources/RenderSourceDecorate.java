package com.github.hui.quick.plugin.qrcode.v3.resources;

import com.github.hui.quick.plugin.qrcode.v3.canvas.QrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.options.qr.QrDetectPosition;
import com.github.hui.quick.plugin.qrcode.v3.options.source.DetectSourceOptions;
import com.github.hui.quick.plugin.qrcode.v3.options.source.SourceOptions;
import com.github.hui.quick.plugin.qrcode.v3.resources.impl.detect.DetectRenderSource;
import com.google.zxing.qrcode.encoder.ByteMatrix;

/**
 * 资源装饰类
 *
 * @author
 * @date 2022/6/11
 */
public class RenderSourceDecorate<T> {
    private RenderSource<T> renderSource;

    public static <T> RenderSourceDecorate<T> create(RenderSource<T> source) {
        return new RenderSourceDecorate<T>(source);
    }

    private RenderSourceDecorate(RenderSource<T> renderSource) {
        this.renderSource = renderSource;
    }

    public boolean match(ByteMatrix byteMatrix, int startX, int startY) {
        SourceOptions sourceOptions = renderSource.getSourceOptions();
        for (int x = 0; x < sourceOptions.getCol(); x++) {
            for (int y = 0; y < sourceOptions.getRow(); y++) {
                if (!sourceOptions.miss(x, y) && byteMatrix.get(startX + x, startY + y) == 0) {
                    return false;
                }

                if (sourceOptions.isFullMatch()) {
                    // 全匹配时，则要求资源图是空格的地方，二维码矩阵也是空白的
                    // 非全匹配时，只要求资源图有的地方，二维码矩阵也有
                    if (sourceOptions.miss(x, y) && byteMatrix.get(startX + x, startY + y) == 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public void renderQrInfo(ByteMatrix byteMatrix, QrCanvas canvas, int leftPadding, int topPadding, int x, int y, int infoSize) {
        SourceOptions sourceOptions = renderSource.getSourceOptions();
        int startX = leftPadding + x * infoSize;
        int startY = topPadding + y * infoSize;
        renderSource.render(canvas, startX, startY, sourceOptions.getCol() * infoSize, sourceOptions.getRow() * infoSize);
        // 将命中的标记为已渲染
        for (int col = 0; col < sourceOptions.getCol(); col++) {
            for (int row = 0; row < sourceOptions.getRow(); row++) {
                if (byteMatrix.get(x, y) == 1) {
                    byteMatrix.set(x + col, y + row, 0);
                }
            }
        }
    }

    /**
     * 渲染探测图形
     *
     * @param byteMatrix
     * @param canvas
     * @param position
     * @param leftPadding
     * @param topPadding
     * @param infoSize
     */
    public void renderDetect(ByteMatrix byteMatrix, QrCanvas canvas, QrDetectPosition position, int leftPadding, int topPadding, int infoSize) {
        SourceOptions sourceOptions = renderSource.getSourceOptions();
        int detectSize = QrDetectPosition.detectSize(byteMatrix);
        if (sourceOptions.getCol() == detectSize || sourceOptions.getRow() == detectSize) {
            // 一个资源，渲染整个码眼
            int x = leftPadding + position.startX(byteMatrix);
            int y = topPadding + position.startY(byteMatrix);
            renderSource.render(canvas, x, y, detectSize * infoSize, detectSize * infoSize);
            return;
        }

        int startX = position.startX(byteMatrix), endX = position.endX(byteMatrix);
        int startY = position.startY(byteMatrix), endY = position.endY(byteMatrix);
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                DetectSourceOptions.DetectSourceArea area;
                if (x == startX || y == startY || x == endX || y == endY) {
                    area = DetectSourceOptions.DetectSourceArea.OUT;
                } else if (byteMatrix.get(x, y) == 1) {
                    area = DetectSourceOptions.DetectSourceArea.IN;
                } else {
                    area = DetectSourceOptions.DetectSourceArea.BG;
                }
                ((DetectRenderSource) renderSource).render(canvas, area, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize);
            }
        }
    }
}
