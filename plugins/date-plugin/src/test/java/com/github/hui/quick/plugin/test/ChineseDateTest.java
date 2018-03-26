package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.date.ChineseDateExtendTool;
import org.junit.Test;

/**
 * Created by yihui on 2018/3/26.
 */
public class ChineseDateTest {

    @Test
    public void testDate2Lunar() {
        System.out.println(ChineseDateExtendTool.getNowLunarDate());
    }

}
