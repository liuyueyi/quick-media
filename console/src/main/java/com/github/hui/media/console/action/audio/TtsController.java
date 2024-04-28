package com.github.hui.media.console.action.audio;

import com.github.hui.media.console.entity.ResponseWrapper;
import io.ikfly.constant.OutputFormat;
import io.ikfly.constant.VoiceEnum;
import io.ikfly.model.SSML;
import io.ikfly.service.TTSService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文本转tts
 *
 * @author YiHui
 * @date 2024/4/22
 */
@RestController
public class TtsController {
    private String BASE = "d://tmp/audio/";
    private volatile TTSService ttsService;
    private volatile long lastVisit = 0L;

    @RequestMapping(path = "tts")
    public ResponseWrapper<String> toTts(String content, String saveFile, String voice) {
        System.out.println("开始转换: " + saveFile);
        SSML ssml = SSML.builder()
                .outputFormat(OutputFormat.audio_24khz_48kbitrate_mono_mp3)
                .synthesisText(content)
                .outputFileName(saveFile)
                .voice(buildVoice(voice))
                .build();
        getTts().sendText(ssml);
        return ResponseWrapper.successReturn(BASE + saveFile + ".mp3");
    }

    private VoiceEnum buildVoice(String voice) {
        for (VoiceEnum v : VoiceEnum.values()) {
            if (v.name().equalsIgnoreCase(voice)) {
                return v;
            }
        }
        return VoiceEnum.zh_CN_XiaoxiaoNeural;
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
}
