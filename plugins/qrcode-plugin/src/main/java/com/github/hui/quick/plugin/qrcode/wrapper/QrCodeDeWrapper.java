package com.github.hui.quick.plugin.qrcode.wrapper;

import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 解析二维码的工具类
 * <p>
 * Created by yihui on 2017/7/17.
 */
public class QrCodeDeWrapper {


    /**
     * 读取二维码中的内容, 并返回
     *
     * @param qrcodeImg 二维码图片的地址
     * @return 返回二维码的内容
     * @throws IOException       读取二维码失败
     * @throws NotFoundException
     */
    public static String decode(String qrcodeImg) throws IOException, NotFoundException {
        BufferedImage image = ImageLoadUtil.getImageByPath(qrcodeImg);
        return decode(image);
    }


    public static String decode(BufferedImage image) throws NotFoundException {
        if (image == null) {
            throw new IllegalStateException("can not load qrCode!");
        }


        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Map<DecodeHintType, Object> hints = new LinkedHashMap<DecodeHintType, Object>();
        // 解码设置编码方式为：utf-8，
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        //优化精度
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        //复杂模式，开启PURE_BARCODE模式
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

}
