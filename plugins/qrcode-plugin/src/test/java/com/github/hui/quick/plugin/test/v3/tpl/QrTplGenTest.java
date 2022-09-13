package com.github.hui.quick.plugin.test.v3.tpl;

import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;

import java.awt.*;

/**
 * 模板渲染测试类
 *
 * @author YiHui
 * @date 2022/8/13
 */
public class QrTplGenTest extends BasicGenTest {

    @Test
    public void imgTplTest() throws Exception {
        String content = FileReadUtil.readAll("tpl/flower.tpl");
        Boolean ans = QrCodeGenV3.of(msg).setW(700)
                .setDetectSpecial(true)
                .setImgTemplate(content).build()
                .asFile(prefix + "/img/flower.png");
        System.out.println("over");
    }

    @Test
    public void catTplTest() throws Exception {
        String svgTemplate = FileReadUtil.readAll("tpl/龙猫.tpl");
        boolean ans = QrCodeGenV3.of(msg).setW(500)
                .setSvgTemplate(svgTemplate)
//                .setLogoRate(20)
                .setErrorCorrection(ErrorCorrectionLevel.H)
                .build()
                .asFile(prefix + "/svg/龙猫.svg");
        System.out.println(ans);
    }

    @Test
    public void starTest() throws Exception {
        String svgTemplate = FileReadUtil.readAll("tpl/五星红旗.tpl");
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setDetect(new QrResource().setDrawStyle(DrawStyle.RECT).setDrawColor(Color.RED)).setDetectWhole(false)
                .setSvgTemplate(svgTemplate)
                .asFile(prefix + "/svg/五星红旗.svg");
        System.out.println(ans);
    }
}
