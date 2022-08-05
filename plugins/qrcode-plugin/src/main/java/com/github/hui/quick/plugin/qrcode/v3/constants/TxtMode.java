package com.github.hui.quick.plugin.qrcode.v3.constants;

import java.util.concurrent.atomic.AtomicInteger;

public enum TxtMode {
    /***
     * 文字二维码，随机模式
     */
    RANDOM {
        @Override
        public String txt(String input, AtomicInteger index) {
            int i = (int) (Math.random() * input.length());
            return String.valueOf(input.charAt(i));
        }
    },
    /**
     * 文字二维码，顺序模式
     */
    ORDER {
        @Override
        public String txt(String input, AtomicInteger index) {
            int i = index.getAndAdd(1) % input.length();
            return String.valueOf(input.charAt(i));
        }
    },
    /**
     * 全量
     */
    FULL {
        @Override
        public String txt(String input, AtomicInteger index) {
            return input;
        }
    };

    public abstract String txt(String input, AtomicInteger index);
}