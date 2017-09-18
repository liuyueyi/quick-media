package com.hust.hui.quickmedia.common.gif;

import com.hust.hui.quickmedia.common.util.FileReadUtil;
import com.hust.hui.quickmedia.common.util.FileUtil;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

/**
 * Created by yihui on 2017/9/14.
 */
public class GifHelper {

    public static int loadGif(String gif, List<BufferedImage> list) throws IOException {
        return loadGif(FileReadUtil.getStreamByFileName(gif), list);
    }


    public static int loadGif(InputStream stream, List<BufferedImage> list) {
        GifDecoder decoder = new GifDecoder();
        decoder.read(stream);

        int delay = 100;
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            if (delay > decoder.getDelay(i)) {
                delay = decoder.getDelay(i);
            }
            list.add(decoder.getFrame(i));
        }

        return delay;
    }


    public static void saveGif(List<BufferedImage> frames, int delay, String out) throws FileNotFoundException {
        FileUtil.mkDir(new File(out));
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(out));
        saveGif(frames, delay, outputStream);
    }


    public static void saveGif(List<BufferedImage> frames, int delay, OutputStream out) {
        AnimatedGifEncoder e = new AnimatedGifEncoder();
        e.setRepeat(0);
        e.start(out);

        e.setDelay(delay);
        for (BufferedImage img : frames) {
            e.setDelay(delay);
            e.addFrame(img);
        }
        e.setDelay(3000);
        e.addFrame(frames.get(frames.size() - 1));
        e.finish();
    }
}
