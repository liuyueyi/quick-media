package com.github.hui.quick.plugin.image.util;

import java.util.function.Predicate;

/**
 * @author yihui
 * @date 2021/11/12
 */
public class PredicateUtil {
    public static <T> T conditionGetOrElse(Predicate<T> predicate, T val, T defaultVal) {
        return predicate.test(val) ? val : defaultVal;
    }
}
