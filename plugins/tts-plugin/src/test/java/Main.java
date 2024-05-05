import com.github.hui.quick.plugin.tts.TtsWrapper;
import com.github.hui.quick.plugin.tts.constant.VoiceEnum;
import com.github.hui.quick.plugin.tts.model.TtsConfig;
import org.junit.Test;

/**
 * @author YiHui
 * @date 2024/4/30
 */
public class Main {
    public static void main(String[] args) {
//        TTSService ts = new TTSService();
//        ts.setBaseSavePath("d:\\"); // 设置保存路径
//        SSML ssml = SSML.builder()
//                .outputFormat(OutputFormatEnum.audio_24khz_48kbitrate_mono_mp3)
//                .synthesisText("自定义文件名测试文本，java 文本转语音")
//                .outputFileName("自定义文件名")
//                .voice(VoiceEnum.zh_CN_XiaoxiaoNeural)
//                .build();
//        ts.sendText(ssml);
//
//        ts.sendText(SSML.builder()
//                .synthesisText("文件名自动生成测试文本")
//                .usePlayer(true) // 语音播放
//                .build());
//
//        ts.close();

        TtsWrapper.sendTxt(TtsConfig.newConfig()
                .outputFileName("测试")
                .setSsml("我喜欢吃葡萄 I like to eat grape")
                        .voice(VoiceEnum.zh_CN_liaoning_XiaobeiNeural)
//                .next("猴子喜欢吃香蕉 Monkey likes to eat banana")
//                .voice(VoiceEnum.zh_CN_YunyeNeural)
                .over()
        );

        TtsWrapper.close();
    }

    @Test
    public void testTts() {
        TtsConfig ttsConfig = TtsConfig.newConfig()
                .outputFileName("测试")
                .setSsml("我喜欢吃葡萄 I like to eat grape")
                .voice(VoiceEnum.zh_CN_liaoning_XiaobeiNeural)
                .over();
        TtsWrapper.sendTxt(ttsConfig);
        TtsWrapper.close();
    }
}