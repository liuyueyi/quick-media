package com.github.hui.quick.plugin.test.v3.svg;

import com.github.hui.quick.plugin.qrcode.v3.constants.QrType;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Before;
import org.junit.Test;

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

}
