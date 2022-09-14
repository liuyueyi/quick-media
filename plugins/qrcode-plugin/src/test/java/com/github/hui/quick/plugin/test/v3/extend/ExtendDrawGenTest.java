package com.github.hui.quick.plugin.test.v3.extend;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Test;

import java.awt.*;

/**
 * @author YiHui
 * @date 2022/9/14
 */
public class ExtendDrawGenTest extends BasicGenTest {
    @Test
    public void selfDefGen() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                // 使用自定义的渲染实现类，从而达到二维码下面文字签名的case
                .setFtResource(new QrResource()
                        .setDrawStyle(new SelfDefineDrawStyle())
                        .setDrawColor(Color.BLACK).setText("欢迎关注一灰灰blog\n    by quick-media")
                        .setFontStyle(Font.BOLD))
                .setFtFillColor(ColorUtil.OFF_WHITE)
                .setBgColor(ColorUtil.OFF_WHITE)
                .setFtW(500)
                .setFtH(600)
                .setFtX(0)
                .setFtY(0)
                .asFile(prefix + "/img/extend_ft.png");
        System.out.println(ans);
    }

    @Test
    public void selfDefGenSvg() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setDrawStyle(new SelfDefineDrawStyle())
                .asFile(prefix + "/svg/extend_leaf.svg");
        System.out.println(ans);
    }
}
