package com.github.hui.quick.plugin.qrcode.v3.tpl;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.util.json.JsonUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResourcePool;
import com.github.hui.quick.plugin.qrcode.v3.req.*;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 图片模板文件解析
 *
 * @author YiHui
 * @date 2022/8/13
 */
public class ImgTplParse extends BaseTplParse {

    public static void imgTemplateParseAndInit(QrCodeV3Options v3Options, String template) {
        ImgTemplate img = JsonUtil.toObj(template, ImgTemplate.class);
        Optional.ofNullable(img.getDraw()).ifPresent(v -> initDrawOptions(v3Options, v));
        Optional.ofNullable(img.getLogo()).ifPresent(v -> initLogoOptions(v3Options, v));
        Optional.ofNullable(img.getBg()).ifPresent(v -> initBgOptions(v3Options, v));
        Optional.ofNullable(img.getFt()).ifPresent(v -> initFtOptions(v3Options, v));
        Optional.ofNullable(img.getDetect()).ifPresent(v -> initDetect(v3Options, v));
    }

    private static void initDrawOptions(QrCodeV3Options options, DrawTemplate draw) {
        DrawOptions drawOptions = options.newDrawOptions();
        // 基本配置参数
        if (drawOptions.getDrawStyle() == null) drawOptions.setDrawStyle(draw.getDrawStyle());
        if (drawOptions.getPreColor() == null)
            Optional.ofNullable(ColorUtil.str2color(draw.getPreColor())).ifPresent(drawOptions::setPreColor);
        if (drawOptions.getBgColor() == null)
            Optional.ofNullable(ColorUtil.str2color(draw.getBgColor())).ifPresent(drawOptions::setBgColor);
        Optional.ofNullable(draw.getTransparencyBgFill()).ifPresent(drawOptions::setTransparencyBgFill);
        if (drawOptions.getPicStyle() == null)
            Optional.ofNullable(draw.getPicStyle()).ifPresent(drawOptions::setPicStyle);
        if (drawOptions.getCornerRadius() == null)
            Optional.ofNullable(draw.getCornerRadius()).ifPresent(drawOptions::setCornerRadius);
        if (draw.getImgs() == null || draw.getImgs().isEmpty()) {
            return;
        }

        // 资源配置
        for (DrawImgTemplate img : draw.getImgs()) {
            List<ImmutablePair<Integer, Integer>> sizeList = decodeSize(img.size);
            sizeList.forEach(k -> {
                QrResourcePool.QrResourcesDecorate decorate = drawOptions.getResourcePool().createSource(k.getLeft(), k.getRight());
                if (img.getMiss() != null) decorate.setMiss(img.getMiss());
                int count = img.count == null ? -1 : img.count;
                img.getImg().forEach(i -> decorate.addResource(new QrResource().setImg(i), count));
                decorate.build();
            });
        }
    }

    private static void initDetect(QrCodeV3Options options, DetectTemplate detect) {
        DetectOptions detectOptions = options.newDetectOptions();
        if (detectOptions.getResource() == null && detect.getImg() != null) {
            detectOptions.setResource(new QrResource().setImg(detect.img));
        }

        if (detect.ldImg != null) detectOptions.setLd(new QrResource().setImg(detect.ldImg));
        if (detect.ltImg != null) detectOptions.setLt(new QrResource().setImg(detect.ltImg));
        if (detect.rtImg != null) detectOptions.setRt(new QrResource().setImg(detect.rtImg));
    }

    private static void initLogoOptions(QrCodeV3Options options, LogoTemplate logo) {
        // 主动配置的优先级高于模板读取的配置，因此当配置已经存在时，不使用模板的配置，避免被覆盖
        LogoOptions logoOptions = options.newLogoOptions();
        if (logoOptions.getBorderColor() == null && logo.getBorderColor() != null)
            logoOptions.setBorderColor(ColorUtil.str2color(logo.getBorderColor()));
        if (logoOptions.getOuterBorderColor() == null && logo.getOuterBorderColor() != null)
            logoOptions.setOuterBorderColor(ColorUtil.str2color(logo.getOuterBorderColor()));
        if (logoOptions.getRate() == 0 && logo.rate != null) logoOptions.setRate(logo.rate);
        if (logoOptions.getOpacity() == null && logo.opacity != null) logoOptions.setOpacity(logo.opacity);
        if (logoOptions.getClearLogoArea() == null && logo.getClearLogoArea() != null)
            logoOptions.setClearLogoArea(logo.clearLogoArea);
        if (logoOptions.getLogo() == null || logoOptions.getLogo().getImg() == null) {
            logoOptions.setLogo(new QrResource().setImg(logo.img).setPicStyle(logo.picStyle));
        }
    }

