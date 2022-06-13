package com.github.hui.quick.plugin.test.v3;

import com.github.hui.quick.plugin.qrcode.util.QrUtil;
import com.github.hui.quick.plugin.qrcode.v3.QrGenV3Helper;
import com.github.hui.quick.plugin.qrcode.v3.canvas.QrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.options.QrOptions;
import com.github.hui.quick.plugin.qrcode.v3.options.qr.QrDetectPosition;
import com.github.hui.quick.plugin.qrcode.v3.resources.ResourceContainer;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * @author yihui
 * @date 2022/6/10
 */
public class QrGenV3Test {


    @Test
    public void testRenderToSvg() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            int size = 400;
            QrOptions qrOptions = QrOptions.of(msg).setW(size);

            BitMatrixEx bitMatrix = QrGenV3Helper.encode(qrOptions);

            QrCanvas qrCanvas = qrOptions.getQrCanvas();
            ResourceContainer container = qrOptions.getSources();

            int infoSize = bitMatrix.getMultiple();

            int leftPadding = bitMatrix.getLeftPadding();
            int topPadding = bitMatrix.getTopPadding();

            // 绘制码眼
            container.getDetectSource(QrDetectPosition.LEFT).renderDetect(bitMatrix.getByteMatrix(), qrCanvas, QrDetectPosition.LEFT, leftPadding, topPadding, infoSize);
            container.getDetectSource(QrDetectPosition.RIGHT).renderDetect(bitMatrix.getByteMatrix(), qrCanvas, QrDetectPosition.RIGHT, leftPadding, topPadding, infoSize);
            container.getDetectSource(QrDetectPosition.BOTTOM).renderDetect(bitMatrix.getByteMatrix(), qrCanvas, QrDetectPosition.BOTTOM, leftPadding, topPadding, infoSize);
            for (int x = 0; x < bitMatrix.getByteMatrix().getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getByteMatrix().getHeight(); y++) {
                    QrDetectPosition position = QrUtil.judgeDetectArea(bitMatrix.getByteMatrix(), x, y);
                    if (!position.isDetect()) {
                        container.getSource(bitMatrix.getByteMatrix(), x, y)
                                .renderQrInfo(bitMatrix.getByteMatrix(), qrCanvas, leftPadding, topPadding, x, y, infoSize);
                    }
                }
            }

            BufferedImage img = qrCanvas.output();
            System.out.println("---> over <---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
