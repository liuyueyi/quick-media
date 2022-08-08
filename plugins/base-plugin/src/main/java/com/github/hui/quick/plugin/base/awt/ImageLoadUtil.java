package com.github.hui.quick.plugin.base.awt;

import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.base.gif.GifDecoder;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yihui on 2018/3/23.
 */
public class ImageLoadUtil {

    /**
     * 根据路径获取图片
     *
     * @param path 本地路径 or 网络地址
     * @return 图片
     * @throws IOException
     */
    public static BufferedImage getImageByPath(String path) throws IOException {
        if (StringUtils.isBlank(path)) {
            return null;
        }

        try (InputStream stream = FileReadUtil.getStreamByFileName(path)) {
            return ImageIO.read(stream);
        }
    }

    /**
     * 根据路径获取gif图片
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static GifDecoder getGifByPath(String path) throws IOException {
        if (StringUtils.isBlank(path)) {
            return null;
        }

        GifDecoder decoder = new GifDecoder();
        decoder.read(FileReadUtil.getStreamByFileName(path));
        return decoder;
    }
}
