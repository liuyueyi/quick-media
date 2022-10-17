package com.github.hui.quick.plugin.qrcode.v3.core.calculate;

import com.github.hui.quick.plugin.qrcode.v3.constants.QrArea;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResourcePool;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.BgRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.DetectRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.PreRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.RenderDot;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import org.apache.commons.lang3.BooleanUtils;

import java.util.*;

import static com.github.hui.quick.plugin.qrcode.util.ForEachUtil.foreach;

/**
 * 二维矩阵渲染
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class QrRenderDotGenerator {
    public static final int MATRIX_BG = 0;
    public static final int MATRIX_PRE = 1;
    public static final int MATRIX_PROCEED = 2;

    /**
     * 已处理的中间态
     */
    public static final int MATRIX_TMP_PROCEED = 3;

    /**
     * 计算渲染资源列表
     *
     * @param qrCodeConfig
     * @param bitMatrix
     * @return
     */
    public static List<RenderDot> calculateRenderDots(QrCodeV3Options qrCodeConfig, BitMatrixEx bitMatrix) {
        // 探测图形的大小
        int detectCornerSize = bitMatrix.getByteMatrix().get(0, 5) == MATRIX_PRE ? 7 : 5;

        int matrixW = bitMatrix.getByteMatrix().getWidth();
        int matrixH = bitMatrix.getByteMatrix().getHeight();

        List<RenderDot> result = new ArrayList<>();
        // 若探测图形特殊绘制，则提前处理掉
        QrResourcePool resourcePool = qrCodeConfig.getDrawOptions().getResourcePool();
        foreach(matrixW, matrixH, (x, y) -> {
            QrArea qrArea = inDetectCornerArea(x, y, matrixW, matrixH, detectCornerSize);
            if (qrArea.detectedArea() || qrArea.checkPoint()) {
                // 若探测图形特殊绘制，则单独处理
                if (bitMatrix.getByteMatrix().get(x, y) == MATRIX_PRE) {
                    if (BooleanUtils.isTrue(qrCodeConfig.getDetectOptions().getSpecial())) {
                        // 绘制三个位置探测图形、安全校验点
                        result.add(drawSpecialQrArea(qrCodeConfig, bitMatrix, detectCornerSize, x, y, qrArea));
                    }
                } else {
                    if (BooleanUtils.isNotTrue(qrCodeConfig.getDetectOptions().getSpecial())) {
                        // 探测图形非特殊处理时，0点图渲染
                        drawImgBgInfo(bitMatrix, x, y, resourcePool).ifPresent(result::add);
                    }
                }
            } else {
                // 非探测区域内的0点图渲染
                drawImgBgInfo(bitMatrix, x, y, resourcePool).ifPresent(result::add);
            }
        });
        result.addAll(drawQrInfo(bitMatrix, resourcePool));
        return result;
    }

    /**
     * 当背景点使用指定图片渲染时进行处理
     *
     * @param bitMatrix
     * @param x
     * @param y
     * @param resourcePool
     * @return
     */
    private static Optional<RenderDot> drawImgBgInfo(BitMatrixEx bitMatrix, int x, int y, QrResourcePool resourcePool) {
        if (bitMatrix.getByteMatrix().get(x, y) == MATRIX_BG && resourcePool.getDefaultBgResource() != null) {
            bitMatrix.getByteMatrix().set(x, y, MATRIX_PROCEED);
            // 非探测区域内的0点图渲染
            return Optional.of(new BgRenderDot()
                    .setH(1)
                    .setW(1)
                    .setX(bitMatrix.getLeftPadding() + x * bitMatrix.getMultiple())
                    .setY(bitMatrix.getTopPadding() + y * bitMatrix.getMultiple())
                    .setSize(bitMatrix.getMultiple())
                    .setResource(resourcePool.getDefaultBgResource()));
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
    private static QrArea inDetectCornerArea(int x, int y, int matrixW, int matrixH, int detectCornerSize) {
        if (x < detectCornerSize && y < detectCornerSize) {
            // 左上角
            return QrArea.DETECT_LT;
        }

        if (x < detectCornerSize && y >= matrixH - detectCornerSize) {
            // 左下角
            return QrArea.DETECT_LD;
        }

        if (x >= matrixW - detectCornerSize && y < detectCornerSize) {
            // 右上角
            return QrArea.DETECT_RT;
        }

        if (detectCornerSize == 5) {
            // 不存在定位点
            return QrArea.NONE;
        }

        if (x >= matrixW - 9 && x < matrixW - 4 && y >= matrixH - 9 && y < matrixH - 4) {
            // 定位点
            return QrArea.CHECK_POINT;
        }


        return QrArea.NONE;
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
    private static RenderDot drawSpecialQrArea(QrCodeV3Options qrCodeConfig, BitMatrixEx bitMatrix, int detectCornerSize,
                                               int x, int y, QrArea qrArea) {
        int matrixW = bitMatrix.getByteMatrix().getWidth();
        int matrixH = bitMatrix.getByteMatrix().getHeight();
        if (qrArea.checkPoint()) detectCornerSize = 5;

        DetectRenderDot renderDot = new DetectRenderDot();
        renderDot.setLocation(qrArea)
                .setX(bitMatrix.getLeftPadding() + x * bitMatrix.getMultiple())
                .setY(bitMatrix.getTopPadding() + y * bitMatrix.getMultiple())
                .setSize(bitMatrix.getMultiple());

        QrResource detectResource = qrCodeConfig.getDetectOptions().chooseDetectResource(qrArea);
        if (detectResource != null && BooleanUtils.isTrue(qrCodeConfig.getDetectOptions().getWhole())) {
            // 图片直接渲染完毕之后，将其他探测图形的点设置为0，表示不需要再次渲染
            foreach(detectCornerSize, detectCornerSize, (addX, addY) -> bitMatrix.getByteMatrix().set(x + addX, y + addY, MATRIX_PROCEED));
            // 码眼整个用一张资源渲染
            renderDot.setOutBorder(false).setDotNum(detectCornerSize).setResource(detectResource);
            return renderDot;
        }
        renderDot.setDotNum(1)
                // 设置当前这个点是探测图形的外边框，还是内边框
                .setOutBorder(inOuterCornerArea(x, y, matrixW, matrixH, detectCornerSize, qrArea))
                .setResource(detectResource);
        bitMatrix.getByteMatrix().set(x, y, MATRIX_PROCEED);
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
    private static boolean inOuterCornerArea(int x, int y, int matrixW, int matrixH, int detectCornerSize, QrArea area) {
        if (area.detectedArea()) {
            // 外层的框
            return x == 0 || x == detectCornerSize - 1 || x == matrixW - 1 || x == matrixW - detectCornerSize || y == 0 || y == detectCornerSize - 1 || y == matrixH - 1 || y == matrixH - detectCornerSize;
        } else {
            // 定位点
            return x == matrixW - 9 || x == matrixW - 5 || y == matrixH - 9 || y == matrixH - 5;
        }
    }

    /**
     * 绘制二维码信息
     *
     * @param matrixEx
     * @param imgResources
     * @return
     */
    private static List<RenderDot> drawQrInfo(BitMatrixEx matrixEx, QrResourcePool imgResources) {
        ByteMatrix matrix = matrixEx.getByteMatrix();
        List<RenderDot> result = new ArrayList<>();
        for (QrResourcePool.QrResourcesDecorate renderSource : imgResources.getSourceList()) {
            result.addAll(renderSpecialResource(matrixEx, renderSource, renderSource.isFullMatch()));
        }

        // 最后走一个兜底的1x1的渲染
        foreach(matrix.getWidth(), matrix.getHeight(), (x, y) -> {
            if (matrix.get(x, y) == 1) {
                result.add(new PreRenderDot()
                        .setH(1)
                        .setW(1)
                        .setX(matrixEx.getLeftPadding() + x * matrixEx.getMultiple())
                        .setY(matrixEx.getTopPadding() + y * matrixEx.getMultiple())
                        .setSize(matrixEx.getMultiple())
                        .setResource(imgResources.getDefaultDrawResource()));
            }
        });

        return result;
    }

    private static List<PreRenderDot> renderSpecialResource(BitMatrixEx matrixEx, QrResourcePool.QrResourcesDecorate renderSource, boolean fullMatch) {
        if (renderSource.countOver()) {
            return Collections.emptyList();
        }

        // 当资源存在次数限制时，为了避免每次都是前面几个满足条件的位置被渲染，调整一下匹配策略；先捞出全部的，然后再按照资源数量进行挑选
        List<PreRenderDot> renderDots = new ArrayList<>();
        ByteMatrix matrix = matrixEx.getByteMatrix();
        foreach(matrix.getWidth() - renderSource.getWidth() + 1,
                matrix.getHeight() - renderSource.getHeight() + 1,
                (x, y) -> {
                    if (match(matrix, renderSource, x, y, fullMatch)) {
                        renderDots.add(renderDot(matrixEx, renderSource, x, y));
                    }
                });

        // 当存在计数的资源时，将这些计数资源随机分布在计算出来的RenderDot中
        List<PreRenderDot> result = new ArrayList<>(renderDots.size());
        // 将渲染资源进行随机排列
        Collections.shuffle(renderDots);
        for (PreRenderDot dot : renderDots) {
            if (renderSource.countOver()) {
                // 资源已经用完，恢复之前的标记
                markMatrix(matrixEx, dot, renderSource, MATRIX_PRE);
                continue;
            }

            QrResource qrResource = renderSource.getResource();
            if (qrResource == null) {
                markMatrix(matrixEx, dot, renderSource, MATRIX_PRE);
            } else {
                dot.setResource(qrResource);
                result.add(dot);
                markMatrix(matrixEx, dot, renderSource, MATRIX_PROCEED);
            }
        }

        // 恢复顺序
        result.sort((o1, o2) -> {
            if (o1.getI() < o2.getI()) {
                return -1;
            } else if (o1.getI() == o2.getI()) {
                return Objects.compare(o1.getJ(), o2.getJ(), Integer::compare);
            } else {
                return 1;
            }
        });
        return result;
    }

    private static boolean match(ByteMatrix matrix, QrResourcePool.QrResourcesDecorate renderSource, int startX, int startY, boolean fullMatch) {
        // 要求矩阵的1点图，能完全覆盖 renderSource
        for (int x = 0; x < renderSource.getWidth(); x++) {
            for (int y = 0; y < renderSource.getHeight(); y++) {
                if (!renderSource.miss(x, y) && matrix.get(startX + x, startY + y) != MATRIX_PRE) {
                    // 资源图存在，但是矩阵点不存在，则表示未满足条件，直接退出
                    return false;
                }

                if (fullMatch) {
                    // 全匹配时，则要求资源图是空格的地方，二维码矩阵也是空白的
                    // 非全匹配时，只要求资源图有的地方，二维码矩阵也有
                    if (renderSource.miss(x, y) && matrix.get(startX + x, startY + y) == MATRIX_PRE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static PreRenderDot renderDot(BitMatrixEx matrixEx, QrResourcePool.QrResourcesDecorate renderSource, int x, int y) {
        PreRenderDot renderDot = new PreRenderDot();
        renderDot.setW(renderSource.getWidth())
                .setH(renderSource.getHeight())
                .setI(x).setJ(y)
                .setX(matrixEx.getLeftPadding() + x * matrixEx.getMultiple())
                .setY(matrixEx.getTopPadding() + y * matrixEx.getMultiple())
                .setSize(matrixEx.getMultiple());

        // 将命中的标记为已渲染
        markMatrix(matrixEx, renderDot, renderSource, MATRIX_TMP_PROCEED);
        return renderDot;
    }

    private static void markMatrix(BitMatrixEx matrixEx, PreRenderDot preRenderDot, QrResourcePool.QrResourcesDecorate renderSource, int val) {
        foreach(renderSource.getWidth(), renderSource.getHeight(), (w, h) -> {
            if (!renderSource.miss(w, h)) {
                matrixEx.getByteMatrix().set(preRenderDot.getI() + w, preRenderDot.getJ() + h, val);
            }
        });
    }

}
