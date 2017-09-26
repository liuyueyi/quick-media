package com.hust.hui.quickmedia.web.wxapi.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 2017/9/25.
 */
public enum WxEmotionCreateTemplateEnum {

    GIF_SFONT("gif图 + 静态文字"),

    MIMG_SFONT("多张静态图 + 静态文字"),


    IMG_DFONT_LINE("静态图 + 逐行显示文字"),


    IMG_DFONT_WORD("静态图 + 逐字显示"),


    IMG_DFONT_DIALOGUE("静态图 + 对话");;

    private String desc;


    private static Map<String, WxEmotionCreateTemplateEnum> map;

    static {
        map = new HashMap<>();
        for (WxEmotionCreateTemplateEnum e : values()) {
            map.put(e.name(), e);
        }
    }


    WxEmotionCreateTemplateEnum(String desc) {
        this.desc = desc;
    }


    public static WxEmotionCreateTemplateEnum getTemplate(String id) {
        if (id == null) {
            return GIF_SFONT;
        }


        id = id.toUpperCase();
        WxEmotionCreateTemplateEnum e = map.get(id);
        return e == null ? GIF_SFONT : e;
    }
}
