package com.github.hui.quick.plugin.image.wrapper.merge.cell;


import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import com.github.hui.quick.plugin.image.helper.CalculateHelper;
import com.github.hui.quick.plugin.image.util.FontUtil;
import com.github.hui.quick.plugin.image.util.PunctuationUtil;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yihui on 2017/10/13.
 */
@Data
public class TextCell implements IMergeCell {

    private List<String> texts;

    private Color color = Color.black;

    private Font font = FontUtil.DEFAULT_FONT;


    private int lineSpace;

    private int startX, startY;
    private int endX, endY;


    /**
     * 绘制样式
     */
    private ImgCreateOptions.DrawStyle drawStyle = ImgCreateOptions.DrawStyle.HORIZONTAL;


    private ImgCreateOptions.AlignStyle alignStyle = ImgCreateOptions.AlignStyle.LEFT;


    public void addText(String text) {
        if (texts == null) {
            texts = new ArrayList<>();
        }

        texts.add(text);
    }


    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setFont(font);

        FontMetrics fontMetrics = FontUtil.getFontMetric(font);
        int tmpHeight = fontMetrics.getHeight(), tmpW = font.getSize() >>> 1;
        int tmpY = startY, tmpX = startX;
        int offsetX = drawStyle == ImgCreateOptions.DrawStyle.VERTICAL_LEFT
                ? (font.getSize() + fontMetrics.getDescent() + lineSpace)
                : -(font.getSize() + fontMetrics.getDescent() + lineSpace);
        List<String> splitText = batchSplitText(texts, fontMetrics);
        for (String info : splitText) {
            if (drawStyle == ImgCreateOptions.DrawStyle.HORIZONTAL) {
                g2d.drawString(info, calculateX(info, fontMetrics), tmpY);

                // 换行，y坐标递增一位
                tmpY += fontMetrics.getHeight() + lineSpace;
            } else {
                char[] chars = info.toCharArray();

                tmpY = calculateY(info, fontMetrics);
                for (int i = 0; i < chars.length; i++) {
                    tmpX = PunctuationUtil.isPunctuation(chars[i]) ? tmpW : 0;
                    g2d.drawString(chars[i] + "",
                            tmpX + (PunctuationUtil.isPunctuation(chars[i]) ? tmpW : 0),
                            tmpY);
                    tmpY += tmpHeight;
                }

                // 换一列
                tmpX += offsetX;
            }
        }
    }


    // 若单行文本超过长度限制，则自动进行换行
    private List<String> batchSplitText(List<String> texts, FontMetrics fontMetrics) {
        List<String> ans = new ArrayList<>();
        if (drawStyle == ImgCreateOptions.DrawStyle.HORIZONTAL) {
            int lineLen = Math.abs(endX - startX);
            for(String t: texts) {
                ans.addAll(Arrays.asList(CalculateHelper.splitStr(t, lineLen, fontMetrics)));
            }
        } else {
            int lineLen = Math.abs(endY - startY);
            for(String t: texts) {
                ans.addAll(Arrays.asList(CalculateHelper.splitVerticalStr(t, lineLen, fontMetrics)));
            }
        }
        return ans;
    }


    private int calculateX(String text, FontMetrics fontMetrics) {
        if (alignStyle == ImgCreateOptions.AlignStyle.LEFT) {
            return startX;
        } else if (alignStyle == ImgCreateOptions.AlignStyle.RIGHT) {
            return endX - fontMetrics.stringWidth(text);
        } else {
            return startX + ((endX - startX - fontMetrics.stringWidth(text)) >>> 1);
        }

    }


    private int calculateY(String text, FontMetrics fontMetrics) {
        if (alignStyle == ImgCreateOptions.AlignStyle.TOP) {
            return startY;
        } else if (alignStyle == ImgCreateOptions.AlignStyle.BOTTOM) {
            int size = fontMetrics.stringWidth(text) + fontMetrics.getDescent() * (text.length() - 1);
            return endY - size;
        } else {
            int size = fontMetrics.stringWidth(text) + fontMetrics.getDescent() * (text.length() - 1);
            return startY + ((endY - endX - size) >>> 1);
        }
    }
}
