package com.hust.hui.quickmedia.test;

import com.hust.hui.quickmedia.web.wxapi.helper.ImgGenHelper;
import org.junit.Test;

/**
 * Created by yihui on 2017/9/24.
 */
public class ImgGenHelperTest {

    @Test
    public void testGenImg() {
        String ans = ImgGenHelper.genTmpImg("jpg");
        System.out.println(ans);
    }

}
