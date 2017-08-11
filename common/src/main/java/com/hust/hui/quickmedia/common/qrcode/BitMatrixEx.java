package com.hust.hui.quickmedia.common.qrcode;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import lombok.Getter;
import lombok.Setter;

/**
 * 扩展的二维码矩阵信息， 主要新增了三个位置探测图形的判定
 * <p>
 * Created by yihui on 2017/7/27.
 */
@Getter
@Setter
public class BitMatrixEx {
    private final int width;
    private final int height;
    private final int rowSize;
    private final int[] bits;


    /**
     * 左白边大小
     */
    private int leftPadding;

    /**
     * 上白边大小
     */
    private int topPadding;

    /**
     * 矩阵信息缩放比例
     */
    private int multiple;


    private BitMatrix bitMatrix;


    @Getter
    @Setter
    private ByteMatrix byteMatrix;


    public BitMatrixEx(BitMatrix bitMatrix) {
        this(bitMatrix.getWidth(), bitMatrix.getHeight());
        this.bitMatrix = bitMatrix;

    }

    private BitMatrixEx(int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Both dimensions must be greater than 0");
        }

        this.width = width;
        this.height = height;
        this.rowSize = (width + 31) / 32;
        bits = new int[rowSize * height];
    }



    public void setRegion(int left, int top, int width, int height) {
        int right = left + width;
        int bottom = top + height;

        for (int y = top; y < bottom; y++) {
            int offset = y * rowSize;
            for (int x = left; x < right; x++) {
                bits[offset + (x / 32)] |= 1 << (x & 0x1f);
            }
        }
    }


    public boolean get(int x, int y) {
        return bitMatrix.get(x, y);
    }


    public boolean isDetectCorner(int x, int y) {
        int offset = y * rowSize + (x / 32);
        return ((bits[offset] >>> (x & 0x1f)) & 1) != 0;
    }
}
