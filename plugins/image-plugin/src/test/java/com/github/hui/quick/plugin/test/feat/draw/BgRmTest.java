package com.github.hui.quick.plugin.test.feat.draw;

import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.test.util.BorderDetectUtil;
import com.jhlabs.image.DoGFilter;
import com.jhlabs.image.ImageUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class BgRmTest {
    private static final String PRE = "D://quick-media/";


    /**
     * 获取图片边框
     *
     * @param path
     * @return
     * @throws IOException
     */
    private BufferedImage borderDetected(String path) throws IOException {
        BufferedImage src = ImageLoadUtil.getImageByPath(path);
        src = ImageUtils.convertImageToARGB(src);

        // 高斯差分边缘检测
        DoGFilter filter = new DoGFilter();
        filter.setRadius1(0);
        filter.setRadius2(10);
        filter.setInvert(false);
        BufferedImage output = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

        return filter.filter(src, output);
    }

    @Test
    public void testRm() throws IOException {
        String img = "kdy";
        String path = PRE + img + ".jpg";

        BufferedImage source = autoRemoveBg(path);
        ImageIO.write(source, "png", new File(PRE + "bgf_" + img + ".png"));
    }

    private BufferedImage autoRemoveBg(String path) throws IOException {
        BufferedImage detect = borderDetected(path);
        int w = detect.getWidth(), h = detect.getHeight();
        Byte[][] rect = new Byte[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int color = detect.getRGB(x, y);
                if (color == 0 || color == 0xFF000000) {
                    rect[x][y] = 0;
                } else {
                    rect[x][y] = 1;
                }
            }
        }
        detect = null;
        System.out.println("-----------------------");
        StringBuilder str = new StringBuilder();
        File file = new File(PRE + "t_out.txt");
        FileOutputStream stream = new FileOutputStream(file);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (rect[x][y] == 0) {
                    stream.write(' ');
                    System.out.print("  ");
                } else {
                    stream.write('1');
                    System.out.print("1 ");
                }
            }
            stream.write('\n');
            System.out.println();
        }
        stream.flush();
        stream.close();
        System.out.println("-----------------------");

        BorderDetectUtil.Border<Byte> border = new BorderDetectUtil.Border<>(rect);


        List<List<Integer>> lines = BorderDetectUtil.filterLine(border);

        BufferedImage src = ImageLoadUtil.getImageByPath(path);
        BufferedImage output = GraphicUtil.createImg(w, h, null);
        for (List<Integer> line : lines) {
            for (Integer key : line) {
                Point point = border.key2point(key);
                output.setRGB(point.x, point.y, src.getRGB(point.x, point.y));
            }
            System.out.println("------");
        }

        List<List<Integer>> borders = BorderDetectUtil.filterBorder(border);
        for (List<Integer> sub : borders) {
            for (Integer key : sub) {
                Point point = border.key2point(key);
                output.setRGB(point.x, point.y, src.getRGB(point.x, point.y));
            }
        }
        src = null;
        return output;
    }
}
