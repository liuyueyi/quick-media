package com.github.hui.quick.plugin.qrcode.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author YiHui
 * @date 2022/8/8
 */
public class ForEachUtil {
    /**
     * 二维数组遍历
     *
     * @param w
     * @param h
     * @param consumer
     * @param <T>
     */
    public static <T> void foreach(int w, int h, BiConsumer<Integer, Integer> consumer) {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                consumer.accept(x, y);
            }
        }
    }


    /**
     * 二维数组遍历
     *
     * @param w           第一层大小
     * @param h           第二层大小
     * @param consumer    内层迭代逻辑
     * @param outConsumer 外层迭代逻辑
     * @param <T>
     */
    public static <T> void foreach(int w, int h, BiConsumer<Integer, Integer> consumer, Consumer<Integer> outConsumer) {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                consumer.accept(x, y);
            }
            outConsumer.accept(x);
        }
    }
}
