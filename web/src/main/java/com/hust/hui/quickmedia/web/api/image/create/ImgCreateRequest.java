package com.hust.hui.quickmedia.web.api.image.create;

import com.hust.hui.quickmedia.common.image.ImgCreateOptions;
import com.hust.hui.quickmedia.web.entity.IRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by yihui on 2017/8/18.
 */
@Getter
@Setter
@ToString
public class ImgCreateRequest implements IRequest {


    private Integer w = 400;

    private Integer leftPadding = 10;

    private Integer topPadding = 20;

    private Integer bottomPadding = 20;

    private Integer linePadding = 5;

    /**
     * 背景色
     */
    private String bgColor = "0xffffffff";


    /**
     * 字体色
     */
    private String fontColor = "0xff000000";

    /**
     * 字体大小
     */
    private Integer fontSize = 18;

    /**
     * 字体
     */
    private String fontName = "宋体";


    /**
     * 对齐方式
     */
    private String style = ImgCreateOptions.AlignStyle.LEFT.name();


    /**
     * 待绘制的内容
     */
    private String contents;



    @Data
    public static class Infos {
        private List<ImgInfo> imgs;

        private List<ContentInfo> contents;
    }


    @Data
    public static class ImgInfo implements  IInfo {
        private String img;

        private int order;
    }

    @Data
    public static class ContentInfo implements IInfo {
        private String content;

        private int order;

        /**
         * 字体色
         */
        private String fontColor;

        /**
         * 字体大小
         */
        private Integer fontSize = 18;

        /**
         * 字体
         */
        private String fontName = "宋体";
    }


    public interface IInfo {
        int getOrder();
    }
}