    private static void initFtOptions(QrCodeV3Options options, FtTemplate ft) {
        FrontOptions ftOptions = options.newFrontOptions();
        if (ftOptions.getStartX() == 0) Optional.ofNullable(ft.startX).ifPresent(ftOptions::setStartX);
        if (ftOptions.getStartY() == 0) Optional.ofNullable(ft.startY).ifPresent(ftOptions::setStartY);
        if (ftOptions.getFtH() == 0) Optional.ofNullable(ft.ftH).ifPresent(ftOptions::setFtH);
        if (ftOptions.getFtW() == 0) Optional.ofNullable(ft.ftW).ifPresent(ftOptions::setFtW);
        if (ftOptions.getFillColor() == null)
            Optional.ofNullable(ColorUtil.str2color(ft.fillColor)).ifPresent(ftOptions::setFillColor);
        if ("gif".equalsIgnoreCase(ft.img)) {
            ftOptions.setFt(new QrResource().setGif(ft.img).setPicStyle(ft.picStyle).setCornerRadius(ft.cornerRadius == null ? 0.125f : ft.cornerRadius));
        } else {
            ftOptions.setFt(new QrResource().setImg(ft.img).setPicStyle(ft.picStyle).setCornerRadius(ft.cornerRadius == null ? 0.125f : ft.cornerRadius));
        }
    }

    private static void initBgOptions(QrCodeV3Options options, BgTemplate bg) {
        BgOptions bgOptions = options.newBgOptions();
        if (bgOptions.getBgW() == 0) Optional.ofNullable(bg.bgW).ifPresent(bgOptions::setBgW);
        if (bgOptions.getBgH() == 0) Optional.ofNullable(bg.bgH).ifPresent(bgOptions::setBgH);
        if (bgOptions.getStartX() == 0) Optional.ofNullable(bg.startX).ifPresent(bgOptions::setStartX);
        if (bgOptions.getStartY() == 0) Optional.ofNullable(bg.startY).ifPresent(bgOptions::setStartY);
        if (bgOptions.getOpacity() == 0) Optional.ofNullable(bg.opacity).ifPresent(bgOptions::setOpacity);
        if ("gif".equalsIgnoreCase(bg.imgType)) {
            bgOptions.setBg(new QrResource().setGif(bg.img).setPicStyle(bg.picStyle).setCornerRadius(bg.cornerRadius == null ? 0.125f : bg.cornerRadius));
        } else {
            bgOptions.setBg(new QrResource().setImg(bg.img).setPicStyle(bg.picStyle).setCornerRadius(bg.cornerRadius == null ? 0.125f : bg.cornerRadius));
        }
    }

    public static final class ImgTemplate {
        public DrawTemplate draw;
        public LogoTemplate logo;
        public DetectTemplate detect;
        public BgTemplate bg;
        public FtTemplate ft;

        public DrawTemplate getDraw() {
            return draw;
        }

        public ImgTemplate setDraw(DrawTemplate draw) {
            this.draw = draw;
            return this;
        }

        public DetectTemplate getDetect() {
            return detect;
        }

        public ImgTemplate setDetect(DetectTemplate detect) {
            this.detect = detect;
            return this;
        }

        public LogoTemplate getLogo() {
            return logo;
        }

        public ImgTemplate setLogo(LogoTemplate logo) {
            this.logo = logo;
            return this;
        }

        public BgTemplate getBg() {
            return bg;
        }

        public ImgTemplate setBg(BgTemplate bg) {
            this.bg = bg;
            return this;
        }

        public FtTemplate getFt() {
            return ft;
        }

        public ImgTemplate setFt(FtTemplate ft) {
            this.ft = ft;
            return this;
        }
    }

    public static final class DrawTemplate {
        private List<DrawImgTemplate> imgs;
        private String preColor;
        private String bgColor;
        private DrawStyle drawStyle;
        /**
         * 图片透明处填充，true则表示透明处用bgColor填充； false则透明处依旧透明
         */
        private Boolean transparencyBgFill = true;

        /**
         * 最终生成二维码的配置
         */
        private PicStyle picStyle;

        /**
         * 最终生成二维码，如果时圆角，则这个参数用于控制圆角的弧度，默认为 1 / 8
         */
        private Float cornerRadius;

        public List<DrawImgTemplate> getImgs() {
            return imgs;
        }

        public DrawTemplate setImgs(List<DrawImgTemplate> imgs) {
            this.imgs = imgs;
            return this;
        }

        public String getPreColor() {
            return preColor;
        }

        public DrawTemplate setPreColor(String preColor) {
            this.preColor = preColor;
            return this;
        }

        public String getBgColor() {
            return bgColor;
        }

