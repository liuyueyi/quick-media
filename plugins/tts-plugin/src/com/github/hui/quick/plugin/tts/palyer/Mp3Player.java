package com.github.hui.quick.plugin.tts.palyer;

import com.github.hui.quick.plugin.tts.exceptions.TtsException;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * @author zh-hq
 * @date 2023/3/30
 */
public class Mp3Player extends PcmPlayer {

    @Override
    public void play(String path) throws IOException, UnsupportedAudioFileException {
        File file = new File(path);
        if (!file.exists() || !path.toLowerCase().endsWith(".mp3")) {
            throw TtsException.of("文件不存在");
        }
        AudioInputStream stream = null;
        //使用 mp3spi 解码 mp3 音频文件
        MpegAudioFileReader mp = new MpegAudioFileReader();
        stream = mp.getAudioInputStream(file);

        AudioFormat baseFormat = stream.getFormat();
        //设定输出格式为pcm格式的音频文件
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        // 输出到音频
        stream = AudioSystem.getAudioInputStream(format, stream);
        playPcm(stream);
    }
}
