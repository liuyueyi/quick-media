package com.github.hui.quick.plugin.base;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

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

        if (path.startsWith("http")) { // 从网络获取logo
            return ImageIO.read(new URL(path));
        } else if (path.startsWith("/")) { // 绝对地址获取logo
            return ImageIO.read(new File(path));
        } else { // 从资源目录下获取logo
            return ImageIO.read(ImageLoadUtil.class.getClassLoader().getResourceAsStream(path));
        }
    }


}
