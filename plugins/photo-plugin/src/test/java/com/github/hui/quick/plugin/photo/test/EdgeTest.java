package com.github.hui.quick.plugin.photo.test;

import com.github.hui.quick.plugin.photo.PhotoOperateWrapper;
import com.github.hui.quick.plugin.photo.operator.EdgeOperator;
import com.github.hui.quick.plugin.photo.operator.OperatorEnum;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片边缘检测
 *
 * @author yihui
 * @date 2022/6/14
 */
public class EdgeTest {

    @Test
    public void testCanny() {
        BufferedImage out = ((EdgeOperator.EdgeOperateOptions<PhotoOperateWrapper>) PhotoOperateWrapper.of(OperatorEnum.EDGE))
                .setType(EdgeOperator.EdgeType.CANNY)
                .setBgColor(Color.WHITE)
                .setLineColor(Color.BLACK)
                .setImg("https://img0.baidu.com/it/u=591720380,976271434&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=1111")
                .build()
                .asImg();
        System.out.println("----");
    }

    @Test
    public void testSobel() {
        BufferedImage out = PhotoOperateWrapper.of(OperatorEnum.EDGE)
                .parse(EdgeOperator.EdgeOperateOptions.type())
                .setType(EdgeOperator.EdgeType.SOBEL)
                .setBgColor(Color.WHITE)
                .setLineColor(Color.BLACK)
//                .setImg("https://img0.baidu.com/it/u=591720380,976271434&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=1111")
                .setImg("https://img2.baidu.com/it/u=2063372117,4261025591&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=1000")
                .setImg("https://img2.woyaogexing.com/2017/05/24/432c24863a7ab65a!600x600.jpg")
                .build()
                .asImg();
        System.out.println("----");
    }
}
