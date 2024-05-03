package com.github.hui.quick.plugin.tts.palyer;

import com.github.hui.quick.plugin.tts.exceptions.TtsException;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * @author YiHui
 * @date 2024/4/30
 */
public class Player {
    static MyPlayer getInstance(String path) {
        String suffix = path.substring(path.lastIndexOf("."));
        switch (suffix) {
            case ".mp3":
                return new Mp3Player();
            case ".pcm":
                return new PcmPlayer();
            default:
                throw TtsException.of("不支持的音频文件：" + suffix);
        }
    }

    public static void autoPlay(String path) throws UnsupportedAudioFileException, IOException {
        getInstance(path).play(path);
    }
}
