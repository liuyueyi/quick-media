package com.github.hui.quick.plugin.base;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Created by yihui on 2017/7/17.
 */
public class Base64Util {

    public static String encode(BufferedImage bufferedImage, String imgType) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, imgType, outputStream);
        return encode(outputStream);
    }

    public static String encode(ByteArrayOutputStream outputStream) {
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

}
