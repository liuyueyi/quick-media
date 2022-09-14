package com.github.hui.quick.plugin.test.v3.img;

import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.QrType;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Test;

import java.awt.*;

/**
 * 动图测试
 *
 * @author YiHui
 * @date 2022/9/14
 */
public class QrImgGifGenTest extends BasicGenTest {

    @Override
    public void init() {
        super.init();
        this.prefix += "/img";
    }

    /**
     * 背景作为动图的场景
     */
    @Test
    public void bgGif() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .newBgOptions()
                .setBg(new QrResource().setGif("gifs/bgOverride.gif"))
                .setOpacity(0.6f)
                .complete()
                // 指定输出gif格式二维码，默认保存为图片的时候，可以省略；
                // 一般建议具体指定生成二维码的类型更合适
                .setQrType(QrType.GIF)
                .asFile(prefix + "/gif_bg.gif");
        System.out.println(ans);
    }

    @Test
    public void bgGifFill() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(400)
                // 说明：指定背景图为gif时，也可以直接使用 setImg, 但是这种方式请注意，文件名务必是 gif 结尾
                // 官方更推荐的是使用 setGif 来指定gif资源图，避免文件名后缀不对导致无法正确识别为gif
                .setBgResource(new QrResource().setImg("gifs/bgFill.gif"))
                .setBgStyle(BgStyle.FILL)
                .setBgX(20).setBgY(137)
                .asFile(prefix + "/gif_bg_fill.gif");
        System.out.println(ans);
    }

    /**
     * 使用一张动态的颜色渐变的背景图，从而实现二维码颜色变化的场景
     *
     * @throws Exception
     */
    @Test
    public void bgGifPenetrate() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setBgResource(new QrResource().setImg("gifs/bgPen.gif"))
                .setBgW(500).setBgH(500)
                .setBgStyle(BgStyle.PENETRATE)
                .asFile(prefix + "/gif_bg_penetrate.gif");
        System.out.println(ans);
    }


    /**
     * 借助前置图实现一个gif的logo效果图
     *
     * @throws Exception
     */
    @Test
    public void ftGif() throws Exception {
        int size = 500;
        int ftSize = size / 4;
        boolean ans = QrCodeGenV3.of(msg)
                .setSize(size)
                .clearLogoArea(true)
                .setFtResource(new QrResource().setImg("gifs/preQie.gif"))
                .setFtW(ftSize).setFtH(ftSize)
                .setFtX((ftSize - size) / 2)
                .setFtY((ftSize - size) / 2)
                .asFile(prefix + "/gif_ft_logo.gif");
        System.out.println(ans);
    }


    @Test
    public void ftGifV2() throws Exception {
        boolean ans = QrCodeGenV3.of(msg)
                .setSize(500)
                .setPreColor(0xff73a7f5)
                .setFtResource(new QrResource().setImg("gifs/pre.gif"))
                .setFtX(-50)
                .setFtY(-120)
                .setFtFillColor(Color.WHITE)
                .asFile(prefix + "/gif_ft.gif");
        System.out.println(ans);
    }
}
