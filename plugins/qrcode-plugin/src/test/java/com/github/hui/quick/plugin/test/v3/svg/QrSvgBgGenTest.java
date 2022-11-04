package com.github.hui.quick.plugin.test.v3.svg;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.QrType;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

/**
 * @author YiHui
 * @date 2022/8/23
 */
public class QrSvgBgGenTest extends BasicGenTest {
    @Before
    public void init() {
        super.init();
        prefix += "/svg";
    }


    /**
     * svg 背景图，全覆盖方式，指定二维码透明度为0.6f
     */
    @Test
    public void imgBg() {
        try {
            // 可以直接加载网络图片，为了避免网络资源丢失，原图下载到测试资源目录下
//            String bg = "http://ww1.sinaimg.cn/large/8154e929gy1g8vho8x6r0j20b40b43yl.jpg";
            String bg = "bgs/xjs.jpg";
            boolean ans = QrCodeGenV3.of(msg).setSize(500)
                    .newBgOptions()
                    .setOpacity(0.6f)
                    .setBg(new QrResource().setImg(bg))
                    .complete()
                    .setQrType(QrType.SVG)
                    .asFile(prefix + "/bq_default.svg");
            System.out.println(ans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在背景图的指定位置上输出二维码
     *
     * @throws Exception
     */
    @Test
    public void fillBg() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(550)
                .newBgOptions()
                .setBg(new QrResource().setImg("bgs/qrbg.jpg"))
                .setBgStyle(BgStyle.FILL)
                .setStartX(225)
                .setStartY(320)
                .complete()
                .asFile(prefix + "/bg_fill.svg");
        System.out.println(ans);
    }

    @Test
    public void svgBg() throws Exception {
        try {
            String bg = "<symbol id=\"bg\" width='100%' height='100%'>\n" +
                   "<linearGradient id='g' x2='1' y2='1'>\n" +
                    "<stop stop-color='#F19'/>\n" +
                    "<stop offset='100%' stop-color='#0CF'/>\n" +
                    "</linearGradient>\n" +
                    "<rect width='100%' height='100%' fill='url(#g)'/>" +
                    "</symbol>" +
                    "<path d='M-2 10L10 -2ZM10 6L6 10ZM-2 2L2 -2' stroke='#222' stroke-width='4.5'/>";
            boolean ans = QrCodeGenV3.of(msg).setSize(500)
                    .newBgOptions()
                    .setOpacity(1f)
                    .setBg(bg)
                    .complete()
                    .setQrType(QrType.SVG)
                    .setPreColor(Color.BLUE)
                    .asFile(prefix + "/bq_svg.svg");
            System.out.println(ans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void txtBg() throws Exception {
        try {
            String bg = "<symbol viewBox=\"0 0 1024 1024\" id=\"bg\">\n" +
                    "<path d=\"M118.839458 226.7853q-32.680851-119.829787-102.003868-226.7853l92.10058 0q68.332689 95.071567 102.003868 226.7853l-92.10058 0zM113.887814 277.29207q74.274662 105.965184 110.916828 227.775629l-92.10058 0q-40.603482-128.742747-106.955513-227.775629l88.139265 0zM725.911025 314.924565l346.615087 0 0 64.371373-346.615087 0 0 204.007737 268.37911 0 0 287.195358q3.961315 148.549323-153.500967 143.597679l-481.299807 0 0-430.793037 268.37911 0 0-204.007737-333.740812 0 0-64.371373 333.740812 0 0-200.046422-296.108317 0 0-59.419729 467.435203 0q110.916828 0 222.823985-32.680851l0 69.323017q-49.516441 11.883946-102.003868 17.330754t-110.916828 5.446809l-83.187621 0 0 200.046422zM451.589942 648.665377l0 301.059961 361.470019 0q88.139265 3.961315 88.139265-74.274662l0-226.7853-449.609284 0zM2.970986 1024q53.477756-109.926499 89.129594-224.804642t54.468085-238.669246l78.235977 0q-32.680851 260.45648-119.829787 463.473888l-102.003868 0z\" p-id=\"9625\" fill=\"#1296db\"></path>" +
                    "</symbol>";
            boolean ans = QrCodeGenV3.of(msg).setSize(500)
                    .newBgOptions()
                    .setOpacity(1f)
                    .setBg(bg)
                    .complete()
                    .setQrType(QrType.SVG)
                    .asFile(prefix + "/bq_txt.svg");
            System.out.println(ans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
