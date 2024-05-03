package com.github.hui.quick.plugin.tts;

import com.github.hui.quick.plugin.tts.constant.VoiceEnum;
import com.github.hui.quick.plugin.tts.model.SSML;
import com.github.hui.quick.plugin.tts.service.TTSService;

import java.util.Optional;

/**
 * @author YiHui
 * @date 2024/4/30
 */
public class TtsWrapper {
    public String BASE = "d://tmp/audio/";
    private volatile TTSService ttsService;
    private volatile long lastVisit = 0L;

    private TtsWrapper() {
    }

    private static volatile TtsWrapper instance;

    public static TtsWrapper getInstance() {
        if (instance == null) {
            synchronized (TtsWrapper.class) {
                if (instance == null) {
                    instance = new TtsWrapper();
                }
            }
        }
        return instance;
    }


    /**
     * 获取音色
     *
     * @param voice
     * @return
     */
    public VoiceEnum fromVoice(String voice) {
        return Optional.ofNullable(VoiceEnum.of(voice)).orElse(VoiceEnum.zh_CN_XiaoxiaoNeural);
    }

    private void init() {
        ttsService = new TTSService();
        ttsService.setBaseSavePath(BASE);
        lastVisit = System.currentTimeMillis();
    }

    public TTSService getTts() {
        if (System.currentTimeMillis() - lastVisit >= 60_000) {
            if (ttsService != null) {
                ttsService.close();
            }
            init();
        }
        lastVisit = System.currentTimeMillis();
        return this.ttsService;
    }

    public void sendTxt(SSML txt) {
        getTts().sendText(txt);
    }
}
