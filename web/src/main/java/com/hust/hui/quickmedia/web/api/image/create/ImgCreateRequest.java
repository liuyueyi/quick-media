package com.hust.hui.quickmedia.web.api.image.create;

import com.hust.hui.quickmedia.common.image.ImgCreateOptions;
import com.hust.hui.quickmedia.web.entity.IRequest;
import com.hust.hui.quickmedia.web.entity.IValidate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by yihui on 2017/8/18.
 */
@Getter
@Setter
@ToString
public class ImgCreateRequest implements IRequest, IValidate {


    private Integer w;

    private Integer h;

    private Integer leftPadding = 10;

    private Integer rightPadding = 10;

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
     * 对齐方式, 居中，左对齐，右对齐，上对齐，下对齐
     * <p>
     * {@link ImgCreateOptions.AlignStyle#name}
     */
    private String style = ImgCreateOptions.AlignStyle.LEFT.name();


    /**
     * 待绘制的内容 为 {@link Infos} 对象的json串
     */
    private String contents;


    /**
     * 主题， 从上到下， 从左到右， 从右到左
     * <p>
     * {@link ImgCreateOptions.DrawStyle#name}
     */
    private String topic = ImgCreateOptions.DrawStyle.HORIZONTAL.name();


    @Override
    public boolean validate() {
        if (ImgCreateOptions.DrawStyle.HORIZONTAL.name().equalsIgnoreCase(topic)) {
            // 文本水平时，需要指定图片宽度
            if (w == null) {
                return false;
            }
        } else {
            // 文本竖排时，需要指定输出图片高度
            if (h == null) {
                return false;
            }
        }


        if (StringUtils.isBlank(contents)) { // 绘制内容不能为空
            return false;
        }

        return true;
    }


    @Data
    public static class Infos {
        private List<ImgInfo> imgs;

        private List<ContentInfo> contents;
    }


    @Data
    public static class ImgInfo implements IInfo {
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


