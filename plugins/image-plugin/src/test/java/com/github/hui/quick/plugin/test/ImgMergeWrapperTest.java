package com.github.hui.quick.plugin.test;


import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.base.awt.ImageOperateUtil;
import com.github.hui.quick.plugin.image.util.FontUtil;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import com.github.hui.quick.plugin.image.wrapper.merge.ImgMergeWrapper;
import com.github.hui.quick.plugin.image.wrapper.merge.cell.*;
import com.github.hui.quick.plugin.image.wrapper.merge.template.QrCodeCardTemplate;
import com.github.hui.quick.plugin.image.wrapper.merge.template.QrCodeCardTemplateBuilder;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yihui on 2017/10/13.
 */
public class ImgMergeWrapperTest {

    @Test
    public void gen() throws IOException {
        try {
            testCover("我师兄实在太稳健了", "cover");
            testCover("射雕英雄传", "cover2");
        } catch (Exception e) {
        }
    }

    public void testCover(String text, String out) throws IOException {
        int w = 276, h = 402;
        BufferedImage bg = ImageLoadUtil.getImageByPath("cover.jpg");


        TextCell textCell = new TextCell();
        textCell.setColor(ColorUtil.int2color(0xFFB49876));
        textCell.addText(text);
        textCell.setFont(new Font("苹方", Font.PLAIN, 32));
        int textX = 13 * w / (13 + 12 + 67);
        textCell.setStartX(textX);
        int textY = (int) (23.5 * h / (23.5 + 13 + 86)) + 28;
        textCell.setStartY(textY);
        textCell.setEndX(w - textX);
        textCell.setEndY(textY);
        textCell.setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL);
        textCell.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER);
        int textHeight = textCell.getDrawHeight();


        RectFillCell fillCell = new RectFillCell();
        textY = textY - 32;
        fillCell.setX(textX - 15);
        fillCell.setY(textY - 15);
        fillCell.setW(w - 2 * textX + 30);
        fillCell.setH(textHeight + 30);
        fillCell.setRadius(8);
        fillCell.setColor(Color.LIGHT_GRAY);


        RectCell rectCell = new RectCell();
        rectCell.setX(textX - 21);
        rectCell.setY(textY - 21);
        rectCell.setW(w - 2 * textX + 42);
        rectCell.setH(textHeight + 42);
        rectCell.setColor(Color.LIGHT_GRAY);
        rectCell.setRadius(12);
        rectCell.setStroke(new BasicStroke(2));


        Graphics2D g2d = GraphicUtil.getG2d(bg);
        List<IMergeCell> list = new ArrayList<>();
        list.add(rectCell);
        list.add(fillCell);
        list.add(textCell);
        list.stream().forEach(s -> s.draw(g2d));

        System.out.println("---绘制完成---");
        try {
            ImageIO.write(bg, "png", new File("/tmp/cover/" + out + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testCell() throws IOException {
        int w = 520, h = 260;
        BufferedImage bg = new BufferedImage(520, 260, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = GraphicUtil.getG2d(bg);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);


        List<IMergeCell> list = new ArrayList<>();

        // logo
        BufferedImage logo = ImageLoadUtil.getImageByPath("bg.png");
        logo = ImageOperateUtil.makeRoundImg(logo, false, null);
        ImgCell logoCell = ImgCell.builder().img(logo).x(60).y(20).w(100).h(100).build();
        list.add(logoCell);


        // 文案
        TextCell textCell = new TextCell();
        textCell.setFont(new Font("宋体", Font.BOLD, 22));
        textCell.setColor(Color.BLACK);
        textCell.setStartX(20);
        textCell.setStartY(140);
        textCell.setEndX(220);
        textCell.setEndY(180);
        textCell.addText("小灰灰Blog");
        textCell.setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL);
        textCell.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER);
        list.add(textCell);


        // 说明文案
        TextCell descCell = new TextCell();
        descCell.setFont(FontUtil.DEFAULT_FONT);
        descCell.setColor(Color.GRAY);
        descCell.setStartX(20);
        descCell.setStartY(180);
        descCell.setEndX(220);
        descCell.setEndY(240);
        descCell.addText("我是小灰灰Blog，哒哒");
        descCell.addText("下一行，小尾巴，哒哒哒");
        descCell.setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL);
        descCell.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER);
        list.add(descCell);


        // line
        LineCell line = LineCell.builder().x1(280).y1(20).x2(240).y2(240).color(Color.GRAY).build();
        list.add(line);


        BufferedImage qrCode = ImageLoadUtil.getImageByPath("xcx.jpg");
        ImgCell imgCell = ImgCell.builder().img(qrCode).x(300).y(30).w(200).h(200).build();
        list.add(imgCell);


        list.stream().forEach(s -> s.draw(g2d));

        System.out.println("---绘制完成---");
        try {
            ImageIO.write(bg, "jpg", new File("/tmp/merge.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testTemplate() throws IOException {
        BufferedImage logo = ImageLoadUtil.getImageByPath("logo.jpg");
        BufferedImage qrCode = ImageLoadUtil.getImageByPath("QrCode.jpg");
        String name = "小灰灰Blog";
        List<String> desc = Arrays.asList(" 无聊的码农，不定时分享各种博文 ");

        int w = QrCodeCardTemplate.w, h = QrCodeCardTemplate.h;
        List<IMergeCell> list = QrCodeCardTemplateBuilder.build(logo, name, desc, qrCode, "微 信 公 众 号");

        BufferedImage bg = ImgMergeWrapper.merge(list, w, h);

        try {
            ImageIO.write(bg, "jpg", new File("/tmp/merge.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testMerge() throws IOException {
        BufferedImage img1 = ImageLoadUtil.getImageByPath("QrCode.jpg");
        BufferedImage img2 = ImageLoadUtil.getImageByPath("blogQrcode.png");


        ImgCell imgCell = ImgCell.builder().img(img1).x(0).y(0).build();


        LineCell lineCell =
                LineCell.builder().x1(img1.getWidth() / 3).x2(img1.getWidth() * 7 / 6).y1(img1.getHeight() + 4)
                        .y2(img1.getHeight() + 4).color(Color.LIGHT_GRAY).dashed(true).build();


        ImgCell imgCell2 = ImgCell.builder().img(img2).x(img1.getWidth() / 2).y(img1.getHeight() + 4).build();

        BufferedImage ans = ImgMergeWrapper
                .merge(Arrays.asList(imgCell, lineCell, imgCell2), img1.getWidth() / 2 + img2.getWidth(),
                        img1.getHeight() + 4 + img2.getHeight(), Color.WHITE);
        ImageIO.write(ans, "jpg", new File("/tmp/ansV3.jpg"));
    }
}
