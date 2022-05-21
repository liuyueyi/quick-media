package com.github.hui.quick.plugin.image.wrapper.pixel.context;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yihui
 * @date 21/11/22
 */
public class PixelContextHolder {

    private static final ThreadLocal<ImgPixelChar> charCache = new ThreadLocal<>();

    public static void newPic() {
        ImgPixelChar imgPixelChar = charCache.get();
        if (imgPixelChar == null) {
            // 首张图
            charCache.set(new ImgPixelChar(new AtomicInteger()));
        } else {
            // 下一张图
            ImgPixelChar newImg = new ImgPixelChar(imgPixelChar.seqCount);
            newImg.pre = imgPixelChar;
            charCache.set(newImg);
        }
    }

    public static void addChar(int y, char ch) {
        ImgPixelChar imgPixelChar = charCache.get();
        if (imgPixelChar == null) {
            imgPixelChar = new ImgPixelChar(new AtomicInteger());
            charCache.set(imgPixelChar);
        }
        imgPixelChar.addChar(y, ch);
    }

    public static void clear() {
        charCache.remove();
    }

    public static ImgPixelChar getPixelChar() {
        return charCache.get();
    }

    /**
     * 输出字符数组
     *
     * @return
     */
    public static List<List<String>> toPixelChars(ImgPixelChar imgPixelChar) {
        List<List<String>> list = new ArrayList<>();
        while (imgPixelChar != null) {
            list.add(imgPixelChar.toPixelChars());
            imgPixelChar = imgPixelChar.pre;
        }

        Collections.reverse(list);
        return list;
    }


    public static class ImgPixelChar {
        private final AtomicInteger seqCount;
        /**
         * key: y
         * value: y轴对应的字符
         */
        private final Map<Integer, StringBuilder> charCache;

        private ImgPixelChar pre;

        public ImgPixelChar(AtomicInteger seqCount) {
            charCache = new TreeMap<>();
            this.seqCount = seqCount;
        }

        public void addChar(int y, char ch) {
            charCache.computeIfAbsent(y, (s) -> new StringBuilder()).append(ch);
        }

        public int getAndUpdateSeqIndex(int maxIndex) {
            int index = seqCount.getAndAdd(1);
            if (index >= maxIndex) {
                index = 0;
                seqCount.set(1);
            }
            return index;
        }

        public List<String> toPixelChars() {
            List<String> list = new ArrayList<>();
            for (StringBuilder builder : charCache.values()) {
                list.add(builder.toString());
            }
            return list;
        }

        public void setPre(ImgPixelChar pre) {
            this.pre = pre;
        }
    }
}
