package com.github.hui.quick.plugin.qrcode.v3.core.calculate;

import com.github.hui.quick.plugin.qrcode.helper.QrCodeRenderHelper;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.entity.RenderResourcesV3;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.BgRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.DetectRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.PreRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.RenderDot;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * 二维矩阵渲染
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class QrRenderDotGenerator {
    /**
     * 二维数组遍历
     *
     * @param w
     * @param h
     * @param consumer
     * @param <T>
     */
    public static <T> void scanMatrix(int w, int h, BiConsumer<Integer, Integer> consumer) {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                consumer.accept(x, y);
            }
        }
    }

    /**
     * 计算渲染资源列表
     *
     * @param qrCodeConfig
     * @param bitMatrix
     * @return
     */
    public static List<RenderDot> calculateRenderDots(QrCodeV3Options qrCodeConfig, BitMatrixEx bitMatrix) {
        // 探测图形的大小
        int detectCornerSize = bitMatrix.getByteMatrix().get(0, 5) == 1 ? 7 : 5;

        int matrixW = bitMatrix.getByteMatrix().getWidth();
        int matrixH = bitMatrix.getByteMatrix().getHeight();

        List<RenderDot> result = new ArrayList<>();
        // 若探测图形特殊绘制，则提前处理掉
        RenderResourcesV3 imgResourcesV3 = qrCodeConfig.getDrawOptions().getRenderResourcesV3();
        scanMatrix(matrixW, matrixH, (x, y) -> {
            QrCodeRenderHelper.DetectLocation detectLocation = inDetectCornerArea(x, y, matrixW, matrixH, detectCornerSize);
            if (detectLocation.detectedArea() && qrCodeConfig.getDetectOptions().getSpecial()) {
                // 若探测图形特殊绘制，则单独处理
                if (bitMatrix.getByteMatrix().get(x, y) == 1) {
                    // 绘制三个位置探测图形
                    result.add(drawDetectInfo(qrCodeConfig, bitMatrix, detectCornerSize, x, y, detectLocation));
                }
            } else {
                // 非探测区域内的0点图渲染
                drawImgBgInfo(bitMatrix, x, y, imgResourcesV3).ifPresent(result::add);
            }
        });
        result.addAll(drawQrInfo(bitMatrix, imgResourcesV3));
        return result;
    }

    /**
     * 当背景点使用指定图片渲染时进行处理
     *
     * @param bitMatrix
     * @param x
     * @param y
     * @param imgResourcesV3
     * @return
     */
    private static Optional<RenderDot> drawImgBgInfo(BitMatrixEx bitMatrix, int x, int y, RenderResourcesV3 imgResourcesV3) {
        if (bitMatrix.getByteMatrix().get(x, y) == 0 && imgResourcesV3.getDefaultBgImg() != null) {
            // 非探测区域内的0点图渲染
            return Optional.of(new BgRenderDot()
                    .setRow(1)
                    .setCol(1)
                    .setX(bitMatrix.getLeftPadding() + x * bitMatrix.getMultiple())
                    .setY(bitMatrix.getTopPadding() + y * bitMatrix.getMultiple())
                    .setSize(bitMatrix.getMultiple())
                    .setResource(imgResourcesV3.getDefaultBgImg()));
        }
        return Optional.empty();
    }

    /**
     * 判断 (x,y) 对应的点是否处于二维码矩阵的探测图形内
     *
     * @param x                目标点的x坐标
     * @param y                目标点的y坐标
     * @param matrixW          二维码矩阵宽
     * @param matrixH          二维码矩阵高
     * @param detectCornerSize 探测图形的大小
     * @return
     */
    private static QrCodeRenderHelper.DetectLocation inDetectCornerArea(int x, int y, int matrixW, int matrixH, int detectCornerSize) {
        if (x < detectCornerSize && y < detectCornerSize) {
            // 左上角
            return QrCodeRenderHelper.DetectLocation.LT;
        }

        if (x < detectCornerSize && y >= matrixH - detectCornerSize) {
            // 左下角
            return QrCodeRenderHelper.DetectLocation.LD;
        }

        if (x >= matrixW - detectCornerSize && y < detectCornerSize) {
            // 右上角
            return QrCodeRenderHelper.DetectLocation.RT;
        }

        return QrCodeRenderHelper.DetectLocation.NONE;
    }

    /**
     * 绘制探测图形
     *
     * @param qrCodeConfig     绘制参数
     * @param bitMatrix        二维码矩阵
     * @param detectCornerSize 探测图形大小
     * @param x                目标点x坐标
     * @param y                目标点y坐标
     */
    private static RenderDot drawDetectInfo(QrCodeV3Options qrCodeConfig, BitMatrixEx bitMatrix, int detectCornerSize, int x, int y, QrCodeRenderHelper.DetectLocation detectLocation) {
        int matrixW = bitMatrix.getByteMatrix().getWidth();
        int matrixH = bitMatrix.getByteMatrix().getHeight();

        DetectRenderDot renderDot = new DetectRenderDot();
        renderDot.setLocation(detectLocation)
                .setX(bitMatrix.getLeftPadding() + x * bitMatrix.getMultiple())
                .setY(bitMatrix.getTopPadding() + y + bitMatrix.getMultiple())
                .setSize(bitMatrix.getMultiple());

        QrResource detectResource = qrCodeConfig.getDetectOptions().chooseDetectResource(detectLocation);
        if (detectResource != null && BooleanUtils.isTrue(qrCodeConfig.getDetectOptions().getWhole())) {
            // 图片直接渲染完毕之后，将其他探测图形的点设置为0，表示不需要再次渲染
            scanMatrix(detectCornerSize, detectCornerSize, (addX, addY) -> bitMatrix.getByteMatrix().set(x + addX, y + addY, 0));
            // 码眼整个用一张资源渲染
            renderDot.setOutBorder(false).setDotNum(detectCornerSize);
            return renderDot;
        }

        RenderResourcesV3 renderResourcesV3 = qrCodeConfig.getDrawOptions().getRenderResourcesV3();
        renderDot.setDotNum(1).setOutBorder(inOuterDetectCornerArea(x, y, matrixW, matrixH, detectCornerSize)).setResource(renderResourcesV3.getDefaultBgImg());
        bitMatrix.getByteMatrix().set(x, y, 0);
        return renderDot;
    }

    /**
     * 判断 (x,y) 对应的点是否为二维码举证探测图形中外面的框, 这个方法的调用必须在确认(x,y)对应的点在探测图形内
     *
     * @param x                目标点的x坐标
     * @param y                目标点的y坐标
     * @param matrixW          二维码矩阵宽
     * @param matrixH          二维码矩阵高
     * @param detectCornerSize 探测图形的大小
     * @return
     */
    private static boolean inOuterDetectCornerArea(int x, int y, int matrixW, int matrixH, int detectCornerSize) {
        // 外层的框
        return x == 0 || x == detectCornerSize - 1 || x == matrixW - 1 || x == matrixW - detectCornerSize || y == 0 || y == detectCornerSize - 1 || y == matrixH - 1 || y == matrixH - detectCornerSize;
    }

    /**
     * 绘制二维码信息
     *
     * @param matrixEx
     * @param imgResources
     * @return
     */
    private static List<RenderDot> drawQrInfo(BitMatrixEx matrixEx, RenderResourcesV3 imgResources) {
        ByteMatrix matrix = matrixEx.getByteMatrix();
        List<RenderDot> result = new ArrayList<>();
        for (RenderResourcesV3.RenderSource renderSource : imgResources.getSourceList()) {
            result.addAll(renderSpecialResource(matrixEx, renderSource, renderSource.isFullMatch()));
        }

        // 最后走一个兜底的1x1的渲染
        scanMatrix(matrix.getWidth(), matrix.getHeight(), (x, y) -> {
            if (matrix.get(x, y) == 1) {
                result.add(new PreRenderDot()
                        .setRow(1)
                        .setCol(1)
                        .setX(matrixEx.getLeftPadding() + x * matrixEx.getMultiple())
                        .setY(matrixEx.getTopPadding() + y * matrixEx.getMultiple())
                        .setSize(matrixEx.getMultiple())
                        .setResource(imgResources.getDefaultDrawImg()));
            }
        });

        return result;
    }

    private static List<RenderDot> renderSpecialResource(BitMatrixEx matrixEx, RenderResourcesV3.RenderSource renderSource, boolean fullMatch) {
        if (renderSource.countOver()) {
            return Collections.emptyList();
        }
        List<RenderDot> result = new ArrayList<>();
        ByteMatrix matrix = matrixEx.getByteMatrix();
        for (int x = 0; x < matrix.getWidth() - renderSource.getCol() + 1; x++) {
            for (int y = 0; y < matrix.getHeight() - renderSource.getRow() + 1; y++) {
                if (match(matrix, renderSource, x, y, fullMatch)) {
                    result.add(renderDot(matrixEx, renderSource, x, y));
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

    private static RenderDot renderDot(BitMatrixEx matrixEx, RenderResourcesV3.RenderSource renderSource, int x, int y) {
        PreRenderDot renderDot = new PreRenderDot();
        renderDot.setCol(renderSource.getCol()).setRow(renderSource.getRow()).setX(matrixEx.getLeftPadding() + x * matrixEx.getMultiple()).setY(matrixEx.getTopPadding() + y * matrixEx.getMultiple()).setSize(matrixEx.getMultiple()).setResource(renderSource.getResource());

        // 将命中的标记为已渲染
        scanMatrix(renderSource.getCol(), renderDot.getRow(), (col, row) -> {
            if (!renderSource.miss(col, row)) {
                matrixEx.getByteMatrix().set(x + col, y + row, 0);
            }
        });
        return renderDot;
    }

}
