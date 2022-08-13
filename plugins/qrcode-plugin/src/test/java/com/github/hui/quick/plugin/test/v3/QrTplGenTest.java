package com.github.hui.quick.plugin.test.v3;

import com.github.hui.quick.plugin.base.OSUtil;
import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import org.junit.Before;
import org.junit.Test;

/**
 * 模板渲染测试类
 *
 * @author YiHui
 * @date 2022/8/13
 */
public class QrTplGenTest {

    private String prefix = "/tmp";
    private static final String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";

    @Before
    public void init() {
        if (OSUtil.isWinOS()) {
            prefix = "d://quick-media";
        }
    }

    @Test
    public void imgTplTest() throws Exception {
        String content = FileReadUtil.readAll("tpl/flower.tpl");
        Boolean ans = QrCodeGenV3.of(msg).setW(700)
                .setDetectSpecial(true)
                .setImgTemplate(content).build()
                .asFile(prefix + "/flower.png");
        System.out.println("over");
    }
}
