package com.github.hui.quick.plugin.test.v3;

import com.github.hui.quick.plugin.base.OSUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

/**
 * svg 格式二维码输出测试
 *
 * @author YiHui
 * @date 2022/8/5
 */
public class QrSvgGenTest {

    private String prefix = "/tmp";
    private static final String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";

    @Before
    public void init() {
        if (OSUtil.isWinOS()) {
            prefix = "d://quick-media";
        }
    }

    @Test
    public void basicTest() throws Exception {
        String svg = QrCodeGenV3.of(msg).build().asSvg();
        System.out.println(svg);
    }

    @Test
    public void basicStarSvg() throws Exception {
        String svg = QrCodeGenV3.of(msg).setW(500)
                .newDrawOptions().setDrawStyle(DrawStyle.STAR).setPreColor(Color.RED).complete().build()
                .asSvg();
        System.out.println(svg);
    }

    /**
     * 圆角矩形的二维码
     *
     * @throws Exception
     */
    @Test
    public void roundRectSvg() throws Exception {
        String svg = QrCodeGenV3.of(msg).setW(500)
                .setDrawStyle(DrawStyle.ROUND_RECT)
                .setPreColor(Color.ORANGE)
                .setBgColor(Color.LIGHT_GRAY)
                .build()
                .asSvg();
        System.out.println(svg);
    }

