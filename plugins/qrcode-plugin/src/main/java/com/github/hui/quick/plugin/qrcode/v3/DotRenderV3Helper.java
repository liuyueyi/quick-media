package com.github.hui.quick.plugin.qrcode.v3;

import com.github.hui.quick.plugin.qrcode.v3.entity.render.PreRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.RenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.RenderResourcesV3;
import com.google.zxing.qrcode.encoder.ByteMatrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * v2版图片渲染辅助类，根据优先级从从最高的网下进行绘制
 *
 * @author yihui
 * @date 2021/7/28
 */
public class DotRenderV3Helper {
    public static  List<RenderDot> renderResources(ByteMatrix matrix, RenderResourcesV3 imgResources) {
        List<RenderDot> result = new ArrayList<>();
        for (RenderResourcesV3.RenderSource renderSource : imgResources.getSourceList()) {
            result.addAll(renderSpecialResource(matrix, renderSource, renderSource.isFullMatch()));
        }

        // 最后走一个兜底的1x1的渲染
        for (int x = 0; x < matrix.getWidth(); x++) {
            for (int y = 0; y < matrix.getHeight(); y++) {
                if (matrix.get(x, y) == 1) {
                    result.add(new PreRenderDot().setRow(1).setCol(1).setX(x).setY(y).setResource(imgResources.getDefaultDrawImg()));
                }
            }
        }
        return result;
    }

    private static  List<RenderDot> renderSpecialResource(ByteMatrix matrix, RenderResourcesV3.RenderSource renderSource, boolean fullMatch) {
        if (renderSource.countOver()) {
            return Collections.emptyList();
        }
        List<RenderDot> result = new ArrayList<>();
        for (int x = 0; x < matrix.getWidth() - renderSource.getCol() + 1; x++) {
            for (int y = 0; y < matrix.getHeight() - renderSource.getRow() + 1; y++) {
                if (match(matrix, renderSource, x, y, fullMatch)) {
                    result.add(renderResource(matrix, renderSource, x, y));
                    if (renderSource.countOver()) {
                        return result;
                    }
                }
            }
        }
        return result;
    }

    private static boolean match(ByteMatrix matrix, RenderResourcesV3.RenderSource renderSource, int startX, int startY, boolean fullMatch) {
        // 要求矩阵的1点图，能完全覆盖 renderSource
        for (int x = 0; x < renderSource.getCol(); x++) {
            for (int y = 0; y < renderSource.getRow(); y++) {
                if (!renderSource.miss(x, y) && matrix.get(startX + x, startY + y) == 0) {
                    // 资源图存在，但是矩阵点不存在，则表示未满足条件，直接退出
                    return false;
                }

                if (fullMatch) {
                    // 全匹配时，则要求资源图是空格的地方，二维码矩阵也是空白的
                    // 非全匹配时，只要求资源图有的地方，二维码矩阵也有
                    if (renderSource.miss(x, y) && matrix.get(startX + x, startY + y) == 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static  RenderDot renderResource(ByteMatrix matrix, RenderResourcesV3.RenderSource renderSource, int x, int y) {
        PreRenderDot renderDot = new PreRenderDot();
        renderDot.setCol(renderSource.getCol()).setRow(renderSource.getRow()).setX(x).setY(y).setResource(renderSource.getResource());

        // 将命中的标记为已渲染
        for (int col = 0; col < renderSource.getCol(); col++) {
            for (int row = 0; row < renderSource.getRow(); row++) {
                if (!renderSource.miss(col, row)) {
                    matrix.set(x + col, y + row, 0);
                }
            }
        }
        return renderDot;
    }
}
