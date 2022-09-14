package com.github.hui.quick.plugin.test.v3.img;

import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 有背景图的二维码使用姿势
 *
 * @author YiHui
 * @date 2022/9/13
 */
public class QrImgBgGenTest extends BasicGenTest {
    @Override
    public void init() {
        super.init();
        this.prefix += "/img";
    }

    /**
     * 默认的背景图方式
     * - 全覆盖方式
     * - 二维码指定透明度
     * - 一般期望能是背景图与生成的二维码图大小相同
     */
    @Test
    public void defaultBg() {
        try {
            // 可以直接加载网络图片，为了避免网络资源丢失，原图下载到测试资源目录下
//            String bg = "http://ww1.sinaimg.cn/large/8154e929gy1g8vho8x6r0j20b40b43yl.jpg";
            String bg = "bgs/xjs.jpg";
            boolean ans = QrCodeGenV3.of(msg).setSize(500)
                    .newBgOptions()
                    .setBg(new QrResource().setImg(bg))
                    .setOpacity(0.5f)
                    .complete()
                    .asFile(prefix + "/bq_default.png");
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
                .asFile(prefix + "/bg_fill.png");
        System.out.println(ans);
    }

    /**
     * 信息点透明，使用背景图的颜色进行渲染
     *
     * @throws Exception
     */
    @Test
    public void penetrateBg() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(600)
                .newBgOptions()
                .setBg(new QrResource().setImg("bgs/color.jpg"))
                .setBgW(600).setBgH(600)
                .setBgStyle(BgStyle.PENETRATE)
                .complete()
                .asFile(prefix + "/bg_penetrate.png");
        System.out.println(ans);
    }


    /**
     * 文字背景 + 穿透模式，实现文字二维码的效果
     *
     * @throws Exception
     */
    @Test
    public void TxtBg() throws Exception {
        int size = 500;
        BufferedImage bgImg = GraphicUtil.createImg(500, 500, null);
        Graphics2D g2d = GraphicUtil.getG2d(bgImg);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, 500, 500);

        Font font = new Font("宋体", Font.BOLD, 500);
        g2d.setFont(font);
        g2d.setColor(Color.RED);

        int y = size - (size - g2d.getFontMetrics().getDescent() - g2d.getFontMetrics().getAscent()) / 2 - g2d.getFontMetrics().getDescent();
        g2d.drawString("码", 0, y);
        g2d.dispose();

        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .newBgOptions()
                .setBg(new QrResource().setImg(bgImg))
                .setBgStyle(BgStyle.PENETRATE)
                .complete()
                .asFile(prefix + "/bg_txt.png");
        System.out.println(ans);
    }

    /**
     * 背景图圆角处理
     *
     * @throws Exception
     */
    @Test
    public void roundBg() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setBgResource(new QrResource().setImg("bgs/xjs.jpg").setPicStyle(PicStyle.ROUND))
                .setBgOpacity(0.5f)
                .asFile(prefix + "/bg_round.png");
        System.out.println(ans);
    }

    @Test
    public void circleBg() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(550)
                .setBgResource(new QrResource().setImg("bgs/qrbg.jpg").setPicStyle(PicStyle.CIRCLE))
                .setBgStyle(BgStyle.FILL)
                .setBgX(225)
                .setBgY(320)
                .asFile(prefix + "/bg_circle.png");
        System.out.println(ans);
    }
}
