package com.github.hui.quick.plugin.photo.test;

import com.github.hui.quick.plugin.photo.PhotoOperateWrapper;
import com.github.hui.quick.plugin.photo.operator.OperatorEnum;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * @author yihui
 * @date 2022/6/14
 */
public class PhotoTest {

    @Test
    public void testSketch() {
        BufferedImage out = PhotoOperateWrapper.of(OperatorEnum.SKETCH)
                .setImg("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF")
                .build()
                .asImg();
        System.out.println("----");
    }
}
