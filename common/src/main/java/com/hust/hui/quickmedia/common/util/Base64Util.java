package com.hust.hui.quickmedia.common.util;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * Created by yihui on 2017/7/17.
 */
public class Base64Util {


    public static String encode(ByteArrayOutputStream outputStream) {
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

}
