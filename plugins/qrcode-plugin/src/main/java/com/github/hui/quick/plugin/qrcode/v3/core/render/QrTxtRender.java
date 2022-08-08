package com.github.hui.quick.plugin.qrcode.v3.core.render;

import com.github.hui.quick.plugin.qrcode.util.ForEachUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.TxtMode;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import org.apache.commons.lang3.StringUtils;

/**
 * 二维字符矩阵
 *
 * @author YiHui
 * @date 2022/8/8
 */
public class QrTxtRender {
    private static final String BLACK = "⬛";
    private static final String WHITE = "⬜";

    public static StringBuilder drawQrInfo(BitMatrixEx matrix, QrCodeV3Options options) {
        QrResource draw = options.getDrawOptions().getResourcePool().getDefaultDrawResource();
        if (draw == null) draw = new QrResource().setText(BLACK).setTxtMode(TxtMode.FULL);
        else if (StringUtils.isBlank(draw.getText())) draw.setText(BLACK);

        QrResource bg = options.getDrawOptions().getResourcePool().getDefaultBgResource();
        if (bg == null) bg = new QrResource().setText(WHITE).setTxtMode(TxtMode.FULL);
        else if (StringUtils.isBlank(bg.getText())) bg.setText(WHITE);

        StringBuilder builder = new StringBuilder();
        QrResource finalDraw = draw;
        QrResource finalBg = bg;
        ForEachUtil.foreach(matrix.getByteMatrix().getWidth(), matrix.getByteMatrix().getHeight(), (x, y) -> {
            if (matrix.getByteMatrix().get(x, y) == 1) {
                builder.append(finalDraw.getText());
            } else {
                builder.append(finalBg.getText());
            }
        }, x -> builder.append("\n"));
        return builder;
    }
}
