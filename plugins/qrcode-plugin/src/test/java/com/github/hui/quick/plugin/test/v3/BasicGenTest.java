package com.github.hui.quick.plugin.test.v3;

import com.github.hui.quick.plugin.base.OSUtil;
import org.junit.Before;

/**
 * @author YiHui
 * @date 2022/8/23
 */
public class BasicGenTest {
    protected String prefix = "/tmp";
    protected static final String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";

    @Before
    public void init() {
        if (OSUtil.isWinOS()) {
            prefix = "d://quick-media";
        }
    }

}