        public DrawTemplate setBgColor(String bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public DrawStyle getDrawStyle() {
            return drawStyle;
        }

        public DrawTemplate setDrawStyle(DrawStyle drawStyle) {
            this.drawStyle = drawStyle;
            return this;
        }

        public Boolean getTransparencyBgFill() {
            return transparencyBgFill;
        }

        public DrawTemplate setTransparencyBgFill(Boolean transparencyBgFill) {
            this.transparencyBgFill = transparencyBgFill;
            return this;
        }

        public PicStyle getPicStyle() {
            return picStyle;
        }

        public DrawTemplate setPicStyle(PicStyle picStyle) {
            this.picStyle = picStyle;
            return this;
        }

        public Float getCornerRadius() {
            return cornerRadius;
        }

        public DrawTemplate setCornerRadius(Float cornerRadius) {
            this.cornerRadius = cornerRadius;
            return this;
        }
    }

    public static final class DrawImgTemplate {
        public List<String> img;
        public String size;
        public String miss;
        public Integer count;

        public List<String> getImg() {
            return img;
        }

        public DrawImgTemplate setImg(List<String> img) {
            this.img = img;
            return this;
        }

        public String getSize() {
            return size;
        }

        public DrawImgTemplate setSize(String size) {
            this.size = size;
            return this;
        }

        public String getMiss() {
            return miss;
        }

        public DrawImgTemplate setMiss(String miss) {
            this.miss = miss;
            return this;
        }

        public Integer getCount() {
            return count;
        }

        public DrawImgTemplate setCount(Integer count) {
            this.count = count;
            return this;
        }
    }

    public static final class DetectTemplate {
        private String img;
        private String ltImg;
        private String ldImg;
        private String rtImg;

        public String getImg() {
            return img;
        }

        public DetectTemplate setImg(String img) {
            this.img = img;
            return this;
        }

        public String getLtImg() {
            return ltImg;
        }

        public DetectTemplate setLtImg(String ltImg) {
            this.ltImg = ltImg;
            return this;
        }

        public String getLdImg() {
            return ldImg;
        }

        public DetectTemplate setLdImg(String ldImg) {
            this.ldImg = ldImg;
            return this;
        }

        public String getRtImg() {
            return rtImg;
        }

        public DetectTemplate setRtImg(String rtImg) {
            this.rtImg = rtImg;
            return this;
        }
    }

    public static final class LogoTemplate {
        public String img;
        public PicStyle picStyle;
        public String borderColor;
        public String outerBorderColor;
        public Integer rate;
        public Float opacity;
        public Boolean clearLogoArea;

        public String getImg() {
            return img;
        }

        public LogoTemplate setImg(String img) {
            this.img = img;
            return this;
        }

        public PicStyle getPicStyle() {
            return picStyle;
        }

        public LogoTemplate setPicStyle(PicStyle picStyle) {
            this.picStyle = picStyle;
            return this;
        }

        public String getBorderColor() {
            return borderColor;
        }

        public LogoTemplate setBorderColor(String borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public String getOuterBorderColor() {
            return outerBorderColor;
        }

        public LogoTemplate setOuterBorderColor(String outerBorderColor) {
            this.outerBorderColor = outerBorderColor;
            return this;
        }

        public Integer getRate() {
            return rate;
        }

        public LogoTemplate setRate(Integer rate) {
            this.rate = rate;
            return this;
        }

        public Float getOpacity() {
            return opacity;
        }

        public LogoTemplate setOpacity(Float opacity) {
            this.opacity = opacity;
            return this;
        }

        public Boolean getClearLogoArea() {
            return clearLogoArea;
        }

        public LogoTemplate setClearLogoArea(Boolean clearLogoArea) {
            this.clearLogoArea = clearLogoArea;
            return this;
        }
    }

    public static final class FtTemplate {
        public String img;
        // 如果是gif图片，请指定imgType = gif
        public String imgType;
        public PicStyle picStyle;
        public Float cornerRadius;
        public Integer ftW;
        public Integer ftH;
        public Integer startX;
        public Integer startY;
        public String fillColor;

        public String getImg() {
            return img;
        }

        public FtTemplate setImg(String img) {
            this.img = img;
            return this;
        }

        public String getImgType() {
            return imgType;
        }

        public FtTemplate setImgType(String imgType) {
            this.imgType = imgType;
            return this;
        }

        public PicStyle getPicStyle() {
            return picStyle;
        }

        public FtTemplate setPicStyle(PicStyle picStyle) {
            this.picStyle = picStyle;
            return this;
        }

        public Integer getFtW() {
            return ftW;
        }

        public FtTemplate setFtW(Integer ftW) {
            this.ftW = ftW;
            return this;
        }

        public Integer getFtH() {
            return ftH;
        }

