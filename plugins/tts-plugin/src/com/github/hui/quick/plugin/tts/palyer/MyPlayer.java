package com.github.hui.quick.plugin.tts.palyer;


import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * @author zh-hq
 * @date 2023/3/30
 */
public interface MyPlayer {

    /**
     * 播放音频
     *
     * @param path
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    void play(String path) throws IOException, UnsupportedAudioFileException;
}
