package com.github.hui.quick.plugin.test.v3.svg;

import com.github.hui.quick.plugin.base.OSUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;


/**
 * svg 格式二维码输出测试
 *
 * @author YiHui
 * @date 2022/8/5
 */
public class QrSvgGeometryGenTest extends BasicGenTest {

    @Before
    public void init() {
        super.init();
        prefix += "/svg";
    }

    // --------------------- 几何样式 ----------------------

    @Test
    public void rect() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300)
                .newDrawOptions()
                .setDrawStyle(DrawStyle.RECT)
                .complete()
                .asFile(prefix  + "/rect.svg");
        System.out.println(ans);
    }

    @Test
    public void rectV2() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300)
                .setDrawStyle(DrawStyle.RECT)
                .asFile(prefix  + "/rect.svg");
        System.out.println(ans);
    }

    @Test
    public void roundRect() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300).setDrawStyle(DrawStyle.ROUND_RECT).asFile(prefix + "/roundRect.svg");
        System.out.println(ans);
    }

    @Test
    public void miniRect() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300).setDrawStyle(DrawStyle.MINI_RECT).asFile(prefix + "/miniRect.svg");
        System.out.println(ans);
    }

    @Test
    public void triangle() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300).setDrawStyle(DrawStyle.TRIANGLE).asFile(prefix + "/triangle.svg");
        System.out.println(ans);
    }

    @Test
    public void diamond() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300).setDrawStyle(DrawStyle.DIAMOND).asFile(prefix + "/diamond.svg");
        System.out.println(ans);
    }

    @Test
    public void star() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setW(300)
                .setDrawStyle(DrawStyle.STAR)
                .setPreColor(Color.RED)
                .build()
                .asFile(prefix + "/star.svg");
        System.out.println(ans);
    }

    @Test
    public void hexagon() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setW(300).setDrawStyle(DrawStyle.HEXAGON).asFile(prefix + "/hexagon.svg");
        System.out.println(ans);
    }

    @Test
    public void octagon() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setW(300).setDrawStyle(DrawStyle.OCTAGON).asFile(prefix + "/octagon.svg");
        System.out.println(ans);
    }

    @Test
    public void circle() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300).setDrawStyle(DrawStyle.CIRCLE).asFile(prefix + "/circle.svg");
        System.out.println(ans);
    }
}