        public FtTemplate setFtH(Integer ftH) {
            this.ftH = ftH;
            return this;
        }

        public Integer getStartX() {
            return startX;
        }

        public FtTemplate setStartX(Integer startX) {
            this.startX = startX;
            return this;
        }

        public Integer getStartY() {
            return startY;
        }

        public FtTemplate setStartY(Integer startY) {
            this.startY = startY;
            return this;
        }

        public String getFillColor() {
            return fillColor;
        }

        public FtTemplate setFillColor(String fillColor) {
            this.fillColor = fillColor;
            return this;
        }

        public Float getCornerRadius() {
            return cornerRadius;
        }

        public FtTemplate setCornerRadius(Float cornerRadius) {
            this.cornerRadius = cornerRadius;
            return this;
        }
    }

    public static final class BgTemplate {
        public String img;
        // 如果是gif图片，请指定imgType = gif
        public String imgType;
        public PicStyle picStyle;
        public Float cornerRadius;
        public Integer bgW;
        public Integer bgH;
        public BgStyle bgStyle;
        public Float opacity;
        public Integer startX;
        public Integer startY;

        public String getImg() {
            return img;
        }

        public BgTemplate setImg(String img) {
            this.img = img;
            return this;
        }


        public Float getCornerRadius() {
            return cornerRadius;
        }

        public BgTemplate setCornerRadius(Float cornerRadius) {
            this.cornerRadius = cornerRadius;
            return this;
        }

        public String getImgType() {
            return imgType;
        }

        public BgTemplate setImgType(String imgType) {
            this.imgType = imgType;
            return this;
        }

        public PicStyle getPicStyle() {
            return picStyle;
        }

        public BgTemplate setPicStyle(PicStyle picStyle) {
            this.picStyle = picStyle;
            return this;
        }

        public Integer getBgW() {
            return bgW;
        }

        public BgTemplate setBgW(Integer bgW) {
            this.bgW = bgW;
            return this;
        }

        public Integer getBgH() {
            return bgH;
        }

        public BgTemplate setBgH(Integer bgH) {
            this.bgH = bgH;
            return this;
        }

        public BgStyle getBgStyle() {
            return bgStyle;
        }

        public BgTemplate setBgStyle(BgStyle bgStyle) {
            this.bgStyle = bgStyle;
            return this;
        }

        public Float getOpacity() {
            return opacity;
        }

        public BgTemplate setOpacity(Float opacity) {
            this.opacity = opacity;
            return this;
        }

        public Integer getStartX() {
            return startX;
        }

        public BgTemplate setStartX(Integer startX) {
            this.startX = startX;
            return this;
        }

        public Integer getStartY() {
            return startY;
        }

        public BgTemplate setStartY(Integer startY) {
            this.startY = startY;
            return this;
        }
    }

    public static void main(String[] args) {
        DrawTemplate drawTemplate = new DrawTemplate();
        drawTemplate.setDrawStyle(DrawStyle.IMAGE);
        List<DrawImgTemplate> imgTemplates = new ArrayList<>();
        imgTemplates.add(new DrawImgTemplate().setSize("1x1").setImg(Arrays.asList("flower/11.png",
                "flower/12.png", "flower/13.png"
        )).setCount(-1));
        imgTemplates.add(new DrawImgTemplate().setSize("2x1").setImg(Arrays.asList("flower/21.png",
                "flower/22.png")).setCount(-1));
        imgTemplates.add(new DrawImgTemplate().setSize("1x2").setImg(Arrays.asList("flower/31.png",
                "flower/32.png", "flower/33.png")));
        imgTemplates.add(new DrawImgTemplate().setSize("2x2").setImg(Arrays.asList("flower/41.png",
                "flower/42.png", "flower/43.png")).setMiss("0-1"));
        drawTemplate.setImgs(imgTemplates);
        drawTemplate.setPicStyle(PicStyle.NORMAL);

        LogoTemplate logoTemplate = new LogoTemplate();
        logoTemplate.setRate(12).setBorderColor("0xFFF7EED6").setPicStyle(PicStyle.ROUND).setImg("logo.png");

        BgTemplate bgTemplate = new BgTemplate();
        bgTemplate.setStartX(140).setStartY(140).setBgW(1080).setBgH(1850).setBgStyle(BgStyle.OVERRIDE);

        FtTemplate ftTemplate = new FtTemplate();
        ftTemplate.setStartX(100).setStartY(130).setFtW(1340).setFtW(1340).setImg("ft/ft_1.png");

        ImgTemplate imgTemplate = new ImgTemplate().setDraw(drawTemplate).setLogo(logoTemplate).setFt(ftTemplate).setBg(bgTemplate);
        System.out.println(JsonUtil.toStr(imgTemplate));
    }
}
