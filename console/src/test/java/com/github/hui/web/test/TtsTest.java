package com.github.hui.web.test;

import io.ikfly.constant.OutputFormat;
import io.ikfly.constant.VoiceEnum;
import io.ikfly.model.SSML;
import io.ikfly.service.TTSService;
import org.junit.Test;

/**
 * @author YiHui
 * @date 2024/4/18
 */
public class TtsTest {

    @Test
    public void testTrans() {
        TTSService ts = new TTSService();
//        ts.setBaseSavePath("d:\\"); // 设置保存路径
        SSML ssml = SSML.builder()
                .outputFormat(OutputFormat.audio_24khz_48kbitrate_mono_mp3)
                .synthesisText("葡萄 Grape，我喜欢吃葡萄，I love to eat grapes")
                .outputFileName("grape.mp3")
                .voice(VoiceEnum.zh_CN_XiaoxiaoNeural)
                .build();
        ts.sendText(ssml);
//
//        ts.sendText(SSML.builder()
//                .synthesisText("文件名自动生成测试文本")
//                .usePlayer(true) // 语音播放
//                .build());

        ts.close();
    }

}
