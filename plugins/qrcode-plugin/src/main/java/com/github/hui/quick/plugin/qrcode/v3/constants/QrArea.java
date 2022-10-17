package com.github.hui.quick.plugin.qrcode.v3.constants;

import java.util.HashSet;
import java.util.Set;

/**
 * @author YiHui
 * @date 2022/10/17
 */
public enum QrArea {
    /**
     * 左上角
     */
    DETECT_LT,
    /**
     * 左下角
     */
    DETECT_LD,
    /**
     * 右上角
     */
    DETECT_RT,
    /**
     * 校正点
     */
    CHECK_POINT,
    NONE;

    private static Set<QrArea> detects;

    static {
        detects = new HashSet<>(4, 1);
        detects.add(DETECT_LD);
        detects.add(DETECT_LT);
        detects.add(DETECT_RT);
    }

    public boolean detectedArea() {
        return detects.contains(this);
    }

    public boolean checkPoint() {
        return CHECK_POINT == this;
    }
}
