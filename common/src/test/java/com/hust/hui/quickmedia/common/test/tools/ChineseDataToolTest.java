package com.hust.hui.quickmedia.common.test.tools;

import com.hust.hui.quickmedia.common.tools.ChineseDataExTool;
import com.hust.hui.quickmedia.common.tools.ChineseDataTool;
import org.junit.Test;

/**
 * Created by yihui on 2017/9/17.
 */
public class ChineseDataToolTest {

    @Test
    public  void testParse() {
        // 公历
        ChineseDataTool.Solar solar = new ChineseDataTool.Solar();
        solar.setSolarYear(2017);
        solar.setSolarMonth(8);
        solar.setSolarDay(17);

        ChineseDataTool.Lunar lunar = ChineseDataTool.solarToLunar(solar);
        System.out.println(lunar);



        // 农历
        ChineseDataTool.Lunar l = new ChineseDataTool.Lunar();
        l.setIsleap(false);
        l.setLunarDay(12);
        l.setLunarMonth(7);
        l.setLunarYear(2017);
        solar = ChineseDataTool.lunarToSolar(l);
        System.out.println(solar);
    }


    @Test
    public void testToStr() {
        ChineseDataTool.Lunar lunar = new ChineseDataTool.Lunar(false, 2017, 7, 27);
        String str = ChineseDataExTool.parseLunar(lunar);
        System.out.println(str);


        System.out.println(ChineseDataExTool.hourToChinese(7));


        System.out.println(ChineseDataExTool.getNowLunarDate());
    }

}
