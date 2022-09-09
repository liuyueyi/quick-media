package com.github.hui.quick.plugin.qrcode.v3.constants;

/**
 * 图片类型
 *
 * @author yihui
 * @date 2022/9/9
 */
public enum PicTypeEnum implements PicType {
    JPG("jpg"),
    PNG("png"),
    GIF("gif"),
    ;

    private String type;

    PicTypeEnum(String type) {
        this.type = type;
    }

    public  static boolean isJpg(String type) {
        return "jpg".equalsIgnoreCase(type) || "jpeg".equals(type);
    }

    public String getType() {
        return type;
    }
}