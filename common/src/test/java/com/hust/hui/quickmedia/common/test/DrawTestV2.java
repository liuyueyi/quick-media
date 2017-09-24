package com.hust.hui.quickmedia.common.test;

import com.hust.hui.quickmedia.common.util.FontUtil;
import com.hust.hui.quickmedia.common.util.GraphicUtil;
import com.hust.hui.quickmedia.common.util.ImageUtil;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by yihui on 2017/9/20.
 */
public class DrawTestV2 {

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= 1000) ? 1000 : n + 1;
    }


    @Test
    public void testSize() {
        int cap = 4;
        System.out.println(tableSizeFor(cap));
    }

    @Test
    public void testDrawLine() {
        String[] msg = new String[]{
                "使用期限: 2017.08.01-2017.09.03",
                "适用范围: 双11超级活动日专项使用",
                "使用条件: -分享主播并完成签到",
                "         -玩车功能111任务!"
        };

        BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = GraphicUtil.getG2d(bufferedImage);
        g2d.setFont(FontUtil.DEFAULT_FONT);


        int y = 30;
        for (String str : msg) {
            g2d.drawString(str, 10, y);
            y += 26;
        }

        System.out.println("i");
    }


    @Test
    public void testDraw() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = GraphicUtil.getG2d(bufferedImage);
        g2d.setFont(FontUtil.DEFAULT_FONT);

        BufferedImage logo = ImageUtil.getImageByPath("mg.jpg");

        // image 实在 (x,y) 坐标处向右下角进行绘制
        g2d.drawImage(logo, 100, 100, 100, 100, null);


        // font 实在 (x,y) 坐标处右上角进行绘制
        g2d.drawString("这是一个测试", 100, 100);
        System.out.println("----");
    }


    @Test
    public void testRect() throws Exception {
        BufferedImage bf = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = GraphicUtil.getG2d(bf);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 500, 500);


        g2d.setColor(Color.RED);
        Stroke dash = new BasicStroke(2.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
                3.5f, new float[]{15, 10,}, 0f);
        g2d.setStroke(dash);
        g2d.drawRoundRect(20, 20, 100, 100, 20, 20);
        System.out.println("--");
    }


}
