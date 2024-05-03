package com.github.hui.quick.plugin.tts.constant;

import com.github.hui.quick.plugin.tts.util.TtsTools;

/**
 * @author zh-hq
 * @date 2023/3/23
 */
public interface TtsConstants {

    String TRUSTED_CLIENT_TOKEN = "6A5AA1D4EAFF4E9FB37E23D68491D6F4";

    // https://speech.platform.bing.com/consumer/speech/synthesize/readaloud/voices/list?trustedclienttoken=6A5AA1D4EAFF4E9FB37E23D68491D6F4
    String VOICE_LIST_URL = "https://speech.platform.bing.com/consumer/speech/synthesize/readaloud/voices/list";

    // wss://speech.platform.bing.com/consumer/speech/synthesize/readaloud/edge/v1?TrustedClientToken=6A5AA1D4EAFF4E9FB37E23D68491D6F4&ConnectionId=a6c52f201673b44b73fede7a6de9def2
    String EDGE_SPEECH_WSS = "wss://speech.platform.bing.com/consumer/speech/synthesize/readaloud/edge/v1";
    String EDGE_SPEECH_ORIGIN = "chrome-extension://jdiccldimpdaibmpdkjnbmckianbfold";

    // wss://eastus.api.speech.microsoft.com/cognitiveservices/websocket/v1?TrafficType=AzureDemo&Authorization=bearer undefined&X-ConnectionId=dasfieioi342354c&Retry-After=200
    String AZURE_SPEECH_WSS = "wss://eastus.api.speech.microsoft.com/cognitiveservices/websocket/v1";
    String AZURE_SPEECH_ORIGIN = "https://azure.microsoft.com";

    /**
     * EDGE UA
     */
    String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44";

    /**
     * 音频流开始传输标记
     */
    String TURN_START = "turn.start";
    /**
     * 音频流结束传输标记
     */
    String TURN_END = "turn.end";

    /**
     * 音频数据流标志头
     */
    String AUDIO_START = "Path:audio\r\n";

    /**
     * 音频mine
     */
    String AUDIO_CONTENT_TYPE = "Content-Type:audio";


    /**
     * 构建转tts的长连接
     *
     * @return ws长连接地址
     */
    static String buildWsUrl() {
        return TtsConstants.EDGE_SPEECH_WSS + "?Retry-After=200&TrustedClientToken=" + TtsConstants.TRUSTED_CLIENT_TOKEN + "&ConnectionId=" + TtsTools.getRandomId();
    }
}
