package com.github.hui.quick.plugin.qrcode.v3.helper;

import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResourcePool;
import com.github.hui.quick.plugin.qrcode.v3.req.DrawOptions;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

import static com.github.hui.quick.plugin.qrcode.v3.entity.QrResourcePool.NO_LIMIT_COUNT;

/**
 * @author YiHui
 * @date 2022/8/6
 */
public class SvgHelper {
    private static final String PRE_START_TAG = "<defs>";
    private static final String PRE_END_TAG = "</defs>";

    private static final String SYMBOL_START_TAG = "<symbol";
    private static final String SYMBOL_END_TAG = "</symbol>";

    /**
     * svg模板解析， 模板规则
     *
     * <defs></defs> 标签包含内容，作为预定义
     * <symbol></symbol> 表示一个标签
     *
     * @return
     */
    public static void svgTemplateParseAndInit(QrCodeV3Options v3Options, String template) {
        DrawOptions options = v3Options.newDrawOptions();
        // 首先解析 defs 内容
        int preStartIndex = template.indexOf(PRE_START_TAG);
        if (preStartIndex >= 0) {
            int preEndIndex = template.indexOf(PRE_END_TAG);
            if (preEndIndex <= preStartIndex) {
                throw new IllegalArgumentException("非法的svgTemplate, <defs>标签不满足要求!");
            }

            String pre = template.substring(preStartIndex + PRE_START_TAG.length(), preEndIndex);
            options.setGlobalResource(new QrResource().setSvg(pre));
            template = template.substring(0, preStartIndex) + template.substring(preEndIndex + PRE_END_TAG.length());
        }

        List<SymbolTag> dotList = new ArrayList<>();
        List<SymbolTag> detectList = new ArrayList<>();

        int dotSize = Integer.MAX_VALUE;
        int start = 0;
        int end = template.indexOf(SYMBOL_END_TAG);
        while (end > 0) {
            end += SYMBOL_END_TAG.length();
            String symbol = template.substring(start, end);
            SymbolTag tag = new SymbolTag(symbol);
            if (tag.detectSvgTag()) {
                // 探测图形的资源位特殊处理，不参与dot_size的计算
                detectList.add(tag);
            } else {
                dotSize = Integer.min(dotSize, Integer.min(tag.width, tag.height));
                dotList.add(tag);
            }
            start = end;
            end = template.indexOf(SYMBOL_END_TAG, start);
        }

        initQrResource(options, dotSize, dotList);
        initDetectResource(v3Options, detectList);
    }

    private static void initQrResource(DrawOptions options, int dotSize, List<SymbolTag> dotList) {
        Map<ImmutablePair<Integer, Integer>, List<SymbolTag>> svgSymbols = new HashMap<>(dotList.size());
        for (SymbolTag tag : dotList) {
            ImmutablePair<Integer, Integer> key = ImmutablePair.of(tag.width / dotSize, tag.height / dotSize);
            svgSymbols.computeIfAbsent(key, s -> new ArrayList<>()).add(tag);
        }

        if (!svgSymbols.containsKey(ImmutablePair.of(1, 1))) {
            throw new IllegalArgumentException("no 1x1 svg symbol in svgTemplate!");
        }

        svgSymbols.forEach((k, v) -> {
            QrResourcePool.QrResourcesDecorate decorate = options.getResourcePool().addSource(k.getLeft(), k.getRight());
            v.forEach(tag -> decorate.addResource(new QrResource().setSvg(tag.tag), tag.count));
            decorate.build();
        });
        options.getResourcePool().over();
    }

