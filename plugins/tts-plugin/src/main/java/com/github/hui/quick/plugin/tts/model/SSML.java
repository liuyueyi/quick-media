package com.github.hui.quick.plugin.tts.model;


import com.github.hui.quick.plugin.tts.constant.OutputFormatEnum;
import com.github.hui.quick.plugin.tts.constant.VoiceEnum;
import com.github.hui.quick.plugin.tts.service.save.FileSaveHook;
import com.github.hui.quick.plugin.tts.service.save.OutputSaveHook;
import com.github.hui.quick.plugin.tts.util.TtsTools;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author zh-hq
 * @date 2023/3/30
 */
public class SSML implements Serializable {

    public static String SSML_PATTERN = "X-RequestId:%s\r\n" + "Content-Type:application/ssml+xml\r\n" + "X-Timestamp:%sZ\r\n" + "Path:ssml\r\n" + "\r\n" + "<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xmlns:mstts='https://www.w3.org/2001/mstts' xml:lang='%s'>\r\n" + "<voice name='%s'>\r\n" + "<prosody pitch='+0Hz' rate='%s' volume='%s'>" + "%s" + "</prosody>" + "</voice>" + "</speak>";
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

    private OutputFormatEnum outputFormat;

    private String outputFileName;

    private boolean usePlayer;

    /**
     * 下一个合成语音的配置相关
     */
    private SSML next;

    /**
     * 文件保存的方式
     */
    private OutputSaveHook saveHook;

    private SSML(String synthesisText, VoiceEnum voice, String rate, String volume, OutputFormatEnum outputFormat, String outputFileName, boolean usePlayer, OutputSaveHook saveHook) {
        this.synthesisText = synthesisText;
        this.voice = voice;
        this.rate = rate;
        this.volume = volume;
        this.outputFormat = outputFormat;
        this.outputFileName = outputFileName;
        this.usePlayer = usePlayer;
        this.saveHook = saveHook;
    }

    public static SSMLBuilder builder() {
        return new SSMLBuilder();
    }

    public String getSynthesisText() {
        return synthesisText;
    }

    public OutputFormatEnum getOutputFormat() {
        return outputFormat;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public OutputSaveHook getSaveHook() {
        return saveHook;
    }

    public boolean getUsePlayer() {
        return usePlayer;
    }

    /*
            X-RequestId:4ff8174b303fc1032ec1b66ea9a86459
            Content-Type:application/ssml+xml
            X-Timestamp:Tue Mar 28 2023 17:49:51 GMT+0800 (中国标准时间)Z
            Path:ssml

            <speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xmlns:mstts='https://www.w3.org/2001/mstts' xml:lang='en-US'>
            <voice name='Microsoft Server Speech Text to Speech Voice (en-GB, LibbyNeural)'>
            <prosody pitch='+0Hz' rate ='+0%' volume='+0%'>CloseEvent.wasClean Re</prosody></voice>
            </speak>
         */
    @Override
    public String toString() {
        return String.format(SSML_PATTERN, TtsTools.getRandomId(), TtsTools.date(), Optional.ofNullable(voice).orElse(VoiceEnum.zh_CN_XiaoxiaoNeural).getLocale(), Optional.ofNullable(voice).orElse(VoiceEnum.zh_CN_XiaoxiaoNeural).getShortName(), Optional.ofNullable(rate).orElse("+0%"), Optional.ofNullable(volume).orElse("+0%"), synthesisText);
    }


    public static class SSMLBuilder {
        private String synthesisText;
        private VoiceEnum voice;
        private String rate;
        private String volume;
        private OutputFormatEnum outputFormat;
        private String outputFileName;
        private boolean usePlayer;
        private OutputSaveHook saveHook;

        public SSMLBuilder synthesisText(String synthesisText) {
            this.synthesisText = synthesisText;
            return this;
        }

        public SSMLBuilder voice(VoiceEnum voice) {
            this.voice = voice;
            return this;
        }

        public SSMLBuilder rate(String rate) {
            this.rate = rate;
            return this;
        }

        public SSMLBuilder volume(String volume) {
            this.volume = volume;
            return this;
        }

        public SSMLBuilder outputFileName(String outputFileName) {
            this.outputFileName = outputFileName;
            return this;
        }

        public SSMLBuilder outputFormat(OutputFormatEnum outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }

        public SSMLBuilder usePlayer(boolean usePlayer) {
            this.usePlayer = usePlayer;
            return this;
        }

        public SSMLBuilder saveHook(OutputSaveHook saveHook) {
            this.saveHook = saveHook;
            return this;
        }

        public SSML build() {
            if (this.saveHook == null) {
                // 默认保存在文件中
                this.saveHook = FileSaveHook::save;
            }

            return new SSML(this.synthesisText, this.voice, this.rate, this.volume, this.outputFormat, this.outputFileName, this.usePlayer, this.saveHook);
        }
    }

}
