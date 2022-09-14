package com.github.hui.quick.plugin.test.v3.extend;

import com.github.hui.quick.plugin.qrcode.v3.draw.IDrawing;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.RenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.SvgTemplate;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.SymbolSvgTag;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

/**
 * 如自定义的签名前置图
 *
 * @author YiHui
 * @date 2022/9/14
 */
public class SelfDefineDrawStyle implements IDrawing {
    /**
     * 自定义的前置图 + 签名的绘制样式
     *
     * @param g2d
     * @param renderDot
     */
    @Override
    public void drawAsImg(Graphics2D g2d, RenderDot renderDot) {
        QrResource resource = renderDot.getResource();
        g2d.setFont(resource.getFont(22));
        String text = resource.getText();
        int y = renderDot.getSize();
        int offset = g2d.getFontMetrics().getHeight();
        for (String str : StringUtils.split(text, "\n")) {
            g2d.drawString(str, 22, y + offset);
            y += offset;
        }
    }

    /**
     * 自定义的四叶草绘制样式
     *
     * @param svg
     * @param renderDot
     */
    @Override
    public void drawAsSvg(SvgTemplate svg, RenderDot renderDot) {
        svg.addSymbol("<symbol id=\"leaf\" viewBox=\"0 0 1024 1024\"><path d=\"M859.867 448.403c136.9-63.175 54.44-193.784-98.066-152.726-84.072 22.603-158.738 111.715-218.313 145.103 33.388-59.575 122.469-134.255 145.104-218.281C729.65 69.96 599.041-12.468 535.881 124.402c-63.175-136.87-193.784-54.441-152.742 98.097 22.65 84.025 111.731 158.706 145.103 218.281-59.56-33.388-134.208-122.5-218.296-145.103-152.507-41.058-234.951 89.551-98.05 152.726-136.901 63.16-54.457 193.768 98.05 152.726 84.088-22.618 158.737-111.731 218.296-145.103-33.372 59.576-122.453 134.24-145.103 218.328-41.042 152.508 89.566 234.937 152.742 98.051 63.16 136.886 193.769 54.457 152.711-98.051-22.635-84.088-111.716-158.752-145.104-218.328 59.575 33.372 134.24 122.485 218.313 145.103 152.507 41.042 234.967-89.566 98.066-152.726z\" fill=\"#BFCD37\" p-id=\"1611\"></path></symbol>");
        svg.addTag(new SymbolSvgTag().setSvgId("leaf")
                .setX(renderDot.getX()).setY(renderDot.getY())
                .setW(renderDot.getSize()).setH(renderDot.getSize()));
    }
}
