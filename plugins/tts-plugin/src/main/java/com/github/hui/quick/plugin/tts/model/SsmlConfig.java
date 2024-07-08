package com.github.hui.quick.plugin.tts.model;

import com.github.hui.quick.plugin.tts.TtsWrapper;
import com.github.hui.quick.plugin.tts.constant.VoiceEnum;

import java.util.Optional;

public class SsmlConfig {
    public static String SSML_PATTERN = "<voice name='%s'>\r\n" + "<prosody pitch='+0Hz' rate='%s' volume='%s'>" + "%s" + "</prosody>\n" + "</voice>\n";

    /**
     * 全局的语音转换配置
     */
    private final TtsConfig ttsConfig;

    /**
     * 语音合成文本
     */
    private String synthesisText;

    /**
     * 语音角色
     */
    private VoiceEnum voice;
    /**
     * 语速
     * 相对值：
     * 以相对数字表示：以充当默认值乘数的数字表示。 例如，如果值为 1，则原始速率不会变化。 如果值为 0.5，则速率为原始速率的一半。 如果值为 2，则速率为原始速率的 2 倍。
     * 以百分比表示：以“+”（可选）或“-”开头且后跟“%”的数字表示，指示相对变化。 例如 <prosody rate="50%">some text</prosody> 或 <prosody rate="-50%">some text</prosody>。
     */
    private String rate;
    /**
     * 音量
     * 绝对值：以从 0.0 到 100.0（从最安静到最大声）的数字表示。 例如 75。 默认值为 100.0。
     * 相对值：
     * 以相对数字表示：以前面带有“+”或“-”的数字表示，指定音量的变化量。 例如 +10 或 -5.5。
     * 以百分比表示：以“+”（可选）或“-”开头且后跟“%”的数字表示，指示相对变化。 例如 <prosody volume="50%">some text</prosody> 或 <prosody volume="+3%">some text</prosody>。
     */
    private String volume;

    protected SsmlConfig(TtsConfig ttsConfig) {
        this.ttsConfig = ttsConfig;
    }

    public SsmlConfig text(String text) {
        text = text.replaceAll("《", "<").replaceAll("》", ">");
        this.synthesisText = text;
        return this;
    }

    public String getSynthesisText() {
        return synthesisText;
    }

    public SsmlConfig voice(VoiceEnum voice) {
        this.voice = voice;
        return this;
    }

    public SsmlConfig voice(String voice) {
        this.voice = VoiceEnum.ofOrDefault(voice);
        return this;
    }

    public VoiceEnum getVoice() {
        return voice;
    }

    public SsmlConfig rate(String rate) {
        this.rate = rate;
        return this;
    }

    public SsmlConfig volume(String volume) {
        this.volume = volume;
        return this;
    }


    public TtsConfig over() {
        return ttsConfig;
    }

    public String toConfig() {
        return String.format(SSML_PATTERN,
                Optional.ofNullable(voice).orElse(VoiceEnum.zh_CN_XiaoxiaoNeural).getShortName(),
                Optional.ofNullable(rate).orElse("+0%"),
                Optional.ofNullable(volume).orElse("+0%"),
                synthesisText);
    }

}
