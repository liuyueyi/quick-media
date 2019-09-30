package com.github.hui.media.console.validate;


import com.github.hui.quick.plugin.base.constants.MediaType;

/**
 * Created by yihui on 2017/9/25.
 */
public class MediaValidate {

    private static final MediaType[] STATIC_IMG_TYPE = new MediaType[]{MediaType.ImagePng, MediaType.ImageJpg, MediaType.ImageWebp};
    private static final MediaType[] DYNAMIC_IMG_TYPE = new MediaType[]{MediaType.ImageGif};

    public static boolean validateImg(String mime) {
        return validateStaticImg(mime) && validateDynamicImg(mime);
    }

    public static boolean validateStaticImg(String mime) {
        if (mime.contains("jpg")) {
            mime = mime.replace("jpg", "jpeg");
        }

        for(MediaType type: STATIC_IMG_TYPE) {
            if (type.getMime().equals(mime)) {
                return true;
            }
        }

        return false;
    }


    public static boolean validateDynamicImg(String mime) {
        for (MediaType type: DYNAMIC_IMG_TYPE) {
            if(type.getMime().equals(mime)) {
                return true;
            }
        }

        return false;
    }
}
