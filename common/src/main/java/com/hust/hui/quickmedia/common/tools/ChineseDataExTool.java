package com.hust.hui.quickmedia.common.tools;

import java.util.Calendar;

/**
 * Created by yihui on 2017/9/17.
 */
public class ChineseDataExTool {

    private static final String[] tianGan = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    private static final String[] diZhi = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};

    private static final String[] HANZI = {"壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};


    /**
     * @param lunarYear 农历年份
     * @return String of Ganzhi: 甲子年 Tiangan:甲乙丙丁戊己庚辛壬癸<br/>
     * Dizhi: 子丑寅卯辰巳无为申酉戌亥
     */
    public static String lunarYearToGanZhi(int lunarYear) {
        return tianGan[(lunarYear - 4) % 10] + diZhi[(lunarYear - 4) % 12] + "年";
    }


    /**
     * 月转中文
     *
     * @param month 月份
     * @return
     */
    public static String lunarMonthToChinese(int month, boolean isleap) {
        String prefix = isleap ? "润" : "";
        if (month <= 10) {
            return prefix + HANZI[month - 1] + "月";
        } else {
            return prefix + HANZI[9] + HANZI[month % 10 - 1] + "月";
        }
    }


    public static String lunarDayToChinese(int day) {
        if (day <= 10) {
            return "初" + HANZI[day - 1];
        } else {
            return HANZI[day / 10 - 1] + HANZI[day % 10 - 1];
        }
    }


    /**
     * 24小时的时间转为对应的农历说法
     *
     * @param hour [0, 23]
     * @return
     */
    public static String hourToChinese(int hour) {
        if (hour == 23) {
            return diZhi[0] + "时";
        } else {
            return diZhi[(hour + 1) / 2] + "时";
        }
    }


    /**
     * 阴历转字符串
     *
     * @param lunar
     * @return
     */
    public static String parseLunar(ChineseDataTool.Lunar lunar) {
        StringBuilder builder = new StringBuilder();
        builder.append(lunarYearToGanZhi(lunar.getLunarYear()));
        builder.append(lunarMonthToChinese(lunar.getLunarMonth(), lunar.isIsleap()));
        builder.append(lunarDayToChinese(lunar.getLunarDay()));
        return builder.toString();
    }


    /**
     * 获取当前的阴历日期
     *
     * @return
     */
    public static String getNowLunarDate(boolean containHour) {
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH) + 1; //第一个月从0开始，所以得到月份＋1
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);

        ChineseDataTool.Solar solar = new ChineseDataTool.Solar(year, month, day);
        ChineseDataTool.Lunar lunar = ChineseDataTool.solarToLunar(solar);

        if (containHour) {
            return parseLunar(lunar) + " " + hourToChinese(hour);
        } else {
            return parseLunar(lunar);
        }
    }


    public static String getNowLunarDate() {
        return getNowLunarDate(true);
    }

}
