package com.github.hui.web.test;

import com.github.hui.quick.plugin.tts.constant.OutputFormatEnum;
import com.github.hui.quick.plugin.tts.constant.VoiceEnum;
import com.github.hui.quick.plugin.tts.model.SsmlConfig;
import com.github.hui.quick.plugin.tts.model.TtsConfig;
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
//        TtsConfig ssml = TtsConfig.newConfig()
//                .outputFormat(OutputFormatEnum.audio_24khz_48kbitrate_mono_mp3)
//                .outputFileName("grape")
//                .setSsml("Popcorn")
//                .voice(VoiceEnum.zh_CN_XiaoruiNeural)
//                .over();
//        ts.sendText(ssml);
//
//        ts.sendText(TtsConfig.newConfig()
//                .outputFormat(OutputFormatEnum.audio_24khz_48kbitrate_mono_mp3)
//                .outputFileName("grape sentence")
//                .setSsml("我喜欢吃葡萄，I love to eat Popcorn")
//                .voice(VoiceEnum.zh_CN_XiaoruiNeural)
//                .over()
//        );

        ts.sendText(TtsConfig.newConfig()
                .setSsml("文件名自动生成测试文本")
                .over()
                .usePlayer() // 语音播放
        );
        ts.close();
    }

}
