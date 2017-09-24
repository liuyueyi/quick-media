package com.hust.hui.quickmedia.web.wxapi.helper;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * Created by yihui on 2017/9/24.
 */
public class ImgGenHelper {

    public static final String TMP_PATH = "/mydata/html/story/public/ximg/genimg/";


    private static String getTime() {
        return DateFormatUtils.format(new Date(), "yyyyMMdd_HHmm");
    }

    public static String genTmpImg(String type) {
        String time = System.currentTimeMillis() + "_" + ((int) (Math.random() * 100));
        return getTime() + "/" + time + "." + type;
    }
}
