package com.github.hui.quick.plugin.tts;

import com.github.hui.quick.plugin.tts.constant.VoiceEnum;
import com.github.hui.quick.plugin.tts.model.TtsConfig;
import com.github.hui.quick.plugin.tts.service.TTSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author YiHui
 * @date 2024/4/30
 */
public class TtsWrapper {
    private static final Logger log = LoggerFactory.getLogger(TtsWrapper.class);
    private volatile String baseDir;
    private volatile TTSService ttsService;
    private volatile long lastVisit = 0L;
    public static int EXPIRE_TIME = 5 * 60 * 1000;

    private TtsWrapper(String baseDir) {
        this.baseDir = baseDir;
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    long now = System.currentTimeMillis();
                    if (now - lastVisit >= EXPIRE_TIME && ttsService != null) {
                        // 超过五分钟没有访问，则主动关闭长连接
                        ttsService.close();
                    }
                } catch (Exception e) {
                    log.warn("auto close tts ws error! ", e);
                }
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    private static volatile TtsWrapper instance;

    public static TtsWrapper getInstance(String baseDir) {
        if (instance == null) {
            synchronized (TtsWrapper.class) {
                if (instance == null) {
                    instance = new TtsWrapper(baseDir);
                }
            }
        }
        return instance;
    }

    public static TtsWrapper getInstance() {
        return getInstance("d://");
    }

    private void init() {
        ttsService = new TTSService();
        ttsService.setBaseSavePath(baseDir);
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

    public String getBaseDir() {
        return baseDir;
    }

    /**
     * 更新全局的默认音频保存路径
     *
     * @param baseDir 保存路径
     */
    public void updateBaseDir(String baseDir) {
        this.baseDir = baseDir;
        getTts().setBaseSavePath(this.baseDir);
    }

    /**
     * 获取音色
     *
     * @param voice 语音
     * @return 语音对应的枚举
     */
    public static VoiceEnum fromVoice(String voice) {
        return Optional.ofNullable(VoiceEnum.of(voice)).orElse(VoiceEnum.zh_CN_XiaoxiaoNeural);
    }

    public static void sendTxt(TtsConfig config) {
        getInstance().getTts().sendText(config);
    }

    public static void close() {
        getInstance().getTts().close();
    }

//    public static TtsConfig autoTrans(String text) {
//        return TtsConfig.newConfig().setSsml(text);
//    }
}
