package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.jhlabs.composite.ColorDodgeComposite;
import com.jhlabs.image.*;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * https://blog.csdn.net/ailiana/article/details/108468349
 * 图片转素描
 */
public class SketchTest {

    @Test
    public void img2sketch() throws Exception {
        BufferedImage composite = toSketch("D://MobileFile/jj2.jpg");


        //输出做好的素描
        File outputfile = new File("D://MobileFile/ske_saved.png");
        ImageIO.write(composite, "png", outputfile);
    }

    public static BufferedImage toSketch(String img) throws IOException {
        BufferedImage src = ImageUtils.convertImageToARGB(ImageLoadUtil.getImageByPath(img));

        //图像灰度化
        PointFilter grayScaleFilter = new GrayscaleFilter();
        BufferedImage grayScale = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        grayScaleFilter.filter(src, grayScale);

        //灰度图像反色
        BufferedImage inverted = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        PointFilter invertFilter = new InvertFilter();
        invertFilter.filter(grayScale, inverted);

        //高斯模糊处理
        GaussianFilter gaussianFilter = new GaussianFilter(20);
        BufferedImage gaussianFiltered = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        gaussianFilter.filter(inverted, gaussianFiltered);

        // 灰度图像和高斯模糊反向图混合
        ColorDodgeComposite cdc = new ColorDodgeComposite(1.0f);
        CompositeContext cc = cdc.createContext(inverted.getColorModel(), grayScale.getColorModel(), null);
        WritableRaster invertedR = gaussianFiltered.getRaster();
        WritableRaster grayScaleR = grayScale.getRaster();
        // 混合之后的就是我们希望的结果
        BufferedImage composite = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        WritableRaster colorDodgedR = composite.getRaster();
        cc.compose(invertedR, grayScaleR, colorDodgedR);
        return composite;
    }
}
