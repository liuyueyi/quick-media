package com.github.hui.quick.plugin.photo;

import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import com.github.hui.quick.plugin.photo.operator.OperatorEnum;
import com.github.hui.quick.plugin.photo.options.OperateOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author yihui
 * @date 2022/6/14
 */
public class PhotoOperateWrapper {
    private OperateOptions<PhotoOperateWrapper> options;

    private PhotoOperateWrapper() {
    }

    public static OperateOptions<PhotoOperateWrapper> of(OperatorEnum operatorEnum) {
        PhotoOperateWrapper wrapper = new PhotoOperateWrapper();
        wrapper.options = operatorEnum.create(wrapper);
        return wrapper.options;
    }

    public BufferedImage asImg() {
        return options.operator().operate();
    }

    public boolean asFile(String fileName) throws IOException {
        File file = new File(fileName);
        FileWriteUtil.mkDir(file.getParentFile());

        BufferedImage bufferedImage = asImg();
        if (!ImageIO.write(bufferedImage, options.getImgType(), file)) {
            throw new IOException("save operator image to: " + fileName + " error!");
        }
        return true;
    }
}
