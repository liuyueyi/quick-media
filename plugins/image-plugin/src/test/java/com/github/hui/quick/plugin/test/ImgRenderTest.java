package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.base.gif.GifDecoder;
import com.github.hui.quick.plugin.base.gif.GifHelper;
import com.github.hui.quick.plugin.image.wrapper.pixel.ImgPixelWrapper;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.PixelStyleEnum;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yihui
 * @date 21/11/20
 */
public class ImgRenderTest {
    private static final String DEFAULT_CHAR_SET = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\\\"^`' ";

    /**
     * 基于颜色的灰度值，获取对应的字符
     *
     * @param g
     * @return
     */
    static char toChar(Color g) {
        double gray = 0.299 * g.getRed() + 0.578 * g.getGreen() + 0.114 * g.getBlue();
        return DEFAULT_CHAR_SET.charAt((int) (gray / 255 * DEFAULT_CHAR_SET.length()));
    }

    Color getAverage(BufferedImage image, int x, int y, int w, int h) {
        int red = 0;
        int green = 0;
        int blue = 0;

        int size = 0;
        for (int i = y; (i < h + y) && (i < image.getHeight()); i++) {
            for (int j = x; (j < w + x) && (j < image.getWidth()); j++) {
                int color = image.getRGB(j, i);
                red += ((color & 0xff0000) >> 16);
                green += ((color & 0xff00) >> 8);
                blue += (color & 0x0000ff);
                ++size;
            }
        }

        red = Math.round(red / (float) size);
        green = Math.round(green / (float) size);
        blue = Math.round(blue / (float) size);
        return new Color(red, green, blue);
    }

    private void parseChars(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        int size = 4;
        List<List<String>> list = new ArrayList<>();
        for (int y = 0; y < h; y += size) {
            List<String> line = new ArrayList<>();
            for (int x = 0; x < w; x += size) {
                Color avgColor = getAverage(img, x, y, size, size);
                line.add(String.valueOf(toChar(avgColor)));
            }
            list.add(line);
        }

        System.out.println("---------------------- 开始 ------------------------");
        for (List<String> line : list) {
            for (String s : line) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
        System.out.println("---------------------- 结束 ------------------------");
    }

    @Test
    public void testChars() throws Exception {
        // String file = "http://pic.dphydh.com/pic/newspic/2017-12-13/505831-1.png";
        String file = "http://5b0988e595225.cdn.sohucssohucs.com/images/20200410/76499041d3b144b58d6ed83f307df8a3.jpeg";
        BufferedImage img = ImageLoadUtil.getImageByPath(file);
//        img = GraphicUtil.scaleImg(200,200, img);
        parseChars(img);
        System.out.println("---over------");
    }

    private BufferedImage parseImg(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        // 创建新的灰度图片画板
        BufferedImage out = new BufferedImage(w, h, img.getType());
        Graphics2D g2d = out.createGraphics();
        g2d.setColor(null);
        g2d.fillRect(0, 0, w, h);

        int size = 4;
        Font font = new Font("宋体", Font.BOLD, size);
        g2d.setFont(font);
        List<List<String>> list = new ArrayList<>();
        for (int y = 0; y < h; y += size) {
            List<String> line = new ArrayList<>();
            for (int x = 0; x < w; x += size) {
                Color avgColor = getAverage(img, x, y, size, size);
//                g2d.setColor(avgColor);
                g2d.setColor(Color.BLACK);
                char ch = toChar(avgColor);
                g2d.drawString(String.valueOf(ch), x, y);
                line.add(String.valueOf(toChar(avgColor)));
            }
            list.add(line);
        }
        g2d.dispose();

        System.out.println("---------------------- 开始 ------------------------");
        for (List<String> line : list) {
            for (String s : line) {
                System.out.print(s + "");
            }
            System.out.println();
        }
        System.out.println("---------------------- 结束 ------------------------");

        return out;
    }

    @Test
    public void testChar() throws Exception {
//        String file = "http://pic.dphydh.com/pic/newspic/2017-12-13/505831-1.png";
        String file = "/Users/user/prince.jpeg";
        ImgPixelWrapper.build().setSourceImg(file).setBlockSize(4).setPixelType(PixelStyleEnum.CHAR_BLACK).build().asFile("/tmp/o.jpg");
//        System.out.println(res);
//        ImageIO.write(res, "o.jpg", new File("/tmp/o.jpg"));

        BufferedImage img = ImageLoadUtil.getImageByPath(file);
//        img = GraphicUtil.scaleImg(300,300, img);
        BufferedImage out = parseImg(img);
        System.out.println(out);


    }

    @Test
    public void testRender() throws IOException {
//        String file = "https://c-ssl.duitang.com/uploads/item/202003/29/20200329043918_2FUvk.thumb.400_0.gif";
//        String file = "https://c-ssl.duitang.com/uploads/item/201707/11/20170711194634_nTiK5.thumb.1000_0.gif";
        String file = "http://n.sinaimg.cn/sinacn/w390h219/20171231/0ac1-fyqefvw5238474.gif";
        // 从网络上下载图片
        GifDecoder decoder = new GifDecoder();
        decoder.read(FileReadUtil.getStreamByFileName(file));

        List<ImmutablePair<BufferedImage, Integer>> frames = new ArrayList<>();
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            BufferedImage img = decoder.getFrame(i);
            frames.add(ImmutablePair.of(parseImg(img), decoder.getDelay(i)));
        }

        File save = new File("/tmp/out3.gif");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GifHelper.saveGif(frames, outputStream);
        FileOutputStream out = new FileOutputStream(save);
        out.write(outputStream.toByteArray());
        out.flush();
        out.close();
        System.out.printf("渲染完成");
    }

