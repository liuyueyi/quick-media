import com.github.hui.quick.plugin.tts.TtsWrapper;
import com.github.hui.quick.plugin.tts.constant.VoiceEnum;
import com.github.hui.quick.plugin.tts.model.TtsConfig;

/**
 * @author YiHui
 * @date 2024/4/30
 */
public class Main {
    public static void main(String[] args) {
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