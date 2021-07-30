package com.github.hui.quick.plugin.test.fix;

import com.github.hui.quick.plugin.base.OSUtil;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.google.zxing.WriterException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author yihui
 * @date 2021/3/2
 */
public class Issue88 {
    private String prefix = "/tmp";

    @Before
    public void init() {
        if (OSUtil.isWinOS()) {
            prefix = "c://quick-media";
        }
    }

    /**
     * 白边修复导致logo定位问题
     *
     * @throws IOException
     * @throws WriterException
     */
    @Test
    public void testLogoErrPosition() throws IOException, WriterException {
        String logo = "logo.jpg";
        String msg = "{\"type\":\"1\",\"data\":\"Bv1EReUAm3T4yG2sMvM0tJzyd5TQccWdd9e5j4cjiCHIEi6S1DHyFvpabpQghmkkmhynmc/6bQoRpw6zhPK1vNZDMPJAaSo7SirP68rkp0sUAHzilB0Vqpf6AqxA0JCXvusgbo3dSDMg9oLoHxQzBVltIB/I70SAHv28aXpQBa/Sc8VB38BA6sjv+2ygBHJIgTuWZEdruAm7hQZFGbPUQuW6HdEdSVWfSPpY55i/AQS/GfWVcBMXtMVYBhzF9JpLN78ZgXywF5Z9CUC57okArJIafSqYC9mtGcsHiV0YvlckxAr1cXthFr3ijfQA6eqmXw5\"}";
        QrCodeGenWrapper.of(msg)
                .setW(300)
                .setLogo(logo)
                // 圆形logo支持
//                .setLogoStyle(QrCodeOptions.LogoStyle.CIRCLE)
                .setLogoBgColor(0xfffefefe)
                .setLogoBorderBgColor(0xffc7c7c7)
                .setLogoBorder(true)
                .asFile(prefix + "/88.png");
    }

}
