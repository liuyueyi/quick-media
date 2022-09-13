package com.github.hui.quick.plugin.test.v3.img;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Test;

import java.awt.*;

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

    @Test
    public void fillBg() throws Exception{
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
}
