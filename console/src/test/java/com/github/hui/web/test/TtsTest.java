package com.github.hui.web.test;

import com.github.hui.quick.plugin.tts.constant.OutputFormatEnum;
import com.github.hui.quick.plugin.tts.constant.VoiceEnum;
import com.github.hui.quick.plugin.tts.model.SSML;
import com.github.hui.quick.plugin.tts.service.TTSService;
import org.junit.Test;

/**
 * @author YiHui
 * @date 2024/4/18
 */
public class TtsTest {

    @Test
    public void testTrans() {
        TTSService ts = new TTSService();
        ts.setBaseSavePath("d:\\"); // 设置保存路径
        SSML ssml = SSML.builder()
                .outputFormat(OutputFormatEnum.audio_24khz_48kbitrate_mono_mp3)
                .synthesisText("Popcorn")
                .outputFileName("grape")
                .voice(VoiceEnum.zh_CN_XiaoruiNeural)
                .build();
        ts.sendText(ssml);

        ts.sendText(SSML.builder()
                .outputFormat(OutputFormatEnum.audio_24khz_48kbitrate_mono_mp3)
                .synthesisText("我喜欢吃葡萄，I love to eat Popcorn")
                .outputFileName("grape sentence")
                .voice(VoiceEnum.zh_CN_XiaoruiNeural)
                .build()
        );
//
//        ts.sendText(SSML.builder()
//                .synthesisText("文件名自动生成测试文本")
//                .usePlayer(true) // 语音播放
//                .build());

        ts.close();
    }

}
