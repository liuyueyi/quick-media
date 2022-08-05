package com.github.hui.quick.plugin.qrcode.wrapper;

import com.github.hui.quick.plugin.base.Base64Util;
import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import com.github.hui.quick.plugin.base.gif.GifHelper;
import com.github.hui.quick.plugin.qrcode.v3.constants.QrType;
import com.github.hui.quick.plugin.qrcode.v3.core.QrRenderFacade;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

/**
 * @author YiHui
 * @date 2022/8/5
 */
public class QrCodeGenV3 {
    private QrCodeV3Options options;

    private QrCodeGenV3() {
    }

    public static QrCodeV3Options of(String msg) {
        QrCodeGenV3 gen = new QrCodeGenV3();
        QrCodeV3Options options = new QrCodeV3Options(gen).setMsg(msg);
        gen.options = options;
        return options;
    }

    public BufferedImage asImg() throws Exception {
        return QrRenderFacade.renderAsImg(options);
    }

    public ByteArrayOutputStream asGif() throws Exception {
        List<ImmutablePair<BufferedImage, Integer>> list = QrRenderFacade.renderAsGif(options);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GifHelper.saveGif(list, outputStream);
        return outputStream;
    }

    public String asSvg() throws Exception {
        return QrRenderFacade.renderAsSvg(options);
    }

    public String asStr() throws Exception {
        if (options.getQrType() == QrType.GIF) {
            try (ByteArrayOutputStream stream = asGif()) {
                return Base64Util.encode(stream);
            }
        } else if (options.getQrType() == QrType.IMG) {
            BufferedImage bufferedImage = asImg();
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, options.getPicType(), outputStream);
                return Base64Util.encode(outputStream);
            }
        } else if (options.getQrType() == QrType.STR) {
            return null;
        } else {
            return null;
        }
    }


    public boolean asFile(String absFileName) throws Exception {
        File file = new File(absFileName);
        FileWriteUtil.mkDir(file.getParentFile());

        if (options.getQrType() == QrType.GIF) {
            try (ByteArrayOutputStream stream = asGif()) {
                FileOutputStream out = new FileOutputStream(file);
                out.write(stream.toByteArray());
                out.flush();
                out.close();
                return true;
            }
        } else if (options.getQrType() == QrType.IMG) {
            BufferedImage bufferedImage = asImg();
            if (!ImageIO.write(bufferedImage, options.getPicType(), file)) {
                throw new IOException("save QrCode image to: " + absFileName + " error!");
            }
            return true;
        } else if (options.getQrType() == QrType.STR) {
            return false;
        } else if (options.getQrType() == QrType.SVG) {
            String svg = asSvg();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(svg);
                writer.flush();
            }
            return true;
        }

        return false;
    }

}
