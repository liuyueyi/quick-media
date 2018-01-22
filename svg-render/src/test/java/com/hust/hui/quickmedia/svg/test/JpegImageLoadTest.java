package com.hust.hui.quickmedia.svg.test;

import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yihui on 2018/1/21.
 */
public class JpegImageLoadTest {

    String url = "http://s17.mogucdn.com/mlcdn/c45406/170418_68lkjddg3bll08h9c9bk0d8ihkffi_800x1200.jpg";


    /**
     * 图片读取之后，颜色变红的测试
     */
    @Test
    public void testLoadRedImg() throws IOException {
        URL u = new URL(url);
        BufferedImage bf = ImageIO.read(u);
        ImageIO.write(bf, "jpg", new File("/tmp/out.jpg"));
        System.out.println("--over--");

    }



    @Test
    public void testLoadRedImg2() throws MalformedURLException {
        URL u = new URL(url);
        Image img = Toolkit.getDefaultToolkit().getImage(u);
        BufferedImage bf = toBufferedImage(img);
        System.out.println("eeee");
    }


    static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null),
                    image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }
}
