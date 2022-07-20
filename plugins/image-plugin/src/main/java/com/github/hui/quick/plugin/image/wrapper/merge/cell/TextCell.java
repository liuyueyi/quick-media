package com.github.hui.quick.plugin.image.wrapper.merge.cell;


import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.image.helper.CalculateHelper;
import com.github.hui.quick.plugin.image.util.FontUtil;
import com.github.hui.quick.plugin.image.util.PunctuationUtil;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by yihui on 2017/10/13.
 */
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


    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getLineSpace() {
        return lineSpace;
    }

    public void setLineSpace(int lineSpace) {
        this.lineSpace = lineSpace;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public ImgCreateOptions.DrawStyle getDrawStyle() {
        return drawStyle;
    }

    public void setDrawStyle(ImgCreateOptions.DrawStyle drawStyle) {
        this.drawStyle = drawStyle;
    }

    public ImgCreateOptions.AlignStyle getAlignStyle() {
        return alignStyle;
    }

    public void setAlignStyle(ImgCreateOptions.AlignStyle alignStyle) {
        this.alignStyle = alignStyle;
    }

    public void addText(String text) {
        if (texts == null) {
            texts = new ArrayList<>();
        }

        texts.add(text);
    }

    /**
     * 文本框占用的高度, 水平绘制时有效
     *
     * @return
     */
    public int getDrawHeight() {
        FontMetrics fontMetrics = FontUtil.getFontMetric(font);
        int size = batchSplitText(texts, fontMetrics).size();
        return size * (fontMetrics.getHeight() + lineSpace);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setFont(font);

        FontMetrics fontMetrics = FontUtil.getFontMetric(font);
        int tmpHeight = fontMetrics.getHeight(), tmpW = font.getSize() >>> 1;
        int tmpY = startY, tmpX = startX;
        int offsetX = drawStyle == ImgCreateOptions.DrawStyle.VERTICAL_LEFT ?
                (font.getSize() + fontMetrics.getDescent() + lineSpace) :
                -(font.getSize() + fontMetrics.getDescent() + lineSpace);
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
                    tmpX = startX + (PunctuationUtil.isPunctuation(chars[i]) ? tmpW : 0);
                    g2d.drawString(chars[i] + "", tmpX, tmpY);
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
            for (String t : texts) {
                ans.addAll(Arrays.asList(CalculateHelper.splitStr(t, lineLen, fontMetrics)));
            }
        } else {
            int lineLen = Math.abs(endY - startY);
            for (String t : texts) {
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
            return startY + ((endY - startY - size) >> 1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TextCell textCell = (TextCell) o;
        return lineSpace == textCell.lineSpace && startX == textCell.startX && startY == textCell.startY &&
                endX == textCell.endX && endY == textCell.endY && Objects.equals(texts, textCell.texts) &&
                Objects.equals(color, textCell.color) && Objects.equals(font, textCell.font) &&
                drawStyle == textCell.drawStyle && alignStyle == textCell.alignStyle;
    }

    @Override
    public int hashCode() {

        return Objects.hash(texts, color, font, lineSpace, startX, startY, endX, endY, drawStyle, alignStyle);
    }

    @Override
    public String toString() {
        return "TextCell{" + "texts=" + texts + ", color=" + color + ", font=" + font + ", lineSpace=" + lineSpace +
                ", startX=" + startX + ", startY=" + startY + ", endX=" + endX + ", endY=" + endY + ", drawStyle=" +
                drawStyle + ", alignStyle=" + alignStyle + '}';
    }

    public static TextCell.Builder builder() {
        return new TextCell.Builder();
    }

    public static class Builder {
        private List<String> texts;

        private Color color = Color.black;

        private Font font = FontUtil.DEFAULT_FONT;

        private int lineSpace;

        private int startX, startY;
        private int endX, endY;

        /**
         * 绘制样式，水平，垂直
         */
        private ImgCreateOptions.DrawStyle drawStyle = ImgCreateOptions.DrawStyle.HORIZONTAL;

        /**
         * 对其方式
         */
        private ImgCreateOptions.AlignStyle alignStyle = ImgCreateOptions.AlignStyle.LEFT;

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder color(int color) {
            return color(ColorUtil.int2color(color));
        }

        public Builder font(Font font) {
            this.font = font;
            return this;
        }

        public Builder lineSpace(int lineSpace) {
            this.lineSpace = lineSpace;
            return this;
        }

        public Builder startX(int startX) {
            this.startX = startX;
            return this;
        }

        public Builder startY(int startY) {
            this.startY = startY;
            return this;
        }

        public Builder endX(int endX) {
            this.endX = endX;
            return this;
        }

        public Builder endY(int endY) {
            this.endY = endY;
            return this;
        }

        public Builder text(String text) {
            if (texts == null) {
                texts = new ArrayList<>();
            }

            texts.add(text);
            return this;
        }

        public Builder drawStyle(ImgCreateOptions.DrawStyle drawStyle) {
            this.drawStyle = drawStyle;
            return this;
        }

        public Builder alignStyle(ImgCreateOptions.AlignStyle alignStyle) {
            this.alignStyle = alignStyle;
            return this;
        }

        public TextCell build() {
            TextCell textCell = new TextCell();
            textCell.color = color;
            textCell.lineSpace = lineSpace;
            textCell.font = font;
            textCell.startX = startX;
            textCell.startY = startY;
            textCell.endX = endX;
            textCell.endY = endY;
            textCell.texts = texts;
            textCell.drawStyle = drawStyle;
            textCell.alignStyle = alignStyle;
            return textCell;
        }
    }
}