    @Test
    public void symbolSvg() throws Exception {
        boolean svg = QrCodeGenV3.of(msg).setW(500)
                .newDrawOptions()
                .setDrawStyle(DrawStyle.SVG)
                .newRenderResource(1, 1, new QrResource().setSvg(" <symbol id=\"symbol_1R\" viewBox=\"0 0 50 50\">\n" +
                        "        <circle cx=\"25\" cy=\"25\" r=\"11.5\" style=\"fill: #F98E00\"/>\n" +
                        "    </symbol>")).addResource(new QrResource().setSvg("<symbol id=\"symbol_1X\" viewBox=\"0 0 50 50\">\n" +
                        "        <path d=\"M42.82,21.81C42.82,11.98,34.84,4,25,4S7.19,11.98,7.19,21.81L5.5,44l8.03-4.37L19.3,46l5.7-6.37L30.7,46  l5.78-6.37L44.5,44L42.82,21.81z M18.46,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03s5.76,2.7,5.76,6.03  C24.22,24.48,21.64,27.18,18.46,27.18z M31.54,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03  c3.18,0,5.76,2.7,5.76,6.03C37.3,24.48,34.72,27.18,31.54,27.18z\"\n" +
                        "              style=\"fill: #F98E00\"/>\n" +
                        "        <ellipse cx=\"20.76\" cy=\"22.13\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "        <ellipse cx=\"33.51\" cy=\"21.8\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "    </symbol>")).addResource(new QrResource().setSvg("<symbol id=\"symbol_1W\" viewBox=\"0 0 50 50\">\n" +
                        "        <path d=\"M42.82,21.81C42.82,11.98,34.84,4,25,4S7.19,11.98,7.19,21.81L5.5,44l8.03-4.37L19.3,46l5.7-6.37L30.7,46  l5.78-6.37L44.5,44L42.82,21.81z M18.46,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03s5.76,2.7,5.76,6.03  C24.22,24.48,21.64,27.18,18.46,27.18z M31.54,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03  c3.18,0,5.76,2.7,5.76,6.03C37.3,24.48,34.72,27.18,31.54,27.18z\"\n" +
                        "              style=\"fill: #40C4ED\"/>\n" +
                        "        <ellipse cx=\"18.63\" cy=\"22.66\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "        <ellipse cx=\"31.37\" cy=\"22.34\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "    </symbol>")).addResource(new QrResource().setSvg("<symbol id=\"symbol_1V\" viewBox=\"0 0 50 50\">\n" +
                        "        <path d=\"M42.82,21.81C42.82,11.98,34.84,4,25,4S7.19,11.98,7.19,21.81L5.5,44l8.03-4.37L19.3,46l5.7-6.37L30.7,46  l5.78-6.37L44.5,44L42.82,21.81z M18.46,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03s5.76,2.7,5.76,6.03  C24.22,24.48,21.64,27.18,18.46,27.18z M31.54,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03  c3.18,0,5.76,2.7,5.76,6.03C37.3,24.48,34.72,27.18,31.54,27.18z\"\n" +
                        "              style=\"fill: #7BB72E\"/>\n" +
                        "        <ellipse cx=\"16.63\" cy=\"22.06\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "        <ellipse cx=\"29.37\" cy=\"21.74\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "    </symbol>")).build()
                .addSource(2, 1, new QrResource().setSvg("<symbol id=\"symbol_1T\" viewBox=\"0 0 100 50\">\n" +
                        "        <line x1=\"17\" y1=\"25\" x2=\"83\" y2=\"25\"\n" +
                        "              style=\"fill: none; stroke: #333333; stroke-width: 20; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
                        "    </symbol>")).addResource(new QrResource().setSvg(" <symbol id=\"symbol_1U\" viewBox=\"0 0 100 50\">\n" +
                        "        <circle cx=\"25\" cy=\"25\" r=\"11.5\" style=\"fill: #F98E00\"/>\n" +
                        "        <circle cx=\"75\" cy=\"25\" r=\"11.5\" style=\"fill: #F98E00\"/>\n" +
                        "    </symbol>")).build()
                .addSource(1, 3, new QrResource().setSvg("<symbol id=\"symbol_1S\" viewBox=\"0 0 50 150\">\n" +
                        "    <line x1=\"25\" y1=\"20\" x2=\"25\" y2=\"130\"\n" +
                        "              style=\"fill: none; stroke: #333333; stroke-width: 20; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
                        "    </symbol>")).build().over()
                .complete()
                .newDetectOptions()
                .setLt(new QrResource().setSvg("<symbol id=\"detect_lt\" viewBox=\"0 0 49 49\">\n" +
                        "        <path d=\"M45.5,42.34c0,1.75-1.38,3.16-3.08,3.16H6.59c-1.7,0-3.09-1.42-3.09-3.16V6.66c0-1.75,1.38-3.16,3.09-3.16h35.83c1.7,0,3.08,1.42,3.08,3.16V42.34z\"\n" +
                        "              style=\"fill: none; stroke: #333333; stroke-width: 7; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
                        "        <path d=\"M35,31c0,2.21-1.79,4-4,4H18c-2.21,0-4-1.79-4-4V18c0-2.21,1.79-4,4-4h13c2.21,0,4,1.79,4,4V31z\"\n" +
                        "              style=\"fill: #333333\"/>\n" +
                        "    </symbol>"))
                .setLd(new QrResource().setSvg("<symbol id=\"detect_ld\" viewBox=\"0 0 49 49\">\n" +
                        "        <path d=\"M45.5,42.34c0,1.75-1.38,3.16-3.08,3.16H6.59c-1.7,0-3.09-1.42-3.09-3.16V6.66c0-1.75,1.38-3.16,3.09-3.16h35.83c1.7,0,3.08,1.42,3.08,3.16V42.34z\"\n" +
                        "              style=\"fill: none; stroke: #333333; stroke-width: 7; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
                        "        <path d=\"M35,31c0,2.21-1.79,4-4,4H18c-2.21,0-4-1.79-4-4V18c0-2.21,1.79-4,4-4h13c2.21,0,4,1.79,4,4V31z\"\n" +
                        "              style=\"fill: #333333\"/>\n" +
                        "    </symbol>"))
                .setRt(new QrResource().setSvg(" <symbol id=\"detect_rt\" viewBox=\"0 0 49 49\">\n" +
                        "        <path d=\"M45.5,42.34c0,1.75-1.38,3.16-3.08,3.16H6.59c-1.7,0-3.09-1.42-3.09-3.16V6.66c0-1.75,1.38-3.16,3.09-3.16h35.83c1.7,0,3.08,1.42,3.08,3.16V42.34z\"\n" +
                        "              style=\"fill: none; stroke: #333333; stroke-width: 7; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
                        "        <path d=\"M35,31c0,2.21-1.79,4-4,4H18c-2.21,0-4-1.79-4-4V18c0-2.21,1.79-4,4-4h13c2.21,0,4,1.79,4,4V31z\"\n" +
                        "              style=\"fill: #333333\"/>\n" +
                        "    </symbol>"))
                .complete()
                .build()
                .asFile(prefix + "/qr.svg");
        System.out.println(svg);
    }

    @Test
    public void symbolSvgV2() throws Exception {
        boolean svg = QrCodeGenV3.of(msg).setW(500)
                .newDrawOptions()
                .setDrawStyle(DrawStyle.SVG)
                .newRenderResource(1, 1, new QrResource().setSvg(" <symbol id=\"symbol_1R\" viewBox=\"0 0 50 50\">\n" +
                        "        <circle cx=\"25\" cy=\"25\" r=\"11.5\" style=\"fill: #F98E00\"/>\n" +
                        "    </symbol>")).addResource(new QrResource().setSvg("<symbol id=\"symbol_1X\" viewBox=\"0 0 50 50\">\n" +
                        "        <path d=\"M42.82,21.81C42.82,11.98,34.84,4,25,4S7.19,11.98,7.19,21.81L5.5,44l8.03-4.37L19.3,46l5.7-6.37L30.7,46  l5.78-6.37L44.5,44L42.82,21.81z M18.46,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03s5.76,2.7,5.76,6.03  C24.22,24.48,21.64,27.18,18.46,27.18z M31.54,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03  c3.18,0,5.76,2.7,5.76,6.03C37.3,24.48,34.72,27.18,31.54,27.18z\"\n" +
                        "              style=\"fill: #F98E00\"/>\n" +
                        "        <ellipse cx=\"20.76\" cy=\"22.13\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "        <ellipse cx=\"33.51\" cy=\"21.8\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "    </symbol>")).addResource(new QrResource().setSvg("<symbol id=\"symbol_1W\" viewBox=\"0 0 50 50\">\n" +
                        "        <path d=\"M42.82,21.81C42.82,11.98,34.84,4,25,4S7.19,11.98,7.19,21.81L5.5,44l8.03-4.37L19.3,46l5.7-6.37L30.7,46  l5.78-6.37L44.5,44L42.82,21.81z M18.46,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03s5.76,2.7,5.76,6.03  C24.22,24.48,21.64,27.18,18.46,27.18z M31.54,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03  c3.18,0,5.76,2.7,5.76,6.03C37.3,24.48,34.72,27.18,31.54,27.18z\"\n" +
                        "              style=\"fill: #40C4ED\"/>\n" +
                        "        <ellipse cx=\"18.63\" cy=\"22.66\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "        <ellipse cx=\"31.37\" cy=\"22.34\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "    </symbol>")).addResource(new QrResource().setSvg("<symbol id=\"symbol_1V\" viewBox=\"0 0 50 50\">\n" +
                        "        <path d=\"M42.82,21.81C42.82,11.98,34.84,4,25,4S7.19,11.98,7.19,21.81L5.5,44l8.03-4.37L19.3,46l5.7-6.37L30.7,46  l5.78-6.37L44.5,44L42.82,21.81z M18.46,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03s5.76,2.7,5.76,6.03  C24.22,24.48,21.64,27.18,18.46,27.18z M31.54,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03  c3.18,0,5.76,2.7,5.76,6.03C37.3,24.48,34.72,27.18,31.54,27.18z\"\n" +
                        "              style=\"fill: #7BB72E\"/>\n" +
                        "        <ellipse cx=\"16.63\" cy=\"22.06\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "        <ellipse cx=\"29.37\" cy=\"21.74\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
                        "    </symbol>")).build()
                .addSource(2, 1, new QrResource().setSvg("<symbol id=\"symbol_1T\" viewBox=\"0 0 100 50\">\n" +
                        "        <line x1=\"17\" y1=\"25\" x2=\"83\" y2=\"25\"\n" +
                        "              style=\"fill: none; stroke: #333333; stroke-width: 20; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
                        "    </symbol>")).addResource(new QrResource().setSvg(" <symbol id=\"symbol_1U\" viewBox=\"0 0 100 50\">\n" +
                        "        <circle cx=\"25\" cy=\"25\" r=\"11.5\" style=\"fill: #F98E00\"/>\n" +
                        "        <circle cx=\"75\" cy=\"25\" r=\"11.5\" style=\"fill: #F98E00\"/>\n" +
                        "    </symbol>")).build()
                .addSource(1, 3, new QrResource().setSvg("<symbol id=\"symbol_1S\" viewBox=\"0 0 50 150\">\n" +
                        "    <line x1=\"25\" y1=\"20\" x2=\"25\" y2=\"130\"\n" +
                        "              style=\"fill: none; stroke: #333333; stroke-width: 20; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
                        "    </symbol>")).build().over()
                .complete()
                .setDetectSpecial(true)
                .build()
                .asFile(prefix + "/qr2.svg");
        System.out.println(svg);
    }
}
