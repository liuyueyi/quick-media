package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.date.ChineseDateExtendTool;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * Created by yihui on 2018/3/26.
 */
public class ChineseDateTest {

    @Test
    public void testDate2Lunar() {
        LocalDateTime now  = LocalDateTime.now();
        System.out.println(now + ">>>" + ChineseDateExtendTool.getNowLunarDate());
    }

}
