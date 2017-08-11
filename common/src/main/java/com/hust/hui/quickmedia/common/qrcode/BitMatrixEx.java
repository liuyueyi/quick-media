package com.hust.hui.quickmedia.common.qrcode;

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
    /**
     * 实际生成二维码的宽
     */
    private int width;


    /**
     * 实际生成二维码的高
     */
    private int height;


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

    private ByteMatrix byteMatrix;
}
