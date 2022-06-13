package com.github.hui.quick.plugin.qrcode.util;

import com.github.hui.quick.plugin.qrcode.v3.options.qr.QrDetectPosition;
import com.google.zxing.qrcode.encoder.ByteMatrix;

/**
 * @author yihui
 * @date 2022/6/12
 */
public class QrUtil {

    /**
     * 判断是否为码眼区域
     *
     * @param matrix
     * @param x
     * @param y
     * @return
     */
    public static QrDetectPosition judgeDetectArea(ByteMatrix matrix, int x, int y) {
        int detectCornerSize = matrix.get(0, 5) == 1 ? 7 : 5;
        int matrixW = matrix.getWidth(), matrixH = matrix.getHeight();
        if (x < detectCornerSize && y < detectCornerSize) {
            // 左上角
            return QrDetectPosition.LEFT;
        }

        if (x < detectCornerSize && y >= matrixH - detectCornerSize) {
            // 左下角
            return QrDetectPosition.BOTTOM;
        }

        if (x >= matrixW - detectCornerSize && y < detectCornerSize) {
            // 右上角
            return QrDetectPosition.RIGHT;
        }

        return QrDetectPosition.NONE;
    }
}
