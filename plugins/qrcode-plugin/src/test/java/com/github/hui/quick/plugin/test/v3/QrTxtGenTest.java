package com.github.hui.quick.plugin.test.v3;

import com.github.hui.quick.plugin.base.OSUtil;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Before;
import org.junit.Test;

/**
 * @author YiHui
 * @date 2022/8/2
 */
public class QrTxtGenTest {

    private String prefix = "/tmp";
    private static final String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";

    @Before
    public void init() {
        if (OSUtil.isWinOS()) {
            prefix = "d://quick-media";
        }
    }

    /**
     * 输出文字版本的二维码
     *
     * @throws Exception
     */
    @Test
    public void testGen() throws Exception {
        String txt = QrCodeGenV3.of(msg).setSize(10).setErrorCorrection(ErrorCorrectionLevel.L).build().asTxt();
        System.out.println(txt);
    }

}
