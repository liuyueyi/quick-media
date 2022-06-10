package com.github.hui.quick.plugin.test.v3;

import com.github.hui.quick.plugin.qrcode.v3.QrGenV3Helper;
import com.github.hui.quick.plugin.qrcode.v3.canvas.QrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.options.QrOptions;
import com.github.hui.quick.plugin.qrcode.v3.options.SourceOptions;
import com.github.hui.quick.plugin.qrcode.v3.resources.BasicRenderResource;
import com.github.hui.quick.plugin.qrcode.v3.resources.RenderSource;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import org.junit.Test;

import java.awt.*;
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
            RenderSource renderSource = new BasicRenderResource(new SourceOptions().setColor(Color.BLACK));

            int infoSize = bitMatrix.getMultiple();

            int leftPadding = bitMatrix.getLeftPadding();
            int topPadding = bitMatrix.getTopPadding();


            for (int x = 0; x < bitMatrix.getByteMatrix().getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getByteMatrix().getHeight(); y++) {
                    if (bitMatrix.getByteMatrix().get(x, y) == 1)
                        renderSource.render(qrCanvas, leftPadding + x * infoSize, topPadding + y * infoSize, infoSize, infoSize);
                }
            }

            BufferedImage img = qrCanvas.output();
            System.out.println("---> over <---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