    private static void initDetectResource(QrCodeV3Options v3Options, List<SymbolTag> detectList) {
        // 设置探测图形资源位
        if (detectList.isEmpty()) {
            return;
        }

        if (detectList.size() == 1) {
            v3Options.newDetectOptions().setResource(new QrResource().setSvg(detectList.get(0).tag));
            return;
        } else if (detectList.size() > 3) {
            throw new IllegalArgumentException("more than 3 detect resource in svg template!");
        }

        // 根据指定的位置来设置资源位
        List<SymbolTag> notHit = new ArrayList<>();
        for (SymbolTag tag : detectList) {
            String type = tag.type.toLowerCase(Locale.ROOT);
            if (type.endsWith("lt")) {
                v3Options.newDetectOptions().setLt(new QrResource().setSvg(tag.tag));
            } else if (type.endsWith("ld")) {
                v3Options.newDetectOptions().setLd(new QrResource().setSvg(tag.tag));
            } else if (type.endsWith("rt")) {
                v3Options.newDetectOptions().setRt(new QrResource().setSvg(tag.tag));
            } else {
                notHit.add(tag);
            }
        }

        // 若没有指定位置，则根据顺序来依次设置资源位
        if (!notHit.isEmpty()) {
            if (v3Options.newDetectOptions().getLt() == null) {
                v3Options.getDetectOptions().setLt(new QrResource().setSvg(notHit.remove(0).tag));
            }

            if (v3Options.getDetectOptions().getLd() == null && notHit.size() > 0) {
                v3Options.getDetectOptions().setLd(new QrResource().setSvg(notHit.remove(0).tag));
            }

            if (v3Options.getDetectOptions().getRt() == null && notHit.size() > 0) {
                v3Options.getDetectOptions().setRt(new QrResource().setSvg(notHit.remove(0).tag));
            }
        }
    }

    private static class SymbolTag {
        /**
         * 指定的宽，如果不存在，则从viewBox进行解析
         */
        private static final String WIDTH = "width=\"";
        /**
         * 指定的高，如果不存在，则从viewBox进行解析
         */
        private static final String HEIGHT = "height=\"";
        /**
         * symbol 视图大小
         */
        private static final String VIEW_BOX = "viewBox=\"";
        /**
         * 最多出现次数属性值
         */
        private static final String COUNT = "count=\"";

        /**
         * 用于确定这个symbol，是探测图形还是pre, bg，取值参考
         *
         * @see SymbolTagType
         */
        private static final String TYPE = "type=\"";

        private static final String MISS = "-MISS-";

        String tag;
        int width;
        int height;
        int count;

        /**
         * detect = 表示探测图形
         * pre = 表示渲染二维码中1点的资源图
         * bg = 表示渲染二维码中0点的资源图
         */
        String type;

        /**
         * 传参形如:  <symbol id="symbol_1z" viewBox="0 0 49 49" width="100" height="100" count="20">
         *
         * @param tag
         */
        public SymbolTag(String tag) {
            this.tag = tag;
            int start = tag.indexOf(SYMBOL_START_TAG);
            int end = tag.indexOf(">", start + SYMBOL_START_TAG.length());
            String symbol = tag.substring(start, end);

            String c = getVal(symbol, COUNT);
            if (MISS.equalsIgnoreCase(c)) count = NO_LIMIT_COUNT;
            else count = Integer.parseInt(c);

            type = getVal(symbol, TYPE);
            if (MISS.equalsIgnoreCase(type)) type = SymbolTagType.PRE.tag;

            String w = getVal(symbol, WIDTH);
            if (!MISS.equalsIgnoreCase(w)) width = Integer.parseInt(w);

            String h = getVal(symbol, HEIGHT);
            if (!MISS.equalsIgnoreCase(h)) height = Integer.parseInt(h);

            if (width > 0 && height > 0) return;

            String box = getVal(symbol, VIEW_BOX);
            String[] pos = StringUtils.split(box, " ");
            if (pos.length == 2) {
                if (width == 0) width = Integer.parseInt(pos[0].trim());
                if (height == 0) height = Integer.parseInt(pos[1].trim());
            } else if (pos.length == 4) {
                if (width == 0) width = Integer.parseInt(pos[2].trim());
                if (height == 0) height = Integer.parseInt(pos[3].trim());
            }

            if (width == 0 || height == 0) {
                throw new IllegalArgumentException("illegal symbol with no width/height!");
            }
        }

        private String getVal(String content, String tag) {
            int start = content.indexOf(tag);
            if (start < 0) {
                return MISS;
            }

            start += tag.length();
            int end = content.indexOf("\"", start);
            if (end <= start) {
                return MISS;
            }

            return content.substring(start, end).trim();
        }

        boolean detectSvgTag() {
            return type.startsWith(SymbolTagType.DETECT.tag);
        }

        boolean preSvgTag() {
            return type == null || SymbolTagType.PRE.tag.equalsIgnoreCase(type);
        }

        boolean bgSvgTag() {
            return SymbolTagType.BG.tag.equalsIgnoreCase(type);
        }
    }

    private enum SymbolTagType {
        PRE("pre"), BG("bg"), DETECT("detect"),
        ;
        String tag;

        SymbolTagType(String tag) {
            this.tag = tag;
        }
    }
}
