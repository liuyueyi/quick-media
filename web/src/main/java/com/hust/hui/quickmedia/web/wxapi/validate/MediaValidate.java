package com.hust.hui.quickmedia.web.wxapi.validate;

import com.hust.hui.quickmedia.common.constants.MediaType;

/**
 * Created by yihui on 2017/9/25.
 */
public class MediaValidate {

    private static final MediaType[] STATIC_IMG_TYPE = new MediaType[]{MediaType.ImagePng, MediaType.ImageJpg, MediaType.ImageWebp};
    private static final MediaType[] DYNAMIC_IMG_TYPE = new MediaType[]{MediaType.ImageGif};

    public static boolean validateImg(String surfix) {
        return validateStaticImg(surfix) && validateDynamicImg(surfix);
    }

    public static boolean validateStaticImg(String surfix) {
        if ("jpeg".equals(surfix)) {
            return true;
        }

        for(MediaType type: STATIC_IMG_TYPE) {
            if (type.getExt().equals(surfix)) {
                return true;
            }
        }

        return false;
    }


    public static boolean validateDynamicImg(String surfix) {
        for (MediaType type: DYNAMIC_IMG_TYPE) {
            if(type.getExt().equals(surfix)) {
                return true;
            }
        }

        return false;
    }
}
