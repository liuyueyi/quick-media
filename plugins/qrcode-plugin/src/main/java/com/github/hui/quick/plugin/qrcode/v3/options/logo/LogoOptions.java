package com.github.hui.quick.plugin.qrcode.v3.options.logo;

/**
 * @author
 * @date 2022/6/10
 */
public class LogoOptions {

    private int rate;

    /**
     * true 表示清空logo内部的信息
     */
    private boolean clearArea;

    public int getRate() {
        return rate;
    }

    public LogoOptions setRate(int rate) {
        this.rate = rate;
        return this;
    }

    public boolean isClearArea() {
        return clearArea;
    }

    public LogoOptions setClearArea(boolean clearArea) {
        this.clearArea = clearArea;
        return this;
    }
}
