package com.github.hui.quick.plugin.tts.palyer;

import com.github.hui.quick.plugin.tts.exceptions.TtsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * @author zh-hq
 * @date 2023/3/30
 */
public class PcmPlayer implements MyPlayer {
    public static final Logger log = LoggerFactory.getLogger(PcmPlayer.class);

    @Override
    public void play(String path) throws IOException, UnsupportedAudioFileException {
        File file = new File(path);
        if (!file.exists()) {
            throw TtsException.of("文件不存在");
        }
        AudioInputStream stream = AudioSystem.getAudioInputStream(file);
        playPcm(stream);
    }

    protected void playPcm(AudioInputStream stream) {
        AudioFormat target = stream.getFormat();
        DataLine.Info dinfo = new DataLine.Info(SourceDataLine.class, target, AudioSystem.NOT_SPECIFIED);
        SourceDataLine line = null;
        int len = -1;
        try {
            line = (SourceDataLine) AudioSystem.getLine(dinfo);
            line.open(target);
            line.start();
            byte[] buffer = new byte[1024];
            while ((len = stream.read(buffer)) > 0) {
                line.write(buffer, 0, len);
            }
            line.drain();
            line.stop();
            line.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
