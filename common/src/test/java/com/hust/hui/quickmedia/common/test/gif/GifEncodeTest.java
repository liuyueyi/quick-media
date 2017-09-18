package com.hust.hui.quickmedia.common.test.gif;

import com.hust.hui.quickmedia.common.gif.AnimatedGifEncoder;
import com.hust.hui.quickmedia.common.gif.GifDecoder;
import com.hust.hui.quickmedia.common.util.FileReadUtil;
import com.hust.hui.quickmedia.common.util.ImageUtil;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by yihui on 2017/9/14.
 */
public class GifEncodeTest {

    @Test
    public void testEncode() throws IOException {
        BufferedImage src1 = ImageUtil.getImageByPath("bg.png");
        BufferedImage src2 = ImageUtil.getImageByPath("logo.jpg");

        AnimatedGifEncoder e = new AnimatedGifEncoder();
        e.setRepeat(0);
        e.start("laoma.gif");
        e.setDelay(300); // 1 frame per sec
        e.addFrame(src1);
        e.setDelay(100);
        e.addFrame(src2);
        e.setDelay(100);
        //  e.addFrame(src2);
        e.finish();
    }


    @Test
    public void testGifLoader() throws IOException {
        GifDecoder gifDecoder = new GifDecoder();
        gifDecoder.read(FileReadUtil.getStreamByFileName("laoma.gif"));

        BufferedImage bf;
        for(int i = 0 ; i < gifDecoder.getFrameCount(); i++) {
            bf = gifDecoder.getFrame(i);
            System.out.println("--" + i);
        }
    }

}
