package com.github.hui.media.console.action.audio;

import com.github.hui.media.console.entity.ResponseWrapper;
import com.github.hui.quick.plugin.tts.TtsWrapper;
import com.github.hui.quick.plugin.tts.constant.OutputFormatEnum;
import com.github.hui.quick.plugin.tts.model.TtsConfig;
import com.github.hui.quick.plugin.tts.service.TTSService;
import com.github.hui.quick.plugin.tts.service.save.StreamSaveHook;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文本转tts
 *
 * @author YiHui
 * @date 2024/4/22
 */
@RestController
public class TtsController {
    @RequestMapping(path = "tts")
    public ResponseWrapper<String> toTts(String content, String saveFile, String voice) {
        System.out.println("开始转换: " + saveFile);
        TtsConfig ssml = TtsConfig.newConfig()
                .outputFormat(OutputFormatEnum.audio_24khz_48kbitrate_mono_mp3)
                .outputFileName(saveFile)
                .setSsml(content)
                .voice(TtsWrapper.fromVoice(voice))
                .over();
        TtsWrapper.sendTxt(ssml);
        return ResponseWrapper.successReturn(TtsWrapper.getInstance().getBaseDir() + saveFile + ".mp3");
    }


    /**
     * 异步方式返回MP3
     *
     * @param content
     * @param voice
     * @param request
     */
    @RequestMapping(path = "stream/tts")
    public void toStream(String content, String voice, HttpServletRequest request) {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.start(() -> {
            TtsConfig ssml = TtsConfig.newConfig()
                    .outputFormat(OutputFormatEnum.audio_24khz_48kbitrate_mono_mp3)
                    .setSsml(content)
                    .voice(TtsWrapper.fromVoice(voice))
                    .over()
                    .saveHook((data, saveName, suffix) -> {
                        InputStream stream = StreamSaveHook.save(data);
                        try {
                            ServletResponse response = asyncContext.getResponse();
                            response.setContentType("audio/mp3");
                            response.setContentLength(data.size());
                            IOUtils.copy(stream, response.getOutputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            asyncContext.complete();
                        }
                        return null;
                    });
            TtsWrapper.sendTxt(ssml);
        });
    }
}
