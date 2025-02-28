package com.github.hui.quick.plugin.image.wrapper.pixel.context;

import com.github.hui.quick.plugin.image.util.CIEDE2000;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
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

    public static ImgPixelChar addChar(int y, char ch) {
        ImgPixelChar imgPixelChar = charCache.get();
        if (imgPixelChar == null) {
            imgPixelChar = new ImgPixelChar(new AtomicInteger());
            charCache.set(imgPixelChar);
        }
        imgPixelChar.addChar(y, ch);
        return imgPixelChar;
    }

    public static void addChar(int y, char ch, Color color) {
        ImgPixelChar imgPixelChar = addChar(y, ch);
        imgPixelChar.putColor(ch + "", color);
    }

    public static String putColor(Color color, float threshold) {
        ImgPixelChar imgPixelChar = getPixelChar();
        if (true) {
            // 颜色范围匹配
            for (Map.Entry<String, Color> entry : imgPixelChar.charColorMap.entrySet()) {
                if (CIEDE2000.calculateCIEDE2000(color, entry.getValue()) <= threshold) {
                    return entry.getKey();
                }
            }
            // 没有的场景
            int index = imgPixelChar.charSeqIndex.addAndGet(1);
            String key = String.valueOf(index);
            imgPixelChar.putColor(key, color);
            return key;
        } else {
            // 下面是颜色精确匹配的场景
            if (!imgPixelChar.charColorMap.containsValue(color)) {
                int index = imgPixelChar.charSeqIndex.addAndGet(1);
                String key = String.valueOf(index);
                imgPixelChar.putColor(key, color);
                return key;
            } else {
                for (Map.Entry<String, Color> entry : imgPixelChar.charColorMap.entrySet()) {
                    String k = entry.getKey();
                    Color v = entry.getValue();
                    if (Objects.equals(v, color)) {
                        return k;
                    }
                }
            }
        }
        return "";
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

        private final Map<String, Color> charColorMap;
        private AtomicInteger charSeqIndex = new AtomicInteger(0);

        public ImgPixelChar(AtomicInteger seqCount) {
            charCache = new TreeMap<>();
            charColorMap = new HashMap<>();
            this.seqCount = seqCount;
        }

        public void addChar(int y, char ch) {
            charCache.computeIfAbsent(y, (s) -> new StringBuilder()).append(ch);
        }

        public void putColor(String ch, Color color) {
            charColorMap.put(ch, color);
        }

        public Map<String, Color> getCharColorMap() {
            return charColorMap;
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

        public AtomicInteger getCharSeqIndex() {
            return charSeqIndex;
        }

        public void setPre(ImgPixelChar pre) {
            this.pre = pre;
        }
    }
}
