package com.hust.hui.quickmedia.common.util;

/**
 * 多媒体前缀方式: http://www.cnblogs.com/del/archive/2012/03/14/2395782.html
 *
 * Created by yihui on 2017/8/18.
 */
public class DomUtil {

    public enum DomType {
        IMG_PNG("data:image/png;base64,"),
        IMG_JPG("data:image/jpeg;base64,"),



        AUDIO_MP3("data:audio/x-mpeg;base64,"),
        AUDIO_WAV("data:audio/wav;base64,"),
        ;


        private String prefix;

        DomType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }



    /**
     * 将base64数据封装为html对应的dom属性值
     *
     * @param base64str
     * @return
     */
    public static String toDomSrc(String base64str, DomType domType) {
        return  domType.getPrefix() + base64str;
    }
}
