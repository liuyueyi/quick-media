package com.github.hui.quick.plugin.qrcode.v3.options.source;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @date 2022/6/12
 */
public class DetectSourceOptions extends SourceOptions {
    private Map<DetectSourceArea, Color> colorMap = new HashMap<>(4, 1);

    public enum DetectSourceArea {
        OUT, IN, BG;
    }

    public Color getColor(DetectSourceArea area) {
        if (colorMap.containsKey(area)) {
            return colorMap.get(area);
        }
        return super.getColor();
    }

    public DetectSourceOptions addColor(DetectSourceArea area, Color color) {
        colorMap.put(area, color);
        return this;
    }
}