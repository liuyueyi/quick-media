package com.github.hui.quick.plugin.test.v3.img;

import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

/**
 * 探测图形/码眼 的填充姿势
 *
 * @author YiHui
 * @date 2022/9/13
 */
public class QrImgDetectGenTest extends BasicGenTest {
    @Before
    public void init() {
        super.init();
        prefix += "/img";
    }

    /**
     * 指定内外层颜色
     */
    @Test
    public void detectColor() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).newDetectOptions().setOutColor(Color.RED).setInColor(Color.PINK)
                .complete().asFile(prefix + "/detect_color.png");
        System.out.println(ans);
    }

    /**
     * 指定探测图形为预定义的样式： 圆形、五角星等
     *
     * @throws Exception
     */
    @Test
    public void detectGeo() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).newDetectOptions()
                .setResource(new QrResource().setDrawStyle(DrawStyle.CIRCLE))
                .setColor(Color.RED)
                // 这一个配置不能少，默认是true，表示整个探测图形会用指定的资源进行绘制
                .setWhole(false)
                .complete()
                .asFile(prefix + "/detect_geo.png");
        System.out.println(ans);
    }

    /**
     * 探测图形特殊配置
     *
     * @throws Exception
     */
    @Test
    public void detectSpecial() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setDetectSpecial(true)
                .setDrawStyle(DrawStyle.STAR).asFile(prefix + "/detect_special.png");
        System.out.println(ans);
    }

    @Test
    public void detectImg() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).newDetectOptions()
                .setResource(new QrResource().setImg("detect.png"))
                .complete().asFile(prefix + "/detect_img.png");
        System.out.println(ans);
    }

    @Test
    public void detectImgV2() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).newDetectOptions()
                .setLt(new QrResource().setImg("leaf/eye.png"))
                .setRt(new QrResource().setImg("jihe/PDP.png"))
                .setLd(new QrResource().setImg("love/01.png"))
                .complete()
                .asFile(prefix + "/detect_img_v2.png");
        System.out.println(ans);
    }
}
