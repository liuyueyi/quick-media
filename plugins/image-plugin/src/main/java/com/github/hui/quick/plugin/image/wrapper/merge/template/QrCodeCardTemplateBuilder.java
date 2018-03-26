package com.github.hui.quick.plugin.image.wrapper.merge.template;


import com.github.hui.quick.plugin.base.ImageOperateUtil;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import com.github.hui.quick.plugin.image.wrapper.merge.cell.*;
import com.github.hui.quick.plugin.image.util.FontUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yihui on 2017/10/13.
 */
public class QrCodeCardTemplateBuilder {


    public static List<IMergeCell> build(BufferedImage logo,
                                         String name,
                                         List<String> desc,
                                         BufferedImage qrcode,
                                         String title) {
        List<IMergeCell> list = new ArrayList<>();

        list.add(buildBg());
        list.add(buildTextLogo(logo));
        list.addAll(buildTextInfo(name, desc));
        list.add(buildLine());
        list.add(buildQrCode(qrcode));
        list.add(buildQrCodeInfo());
        list.add(buildRectInfo());
        list.addAll(buildTitle(title));


        return list;
    }

    private static RectFillCell buildBg() {
        RectFillCell rectFillCell = RectFillCell.builder()
                .w(QrCodeCardTemplate.w)
                .h(QrCodeCardTemplate.h)
                .x(0)
                .y(0)
                .color(QrCodeCardTemplate.bg_color)
                .build();
        return rectFillCell;
    }


    private static ImgCell buildTextLogo(BufferedImage logo) {
        // logo
        logo = ImageOperateUtil.makeRoundImg(logo, false, null);
        return ImgCell.builder()
                .img(logo)
                .x(((QrCodeCardTemplate.text_size - QrCodeCardTemplate.text_logo_size) >>> 1) + QrCodeCardTemplate.text_x)
                .y(QrCodeCardTemplate.text_y)
                .w(QrCodeCardTemplate.text_logo_size)
                .h(QrCodeCardTemplate.text_logo_size)
                .build();
    }


    private static List<TextCell> buildTextInfo(String name, List<String> desc) {
        // 文案
        FontMetrics nameFontMetrics = FontUtil.getFontMetric(QrCodeCardTemplate.text_nameFont);
        int nameY = QrCodeCardTemplate.text_y + QrCodeCardTemplate.text_logo_size
                + QrCodeCardTemplate.text_line_space
                + nameFontMetrics.getHeight()
                + nameFontMetrics.getDescent();

        TextCell nameCell = new TextCell();
        nameCell.setFont(QrCodeCardTemplate.text_nameFont);
        nameCell.setColor(QrCodeCardTemplate.text_nameFont_color);
        nameCell.setStartX(QrCodeCardTemplate.text_x);
        nameCell.setStartY(nameY);
        nameCell.setEndX(QrCodeCardTemplate.text_x + QrCodeCardTemplate.text_size);
        nameCell.setEndY(nameY + nameFontMetrics.getHeight());
        nameCell.addText(name);
        nameCell.setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL);
        nameCell.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER);


        // 说明文案
        FontMetrics descFontMetrics = FontUtil.getFontMetric(QrCodeCardTemplate.text_descFont);
        int descY = nameY + nameFontMetrics.getHeight() + QrCodeCardTemplate.text_line_space;
        TextCell descCell = new TextCell();
        descCell.setFont(QrCodeCardTemplate.text_descFont);
        descCell.setColor(QrCodeCardTemplate.text_descFont_color);
        descCell.setStartX(QrCodeCardTemplate.text_x);
        descCell.setStartY(descY);
        descCell.setEndX(QrCodeCardTemplate.text_x + QrCodeCardTemplate.text_size);
        descCell.setEndY(descY + desc.size() * descFontMetrics.getHeight());
        // 单行超过限制的需要分割
        descCell.setTexts(desc);
        descCell.setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL);
        descCell.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER);


        return Arrays.asList(nameCell, descCell);
    }


    private static LineCell buildLine() {
        // line
        return LineCell.builder()
                .x1(QrCodeCardTemplate.line_x)
                .y1(QrCodeCardTemplate.line_y + QrCodeCardTemplate.line_h)
                .x2(QrCodeCardTemplate.line_x + QrCodeCardTemplate.line_w)
                .y2(QrCodeCardTemplate.line_y)
                .color(QrCodeCardTemplate.line_color)
                .build();
    }



    private static ImgCell buildQrCode(BufferedImage qrcode) {

        int qrCodeX = QrCodeCardTemplate.qrcode_x + ((QrCodeCardTemplate.qrcode_info_w - QrCodeCardTemplate.qrcode_size) >>> 1);

        return ImgCell.builder()
                .img(qrcode)
                .x(qrCodeX)
                .y(QrCodeCardTemplate.qrcode_y)
                .w(QrCodeCardTemplate.qrcode_size)
                .h(QrCodeCardTemplate.qrcode_size)
                .build();
    }



    private static TextCell buildQrCodeInfo() {
        Font font = QrCodeCardTemplate.qrcode_info_font;
        FontMetrics fontMetrics = FontUtil.getFontMetric(font);
        int startY = QrCodeCardTemplate.qrcode_y
                + QrCodeCardTemplate.qrcode_size
                + QrCodeCardTemplate.qrcode_info_padding
                + fontMetrics.getHeight();

        TextCell textCell = new TextCell();
        textCell.setStartX(QrCodeCardTemplate.qrcode_x);
        textCell.setEndX(QrCodeCardTemplate.w - QrCodeCardTemplate.border_space);
        textCell.setStartY(startY);
        textCell.setEndY(startY + fontMetrics.getHeight());
        textCell.setFont(font);
        textCell.setColor(QrCodeCardTemplate.qrcode_info_color);
        textCell.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER);
        textCell.addText("点击或长按关注");
        return textCell;
    }



    private static RectCell buildRectInfo() {
        RectCell rectCell = new RectCell();
        rectCell.setColor(Color.LIGHT_GRAY);
        rectCell.setX(QrCodeCardTemplate.border_space >>> 1);
        rectCell.setY(QrCodeCardTemplate.border_space >>> 1);
        rectCell.setW(QrCodeCardTemplate.w - QrCodeCardTemplate.border_space);
        rectCell.setH(QrCodeCardTemplate.h - QrCodeCardTemplate.border_space);

        return rectCell;
    }


    private static List<IMergeCell> buildTitle(String title) {
        Font titleFont = QrCodeCardTemplate.title_font;
        FontMetrics metrics = FontUtil.getFontMetric(titleFont);


        int w = QrCodeCardTemplate.w;
        int spacing = QrCodeCardTemplate.title_padding;


        int tw = metrics.stringWidth(title);

        RectFillCell rectFillCell = RectFillCell.builder()
                .x((w - tw - metrics.getHeight() - metrics.getHeight()) >>> 1 )
                .y(spacing >>> 1)
                .w(tw + metrics.getHeight() * 2)
                .h(spacing)
                .font(titleFont)
                .color(QrCodeCardTemplate.title_font_bg_color)
                .build();


        TextCell textCell = new TextCell();
        textCell.setStartX(0);
        textCell.setEndX(w);
        textCell.setStartY(spacing + titleFont.getSize() / 2 - metrics.getDescent());
        textCell.setEndY(textCell.getStartY());
        textCell.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER);
        textCell.setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL);
        textCell.addText(title);
        textCell.setFont(titleFont);
        textCell.setColor(QrCodeCardTemplate.title_font_color);


        return Arrays.asList(rectFillCell, textCell);
    }
}
