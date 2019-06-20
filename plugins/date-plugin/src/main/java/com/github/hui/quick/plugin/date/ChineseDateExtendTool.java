package com.github.hui.quick.plugin.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by yihui on 2017/9/17.
 */
public class ChineseDateExtendTool {

    private static final String[] TIAN_GAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    private static final String[] DI_ZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};

    private static final String[] HAN_ZI = {"壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};


    /**
     * @param lunarYear 农历年份
     * @return String of Ganzhi: 甲子年 Tiangan:甲乙丙丁戊己庚辛壬癸<br/>
     * Dizhi: 子丑寅卯辰巳无为申酉戌亥
     */
    public static String lunarYearToGanZhi(int lunarYear) {
        return TIAN_GAN[(lunarYear - 4) % 10] + DI_ZHI[(lunarYear - 4) % 12] + "年";
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
            return prefix + HAN_ZI[month - 1] + "月";
        } else {
            return prefix + HAN_ZI[9] + HAN_ZI[month % 10 - 1] + "月";
        }
    }


    public static String lunarDayToChinese(int day) {
        if (day <= 10) {
            return "初" + HAN_ZI[day - 1];
        } else if (day == 20) {
            return "贰拾";
        } else if (day == 30) {
            return "叁拾";
        } else {
            return HAN_ZI[day / 10 - 1] + HAN_ZI[day % 10 - 1];
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
            return DI_ZHI[0] + "时";
        } else {
            return DI_ZHI[(hour + 1) / 2] + "时";
        }
    }


    /**
     * 阴历转字符串
     *
     * @param lunar
     * @return
     */
    public static String parseLunar(ChineseDateTool.Lunar lunar) {
        StringBuilder builder = new StringBuilder();
        builder.append(lunarYearToGanZhi(lunar.getLunarYear()));
        builder.append(lunarMonthToChinese(lunar.getLunarMonth(), lunar.isIsleap()));
        builder.append(lunarDayToChinese(lunar.getLunarDay()));
        return builder.toString();
    }


    /**
     * 根据当前时间，转换为天干地支计时
     *
     * @return
     */
    public static String getNowLunarDate(LocalDateTime localDateTime, boolean containHour) {
        ChineseDateTool.Solar solar = new ChineseDateTool.Solar(localDateTime.getYear(), localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth());
        ChineseDateTool.Lunar lunar = ChineseDateTool.solarToLunar(solar);

        if (containHour) {
            return parseLunar(lunar) + " " + hourToChinese(localDateTime.getHour());
        } else {
            return parseLunar(lunar);
        }
    }

    /**
     * 将当前时间转为生辰八字方式
     *
     * @return
     */
    public static String getNowLunarDate() {
        return getNowLunarDate(LocalDateTime.now(), true);
    }

    /**
     * 根据ms时间，转为对应的天干地支计时
     *
     * @param timestamp
     * @return
     */
    public static String getLunarDateByTimestamp(long timestamp) {
        LocalDateTime time = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return getNowLunarDate(time, true);
    }

}
