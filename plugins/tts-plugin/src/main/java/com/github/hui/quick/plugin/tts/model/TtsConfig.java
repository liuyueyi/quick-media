package com.github.hui.quick.plugin.tts.model;

import com.github.hui.quick.plugin.tts.constant.OutputFormatEnum;
import com.github.hui.quick.plugin.tts.constant.VoiceEnum;
import com.github.hui.quick.plugin.tts.service.save.OutputSaveHook;
import com.github.hui.quick.plugin.tts.util.TtsTools;

import java.util.Optional;

public class TtsConfig {

    public static String SSML_PATTERN = "X-RequestId:%s\r\n" + "Content-Type:application/ssml+xml\r\n" + "X-Timestamp:%sZ\r\n" + "Path:ssml\r\n" + "\r\n" + "<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xmlns:mstts='https://www.w3.org/2001/mstts' xml:lang='%s'>\r\n";

    /**
     * 音频保存类型
     */
    private OutputFormatEnum outputFormat;

    /**
     * 保存的文件名
     */
    private String outputFileName;

    /**
     * 是否直接播放
     */
    private Boolean usePlayer;
    /**
     * 文件保存的方式
     */
    private OutputSaveHook<?> saveHook;


    /**
     * 转语音的配置
     */
    private SsmlConfig ssml;

    private TtsConfig() {
    }

    public static TtsConfig newConfig() {
        return new TtsConfig();
    }


    public OutputFormatEnum getOutputFormat() {
        return outputFormat;
    }

    public String getOutputFileName() {
        if (outputFileName == null || outputFileName.isEmpty()) {
            outputFileName = (ssml.getSynthesisText().length() < 6 ? ssml.getSynthesisText() : ssml.getSynthesisText().substring(0, 5)).replaceAll("[</|*。?\" >\\\\]", "") + TtsTools.localDateTime();
        }
        return outputFileName;
    }

    public boolean isUsePlayer() {
        return usePlayer;
    }

    public OutputSaveHook<?> getSaveHook() {
        return saveHook;
    }

    public SsmlConfig getSsml() {
        return ssml;
    }

    public TtsConfig saveHook(OutputSaveHook<?> saveHook) {
        this.saveHook = saveHook;
        return this;
    }

    public TtsConfig usePlayer() {
        this.usePlayer = true;
        return this;
    }

    public TtsConfig outputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
        return this;
    }

    public TtsConfig outputFormat(OutputFormatEnum outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    public SsmlConfig setSsml(String text) {
        this.ssml = new SsmlConfig(this).text(text);
        return ssml;
    }

    public String toConfig() {
        String str = String.format(SSML_PATTERN, TtsTools.getRandomId(), TtsTools.date(), Optional.ofNullable(ssml.getVoice()).orElse(VoiceEnum.zh_CN_XiaoxiaoNeural).getLocale());
        return str + this.ssml.toConfig() + "\n</speak>";
//        fixme 实测，不支持多种说话风格
//        String str = String.format("X-RequestId:%s\r\n" + "Content-Type:application/ssml+xml\r\n" + "X-Timestamp:%sZ\r\n" + "Path:ssml\r\n", TtsTools.getRandomId(), TtsTools.date());
//        String ssmlContent = "\r\n<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xmlns:mstts=\"https://www.w3.org/2001/mstts\" xml:lang='zh-CN'>\n" +
//                "   <voice name=\"zh-CN-XiaomoNeural\">\n" +
//                "        女儿看见父亲走了进来，问道：\n" +
//                "        <mstts:express-as role=\"YoungAdultFemale\" style=\"calm\">\n" +
//                "            “您来的挺快的，怎么过来的？”\n" +
//                "        </mstts:express-as>\n" +
//                "        父亲放下手提包，说：\n" +
//                "        <mstts:express-as role=\"OlderAdultMale\" style=\"calm\">\n" +
//                "            “刚打车过来的，路上还挺顺畅。”\n" +
//                "        </mstts:express-as>\n" +
//                "    </voice>" +
//                "</speak>";
//        return str + ssmlContent;
    }

    /**
     * 用于校验必传参数是否传了
     *
     * @return true表示通过
     */
    public boolean checkArgument() {
        if (this.outputFormat == null) {
            this.outputFormat = OutputFormatEnum.audio_24khz_48kbitrate_mono_mp3;
        }

        if (this.usePlayer == null) {
            this.usePlayer = false;
        }

        if (this.ssml == null || this.ssml.getSynthesisText() == null || this.ssml.getSynthesisText().isEmpty()) {
            return false;
        }
        return true;
    }
}
