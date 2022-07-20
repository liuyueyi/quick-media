package com.github.hui.quick.plugin.qrcode.v3;

import com.github.hui.quick.plugin.qrcode.helper.QrCodeRenderHelper;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.BgRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.DetectRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.RenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.RenderResourcesV3;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 二维矩阵渲染
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class QrCodeMatrixRenderHelper {
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
        for (int x = 0; x < matrixW; x++) {
            for (int y = 0; y < matrixH; y++) {
                QrCodeRenderHelper.DetectLocation detectLocation = inDetectCornerArea(x, y, matrixW, matrixH, detectCornerSize);
                if (detectLocation.detectedArea()) {
                    // 若探测图形特殊绘制，则单独处理
                    if (qrCodeConfig.getDetectOptions().getSpecial()) {
                        if (bitMatrix.getByteMatrix().get(x, y) == 1) {
                            // 绘制三个位置探测图形
                            result.add(drawDetectImg(qrCodeConfig, bitMatrix, detectCornerSize, x, y, detectLocation));
                        }
                    } else {
                        if (bitMatrix.getByteMatrix().get(x, y) == 0 && imgResourcesV3.getDefaultBgImg() != null) {
                            result.add(new BgRenderDot().setRow(1).setCol(1).setX(x).setY(y).setResource(imgResourcesV3.getDefaultBgImg()));
                        }
                    }
                } else if (bitMatrix.getByteMatrix().get(x, y) == 0 && imgResourcesV3.getDefaultBgImg() != null) {
                    // 非探测区域内的0点图渲染
                    result.add(new BgRenderDot().setRow(1).setCol(1).setX(x).setY(y).setResource(imgResourcesV3.getDefaultBgImg()));
                }
            }
        }
        result.addAll(DotRenderV3Helper.renderResources(bitMatrix.getByteMatrix(), imgResourcesV3));
        return result;
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
    private static RenderDot drawDetectImg(QrCodeV3Options qrCodeConfig, BitMatrixEx bitMatrix, int detectCornerSize, int x, int y,
                                           QrCodeRenderHelper.DetectLocation detectLocation) {
        int matrixW = bitMatrix.getByteMatrix().getWidth();
        int matrixH = bitMatrix.getByteMatrix().getHeight();

        DetectRenderDot renderDot = new DetectRenderDot();
        renderDot.setLocation(detectLocation).setX(x).setY(y);

        QrResource detectResource = qrCodeConfig.getDetectOptions().chooseDetectResource(detectLocation);
        if (detectResource != null) {
            // 图片直接渲染完毕之后，将其他探测图形的点设置为0，表示不需要再次渲染
            for (int addX = 0; addX < detectCornerSize; addX++) {
                for (int addY = 0; addY < detectCornerSize; addY++) {
                    bitMatrix.getByteMatrix().set(x + addX, y + addY, 0);
                }
            }
            // 码眼整个用一张资源渲染
            renderDot.setSize(detectCornerSize);
            return renderDot;
        }

        RenderResourcesV3 renderResourcesV3 = qrCodeConfig.getDrawOptions().getRenderResourcesV3();
        renderDot.setSize(1)
                .setOutBorder(inOuterDetectCornerArea(x, y, matrixW, matrixH, detectCornerSize))
                .setResource(renderResourcesV3.getDefaultDrawImg());
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
        return x == 0 || x == detectCornerSize - 1 || x == matrixW - 1 || x == matrixW - detectCornerSize ||
                y == 0 || y == detectCornerSize - 1 || y == matrixH - 1 || y == matrixH - detectCornerSize;
    }


}
