package com.github.hui.quick.plugin.tts.exceptions;

/**
 * @author zh-hq
 * @date 2023/3/29
 */
public class TtsException extends RuntimeException {

    private TtsException(String message) {
        super(message);
    }

    public static TtsException of(String message) {
        return new TtsException(message);
    }
}
