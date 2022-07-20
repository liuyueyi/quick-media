package com.github.hui.quick.plugin.image.wrapper.wartermark.remove;

import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.image.wrapper.wartermark.remove.operator.WaterMarkRemoveOperator;
import com.github.hui.quick.plugin.image.wrapper.wartermark.remove.operator.WaterMarkRemoveTypeEnum;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author YiHui
 * @date 2022/7/13
 */
public class WaterMarkRemoveWrapper {
    private static volatile List<WaterMarkRemoveOperator> operators;

    private WaterMarkRemoveOptions waterMarkRemoveOptions;

    private WaterMarkRemoveWrapper() {
    }

    public static WaterMarkRemoveOptions of(String source) throws IOException {
        return of(ImageLoadUtil.getImageByPath(source));
    }

    public static WaterMarkRemoveOptions of(BufferedImage sourceImg) {
        WaterMarkRemoveWrapper wrapper = new WaterMarkRemoveWrapper();
        wrapper.waterMarkRemoveOptions = new WaterMarkRemoveOptions(wrapper).setImg(sourceImg);
        return wrapper.waterMarkRemoveOptions;
    }

    public BufferedImage asImage() {
        WaterMarkRemoveOperator operator = chooseOperator();
        if (operator == null) {
            throw new IllegalArgumentException("illegal WaterMarkRemoveOperatorType:" + waterMarkRemoveOptions.getType());
        }
        return operator.remove(waterMarkRemoveOptions);
    }

    /**
     * 保存到文件
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean asFile(String fileName) throws Exception {
        File file = new File(fileName);
        FileWriteUtil.mkDir(file.getParentFile());

        BufferedImage bufferedImage = asImage();
        if (!ImageIO.write(bufferedImage, "jpg", file)) {
            throw new IOException("save pixel render image to: " + fileName + " error!");
        }

        return true;
    }

    private WaterMarkRemoveOperator chooseOperator() {
        if (operators == null) {
            synchronized (this) {
                if (operators == null) {
                    List<WaterMarkRemoveOperator> operatorList = new ArrayList<>();
                    ServiceLoader<WaterMarkRemoveOperator> services = ServiceLoader.load(WaterMarkRemoveOperator.class);
                    for (WaterMarkRemoveOperator service : services) {
                        operatorList.add(service);
                    }
                    operatorList.addAll(Arrays.asList(WaterMarkRemoveTypeEnum.values()));
                    operatorList.sort(Comparator.comparingInt(WaterMarkRemoveOperator::sort));
                    operators = operatorList;
                }
            }
        }

        for (WaterMarkRemoveOperator operator : operators) {
            if (operator.match(waterMarkRemoveOptions.getType())) {
                return operator;
            }
        }
        return null;
    }
}