    @Test
    public void testCharLines() {
//        String file = "http://pic.dphydh.com/pic/newspic/2017-12-13/505831-1.png";
        String file = "https://c-ssl.duitang.com/uploads/item/201707/11/20170711194634_nTiK5.thumb.1000_0.gif";
        List<List<String>> list = ImgPixelWrapper.build()
                .setSourceImg(file)
                .setBlockSize(3)
                .setRate(0.6)
                .setPixelType(PixelStyleEnum.CHAR_BLACK)
                .build()
                .asChars();
        for (List<String> s : list) {
            for (String t : s) {
                System.out.println(t);
            }

            System.out.println("------- 分割 -------");
        }
    }


    public void renderCharPhoto(String imgPath, String name, String saveFile) throws Exception {
        BufferedImage img = ImageIO.read(new File(imgPath));
        int w = img.getWidth(), h = img.getHeight();

        BufferedImage output = new BufferedImage(w * 24, h * 24, img.getType());
        Graphics2D g2d = output.createGraphics();

        try (InputStream inputStream = Files.newInputStream(Paths.get("D://MobileFile/潇洒手写体.ttf"))) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            g2d.setFont(font.deriveFont(Font.PLAIN, 20));
        }
        int index = 0;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                char ch = name.charAt((index++) % name.length());
                g2d.setColor(new Color(img.getRGB(x, y), true));
                g2d.drawString(String.valueOf(ch), x * 24 + 2, y * 24 + 2);
            }
        }

        g2d.dispose();
        ImageIO.write(output, "png", new File(saveFile));
    }

    public void rend(String imgPath, String saveFile) throws Exception {
        BufferedImage img = ImageIO.read(new File(imgPath));
        int w = img.getWidth(), h = img.getHeight();

        BufferedImage output = new BufferedImage(w * 4, h * 4, img.getType());
        Graphics2D g2d = GraphicUtil.getG2d(output);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                g2d.setColor(new Color(img.getRGB(x, y), true));
                g2d.fillRect(x * 4 + 1, y * 4 + 1, 2, 2);
            }
        }

        g2d.dispose();
        ImageIO.write(output, "png", new File(saveFile));
    }

    @Test
    public void testRenderCharPhoto() throws Exception {
//        renderCharPhoto("d://MobileFile/lyf.png", "刘亦菲", "D://MobileFile/o_lyf.png");
        rend("d://MobileFile/wx.png", "d://MobileFile/o_wx.png");
    }


    @Test
    public void testTt() throws IOException {
        String file = "d://MobileFile/wx.png";
        BufferedImage img = ImageLoadUtil.getImageByPath(file);
        Graphics2D g2d = GraphicUtil.getG2d(img);
        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.fillRect(0, 0, 10, 10);

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRect(0, 20, 10, 10);

        g2d.setColor(new Color(0, 0, 0, 255));
        g2d.fillRect(0, 40, 10, 10);
        g2d.dispose();
    }
}
