package com.hust.hui.quickmedia.common.test.img;

import com.hust.hui.quickmedia.common.img.create.ImgCreateOptions;
import com.hust.hui.quickmedia.common.img.merge.ImgMergeWrapper;
import com.hust.hui.quickmedia.common.img.merge.cell.*;
import com.hust.hui.quickmedia.common.img.merge.template.QrCodeCardTemplate;
import com.hust.hui.quickmedia.common.img.merge.template.QrCodeCardTemplateBuilder;
import com.hust.hui.quickmedia.common.util.FontUtil;
import com.hust.hui.quickmedia.common.util.GraphicUtil;
import com.hust.hui.quickmedia.common.util.ImageExUtil;
import com.hust.hui.quickmedia.common.util.ImageUtil;
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
    public void testCell() throws IOException {
        int w = 520, h = 260;
        BufferedImage bg = new BufferedImage(520, 260, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = GraphicUtil.getG2d(bg);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);


        List<IMergeCell> list = new ArrayList<>();

        // logo
        BufferedImage logo = ImageUtil.getImageByPath("bg.png");
        logo = ImageExUtil.makeRoundImg(logo, false, null);
        ImgCell logoCell = ImgCell.builder()
                .img(logo)
                .x(60)
                .y(20)
                .w(100)
                .h(100)
                .build();
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
        LineCell line = LineCell.builder().x1(280)
                .y1(20)
                .x2(240)
                .y2(240)
                .color(Color.GRAY)
                .build();
        list.add(line);


        BufferedImage qrCode = ImageUtil.getImageByPath("/Users/yihui/Desktop/xcx.jpg");
        ImgCell imgCell = ImgCell.builder()
                .img(qrCode)
                .x(300)
                .y(30)
                .w(200)
                .h(200)
                .build();
        list.add(imgCell);


        list.stream().forEach(s -> s.draw(g2d));

        System.out.println("---绘制完成---");
        try {
            ImageIO.write(bg, "jpg", new File("/Users/yihui/Desktop/merge.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testTemplate() throws IOException {
        BufferedImage logo = ImageUtil.getImageByPath("logo.jpg");
        BufferedImage qrCode = ImageUtil.getImageByPath("/Users/yihui/Desktop/QrCode.jpg");
        String name = "小灰灰Blog";
        List<String> desc = Arrays.asList(" 无聊的码农，不定时分享各种博文 ");

//34485d
        int w = QrCodeCardTemplate.w, h = QrCodeCardTemplate.h;
        List<IMergeCell> list = QrCodeCardTemplateBuilder.build(logo, name, desc, qrCode, "微 信 公 众 号");

        BufferedImage bg = ImgMergeWrapper.merge(list, w, h);

        try {
            ImageIO.write(bg, "jpg", new File("/Users/yihui/Desktop/merge.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testMerge() throws IOException {
        BufferedImage img1 = ImageUtil.getImageByPath("/Users/yihui/Desktop/xcx/wxBlog.jpg");
        BufferedImage img2 = ImageUtil.getImageByPath("/Users/yihui/Desktop/xcx/OscBlog.jpg");


        ImgCell imgCell = ImgCell.builder()
                .img(img1)
                .x(0)
                .y(0)
                .build();


        LineCell lineCell = LineCell.builder()
                .x1(img1.getWidth() / 3)
                .x2(img1.getWidth() * 7 / 6)
                .y1(img1.getHeight() + 4)
                .y2(img1.getHeight() + 4)
                . color(Color.LIGHT_GRAY)
                .dashed(true)
                .build();


        ImgCell imgCell2 = ImgCell.builder()
                .img(img2)
                .x(img1.getWidth() / 2)
                .y(img1.getHeight() + 4)
                .build();

        BufferedImage ans = ImgMergeWrapper.merge(Arrays.asList(imgCell, lineCell, imgCell2),
                img1.getWidth() / 2 + img2.getWidth(),
                img1.getHeight() + 4 + img2.getHeight(),
                Color.WHITE);
        ImageIO.write(ans, "jpg", new File("/Users/yihui/Desktop/xcx/ansV3.jpg"));
    }
}
