package com.github.hui.quick.plugin.svg.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by yihui on 2018/1/14.
 */
public class ImgParseUtil {
    public static BufferedImage parseBytes2Image(byte[] bytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        return bufferedImage;
    }
}
