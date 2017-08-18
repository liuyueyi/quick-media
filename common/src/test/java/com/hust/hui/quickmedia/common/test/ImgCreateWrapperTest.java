package com.hust.hui.quickmedia.common.test;

import com.hust.hui.quickmedia.common.image.ImgCreateOptions;
import com.hust.hui.quickmedia.common.image.ImgCreateWrapper;
import com.hust.hui.quickmedia.common.util.Base64Util;
import com.hust.hui.quickmedia.common.util.FileReadUtil;
import com.hust.hui.quickmedia.common.util.ImageUtil;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by yihui on 2017/8/17.
 */
public class ImgCreateWrapperTest {

    @Test
    public void testGenImg() throws IOException {
        int w = 400;
        int leftPadding = 10;
        int topPadding = 200;
        int bottomPadding = 200;
        int linePadding = 10;
        Font font = new Font("宋体", Font.PLAIN, 18);

        ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
                .setImgW(w)
                .setLeftPadding(leftPadding)
                .setTopPadding(topPadding)
                .setBottomPadding(bottomPadding)
                .setLinePadding(linePadding)
                .setFont(font)
                .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                .setBgImg(ImageUtil.getImageByPath("qrbg.jpg"))
                ;
//                .setBgColor(Color.GREEN);


        BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
        String line;
        int index = 0;
        while ((line = reader.readLine()) != null) {
            build.drawContent(line);

            if(++index == 5) {
                build.drawImage(ImageUtil.getImageByPath("mg.jpg"));
            }

            if (index == 7) {
                build.setFontSize(25);
            }

            if (index == 8) {
                build.setFontColor(Color.RED);
            }
        }

        BufferedImage img = build.asImage();
        String out = Base64Util.encode(img, "png");
        System.out.println("<img src=\"data:image/png;base64," + out + "\" />");
    }

}
