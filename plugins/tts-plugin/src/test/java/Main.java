import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import com.github.hui.quick.plugin.tts.TtsWrapper;
import com.github.hui.quick.plugin.tts.constant.VoiceEnum;
import com.github.hui.quick.plugin.tts.model.TtsConfig;
import com.github.hui.quick.plugin.tts.service.save.StreamSaveHook;

import java.io.InputStream;

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

    public void testStreamSave() {
        TtsConfig config = TtsConfig.newConfig().saveHook((b, s, t) -> {
                    // 流式保存
                    InputStream stream = StreamSaveHook.save(b);
                    FileWriteUtil.saveFileByStream(stream, "mp3");
                    return null;
                })
                .setSsml("飞流直下三千尺")
                .voice(VoiceEnum.zh_CN_liaoning_XiaobeiNeural)
                .over();
        TtsWrapper.sendTxt(config);
        TtsWrapper.close();
    }
}