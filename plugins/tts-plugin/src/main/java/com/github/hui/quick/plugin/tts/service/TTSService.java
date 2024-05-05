package com.github.hui.quick.plugin.tts.service;


import com.github.hui.quick.plugin.tts.constant.OutputFormatEnum;
import com.github.hui.quick.plugin.tts.constant.TtsConstants;
import com.github.hui.quick.plugin.tts.exceptions.TtsException;
import com.github.hui.quick.plugin.tts.model.SpeechConfig;
import com.github.hui.quick.plugin.tts.model.TtsConfig;
import com.github.hui.quick.plugin.tts.palyer.Player;
import com.github.hui.quick.plugin.tts.service.save.FileSaveHook;
import com.github.hui.quick.plugin.tts.util.TtsTools;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.Buffer;
import okio.ByteString;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class TTSService {

    public static final Logger log = LoggerFactory.getLogger(TTSService.class);

    /**
     * 保存音频文件的目录 默认工作目录
     */
    private String baseSavePath;

    public String getBaseSavePath() {
        return baseSavePath;
    }

    public void setBaseSavePath(String baseSavePath) {
        this.baseSavePath = baseSavePath;
    }

    public TTSService() {
    }

    public TTSService(String baseSavePath) {
        this.baseSavePath = baseSavePath;
    }

    private volatile TtsConfig ttsConfig;

    //================================

    /**
     * 正在进行合成...
     */
    private volatile boolean synthesising;

    /**
     * 当前的音频流数据
     */
    private final Buffer audioBuffer = new Buffer();
    private OkHttpClient okHttpClient;
    private WebSocket ws;

    protected WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            log.debug("onClosed:" + reason);
            TTSService.this.ws = null;
            synthesising = false;
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            if (code != 1000) {
                log.warn("onClosing: {}, {}", code, reason);
            } else if (log.isDebugEnabled()) {
                log.warn("onClosing: {}, {}", code, reason);
            }

            TTSService.this.ws = null;
            synthesising = false;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            log.debug("onFailure" + t.getMessage(), t);
            TTSService.this.ws = null;
            synthesising = false;
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            if (log.isDebugEnabled()) {
                log.debug("onMessage text\r\n:{}", text);
            }

            if (text.contains(TtsConstants.TURN_START)) {
                // （新的）音频流开始传输开始，清空重置buffer
                audioBuffer.clear();
            } else if (text.contains(TtsConstants.TURN_END)) {
                // 音频流结束，写为文件
                try {
                    Object absolutePath = writeAudio(ttsConfig.getOutputFormat(), audioBuffer.readByteString(), ttsConfig.getOutputFileName());
                    if (ttsConfig.isUsePlayer() && absolutePath instanceof String) {
                        try {
                            Player.autoPlay((String) absolutePath);
                        } catch (IOException | UnsupportedAudioFileException e) {
                            log.error(absolutePath + ":音频播放失败," + e.getMessage(), e);
                        }
                    }
                } finally {
                    // 转换完成
                    synthesising = false;
                }
            }
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
//            log.debug("onMessage bytes\r\n:{}", bytes.utf8());
            int audioIndex = bytes.lastIndexOf(TtsConstants.AUDIO_START.getBytes(StandardCharsets.UTF_8)) + TtsConstants.AUDIO_START.length();
            boolean audioContentType = bytes.lastIndexOf(TtsConstants.AUDIO_CONTENT_TYPE.getBytes(StandardCharsets.UTF_8)) + TtsConstants.AUDIO_CONTENT_TYPE.length() != -1;
            if (audioIndex != -1 && audioContentType) {
                try {
                    audioBuffer.write(bytes.substring(audioIndex));
                } catch (Exception e) {
                    log.error("onMessage Error," + e.getMessage(), e);
                }
            }
        }
    };

    /**
     * 发送合成请求
     *
     * @param ttsConfig
     */
    public void sendText(TtsConfig ttsConfig) {
        if (!ttsConfig.checkArgument()) {
            throw TtsException.of("请指定需要转语音的文本信息");
        }

        while (synthesising) {
            log.info("空转等待上一个语音合成");
            TtsTools.sleep(1);
        }
        synthesising = true;
        if (this.ttsConfig == null) {
            this.ttsConfig = ttsConfig;
            OutputFormatEnum toUpdateFormat = OutputFormatEnum.audio_24khz_48kbitrate_mono_mp3;
            sendConfig(toUpdateFormat);
        } else if (!Objects.equals(ttsConfig.getOutputFormat(), this.ttsConfig.getOutputFormat())) {
            this.ttsConfig = ttsConfig;
            sendConfig(Optional.ofNullable(ttsConfig.getOutputFormat()).orElse(OutputFormatEnum.audio_24khz_48kbitrate_mono_mp3));
        }

        if (log.isDebugEnabled()) {
            log.debug("ssml:{}", ttsConfig.toConfig());
        }

        if (!getOrCreateWs().send(ttsConfig.toConfig())) {
            throw TtsException.of("语音合成请求发送失败...");
        }
    }

    public void close() {
        while (synthesising) {
            log.info("空转等待语音合成...");
            TtsTools.sleep(1);
        }
        if (Objects.nonNull(ws)) {
            ws.close(1000, "bye");
        }
        if (Objects.nonNull(okHttpClient)) {
            okHttpClient.dispatcher().executorService().shutdown();   //清除并关闭线程池
            okHttpClient.connectionPool().evictAll();                 //清除并关闭连接池
        }
    }

    /**
     * 获取或创建 ws 连接
     *
     * @return
     */
    private synchronized WebSocket getOrCreateWs() {
        if (Objects.nonNull(ws)) {
            return ws;
        }

        String url = TtsConstants.buildWsUrl();
        String origin = TtsConstants.EDGE_SPEECH_ORIGIN;

        Request request = new Request.Builder().url(url).addHeader("User-Agent", TtsConstants.UA).addHeader("Origin", origin).build();
        ws = getOkHttpClient().newWebSocket(request, webSocketListener);
        if (this.ttsConfig != null) {
            sendConfig(this.ttsConfig.getOutputFormat());
        }
        return ws;
    }

    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    // 设置 PING 帧发送间隔
                    .pingInterval(20, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }


    /**
     * 发送下次音频输出配置
     *
     * @param outputFormat
     * @return
     */
    private void sendConfig(OutputFormatEnum outputFormat) {
        SpeechConfig speechConfig = SpeechConfig.of(outputFormat);
        log.debug("audio config:{}", speechConfig);
        if (!getOrCreateWs().send(speechConfig.toString())) {
            throw TtsException.of("语音输出格式配置失败...");
        }
    }


    /**
     * 写出音频
     *
     * @param format   音频输出格式
     * @param data     字节流
     * @param fileName 文件名
     * @return
     */
    private Object writeAudio(OutputFormatEnum format, ByteString data, String fileName) {
        try {
            String[] split = format.getValue().split("-");
            String suffix = split[split.length - 1];
            String saveFile = buildSaveFileName(fileName);
            if (this.ttsConfig.getSaveHook() == null) {
                this.ttsConfig.saveHook(FileSaveHook::save);
            }
            Object ans = this.ttsConfig.getSaveHook().save(data, saveFile, suffix);
            log.debug("音频保存完成! -> {}.{}", saveFile, suffix);
            return ans;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw TtsException.of("音频文件写出异常，" + e.getMessage());
        }
    }

    private String buildSaveFileName(String fileName) {
        if (StringUtils.isBlank(baseSavePath)) {
            this.baseSavePath = "";
        } else if (!baseSavePath.endsWith("/")) {
            this.baseSavePath = baseSavePath + "/";
        }

        if (fileName.startsWith("/")) {
            int index = 0;
            while (true) {
                if (fileName.charAt(index) != '/') {
                    fileName = fileName.substring(index);
                    break;
                }
                index += 1;
                if (index >= fileName.length()) {
                    break;
                }
            }
        }
        return this.baseSavePath + fileName;
    }
}
