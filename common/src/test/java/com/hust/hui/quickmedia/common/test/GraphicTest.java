package com.hust.hui.quickmedia.common.test;

import com.hust.hui.quickmedia.common.image.ImgCreateOptions;
import com.hust.hui.quickmedia.common.util.Base64Util;
import com.hust.hui.quickmedia.common.util.FileReadUtil;
import com.hust.hui.quickmedia.common.util.GraphicUtil;
import com.hust.hui.quickmedia.common.util.ImageUtil;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by yihui on 2017/7/26.
 */
public class GraphicTest {


    @Test
    public void testDrawCircle() throws IOException {
        int w = 100;
        int h = 100;
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();

        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);
        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double(40, 40, 40, 40));


        String img64 = Base64Util.encode(bufferedImage, "png");
        System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");


        System.out.println("-----------");


        // 三角形
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);
        g2.setColor(Color.RED);


        int px1[] = {10, 50, 90};//首末点相重,才能画多边形
        int py1[] = {50, 10, 50};
        g2.fillPolygon(px1, py1, 3);
        img64 = Base64Util.encode(bufferedImage, "png");
        System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");


        System.out.println("-----------");


        // 五角星
        int x = 10;
        int y = 10;
        int tw = 80;
        int th = 80;


        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);
        g2.setColor(Color.RED);
        int bottomX = (int) (x + tw / 2.0 - th * 0.5 * 0.7265);
        int px2[] = {x + tw / 2, x + tw, x + tw - bottomX, x + bottomX, x};

        int leftY = (int) (y + th * 0.5 - tw * 0.5 * 0.3249);
        int py2[] = {y, y + leftY, y + th, y + th, y + leftY};
        g2.fillPolygon(px2, py2, 5);
        img64 = Base64Util.encode(bufferedImage, "png");
        System.out.println("五边形: <img src=\"data:image/png;base64," + img64 + "\" />");


        // 这是一个失败的五角星
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);
        g2.setColor(Color.RED);
        int px3[] = {x + tw / 2, x + tw - bottomX, x, x + tw, x + bottomX};
        int py3[] = {y, y + th, y + leftY, y + leftY, y + th};
        g2.fillPolygon(px3, py3, 5);
        img64 = Base64Util.encode(bufferedImage, "png");
        System.out.println("五角星: <img src=\"data:image/png;base64," + img64 + "\" />");


        BufferedImage log = ImageUtil.getImageByPath("mg.png");

        // 绘制图片
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);
        g2.setColor(Color.WHITE);
        g2.drawImage(log, x, y, tw, th, null);

        img64 = Base64Util.encode(bufferedImage, "png");
        System.out.println("图片: <img src=\"data:image/png;base64," + img64 + "\" />");


        g2.dispose();


    }


    @Test
    public void testDrawText() throws Exception {
        BufferedImage image = convertTextToGraphic("你好", new Font("Arial", Font.PLAIN, 18));
        //write BufferedImage to file
        String img64 = Base64Util.encode(image, "png");
        System.out.println("图片: <img src=\"data:image/png;base64," + img64 + "\" />\n");
    }

    public BufferedImage convertTextToGraphic(String text, Font font) {

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();
        return img;
    }


    @Test
    public void testDra() throws IOException {
        int w = 400;
        int h = 1000;
        ImgCreateOptions options = new ImgCreateOptions();
        options.setImgW(w);
        options.setTopPadding(50);
        options.setLeftPadding(20);
        options.setBottomPadding(50);
        options.setLinePadding(10);


        StringBuilder builder = new StringBuilder();

        BufferedReader reader = FileReadUtil.createLineRead("text/poem2.txt");
        String line = reader.readLine();
        while (line != null) {
            builder.append(line).append("\n");
            line = reader.readLine();
        }

        BufferedImage bImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bImg.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);


        GraphicUtil.drawContent(g2d, builder.toString(), 50, options);
        System.out.println("========");
    }


    @Test
    public void testDrawVerticalContent() {
        int w = 500;
        int h = 300;
        BufferedImage bf = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = GraphicUtil.getG2d(bf);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(Color.BLACK);


        Font font = new Font("宋体", Font.BOLD, 18);
        String content = "正正正正真之正震泽后给你整整的这单打发AD诗酒风流";

        ImgCreateOptions option = new ImgCreateOptions();
        option.setFont(font);
        option.setImgH(h);
        option.setImgW(w);
        option.setTopPadding(18);

        g2d.drawLine(0, 18, w, 18);

        option.setBottomPadding(18);
        g2d.drawLine(0, h - 18, w, h - 18);

        option.setLinePadding(18);

        GraphicUtil.drawVerticalContent(g2d, content, 10, option);
        System.out.println("---");
    }


    @Test
    public void testSpliteVerStr() {
        String content = "这是一个待绘制的文本长度，期待自动换行";
        int len = 100;
        BufferedImage bf = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = GraphicUtil.getG2d(bf);
        Font font = new Font("宋体", Font.BOLD, 18);
        g2d.setFont(font);
        FontMetrics fontMetrics = g2d.getFontMetrics();

        String[] strs = GraphicUtil.splitVerticalStr(content, len, fontMetrics);
        System.out.println("ans: " + Arrays.asList(strs));
    }

}
