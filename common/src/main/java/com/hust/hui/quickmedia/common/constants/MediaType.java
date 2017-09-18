package com.hust.hui.quickmedia.common.constants;

/**
 * 多媒体前缀方式: http://www.cnblogs.com/del/archive/2012/03/14/2395782.html
 *
 * Created by yihui on 2017/9/12.
 */
public enum MediaType {

    // http://www.garykessler.net/library/file_sigs.html
    ImageJpg("jpg", "image/jpeg", "FFD8FF", "data:image/jpeg;base64,"),

    ImageGif("gif", "image/gif", "47494638", "data:image/gif;base64,"),

    ImagePng("png", "image/png", "89504E47", "data:image/png;base64,"),

    ImageWebp("webp", "image/webp", "52494646", ""),


    // http://tools.ietf.org/html/rfc3003
    AudioMp3("mp3", "audio/mp3", "", "data:audio/x-mpeg;base64,"),

    AudioWav("wav", "audio/wav", "", "data:audio/wav;base64,"),

    AudioAmr("amr", "audio/amr", "", ""),


    VideoMp4("mp4", "video/mp4", "", "data:video/mp4;base64,"),

    VideoFlv("flv", "video/x-flv", "464C56", ""),

    VideoMov("mov", "video/quicktime", "", "data:video/quicktime;base64");


    /**
     * 后缀
     */
    private final String ext;

    /**
     * mime
     */
    private final String mime;

    /**
     * 魔数
     */
    private final String magic;


    /**
     * dom 显示 的前缀
     */
    private final String prefix;

    MediaType(String ext, String mime, String magic, String prefix) {
        this.ext = ext;
        this.mime = mime;
        this.magic = magic;
        this.prefix = prefix;
    }

    public String getExt() {
        return ext;
    }

    public String getMime() {
        return mime;
    }


    public String getMagic() {
        return magic;
    }

    public String getPrefix() {
        return prefix;
    }
}
