package com.github.hui.media.console.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by @author yihui in 18:03 19/8/15.
 */
public class LocalDateTimeUtil {

    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HH:mm");

    public static String getCurrentDateTime() {
        return getCurrentDateTime(DEFAULT_FORMATTER);
    }

    public static String getCurrentDateTime(DateTimeFormatter formatter) {
        return LocalDateTime.now().format(formatter);
    }

}
