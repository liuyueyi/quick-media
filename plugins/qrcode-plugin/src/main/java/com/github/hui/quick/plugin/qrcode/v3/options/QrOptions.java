package com.github.hui.quick.plugin.qrcode.v3.options;

import com.github.hui.quick.plugin.qrcode.v3.canvas.GraphicQrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.canvas.QrCanvas;
import com.github.hui.quick.plugin.qrcode.v3.options.bg.BgOptions;
import com.github.hui.quick.plugin.qrcode.v3.options.front.FrontOptions;
import com.github.hui.quick.plugin.qrcode.v3.options.logo.LogoOptions;
import com.github.hui.quick.plugin.qrcode.v3.options.qr.QrDetectOptions;
import com.github.hui.quick.plugin.qrcode.v3.options.qr.QrInfoOptions;
import com.github.hui.quick.plugin.qrcode.v3.resources.ResourceContainer;
import com.google.zxing.EncodeHintType;

import java.util.Map;

/**
 * qrcode 相关配置
 *
 * @author yihui
 * @date 2022/6/10
 */
public class QrOptions {

    private String msg;


    private Map<EncodeHintType, Object> hints;

    private int w;

    private int h;

    /**
     * 生成二维码图片的格式 png, jpg
     */
    private String outType;

    /**
     * 画板
     */
    private QrCanvas qrCanvas;

    private QrInfoOptions qrInfo;

    private QrDetectOptions qrDetect;

    private LogoOptions logo;

    private FrontOptions front;

    private BgOptions bg;

    /**
     * 存储所有的资源
     */
    private ResourceContainer sources;

    public static QrOptions of(String content) {
        QrOptions qrOptions = new QrOptions();
        qrOptions.setMsg(content);
        return qrOptions;
    }

    public String getMsg() {
        return msg;
    }

    public QrOptions setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Map<EncodeHintType, Object> getHints() {
        return hints;
    }

    public QrOptions setHints(Map<EncodeHintType, Object> hints) {
        this.hints = hints;
        return this;
    }

    public String getOutType() {
        return outType;
    }

    public QrOptions setOutType(String outType) {
        this.outType = outType;
        return this;
    }

    public QrInfoOptions getQrInfo() {
        return qrInfo;
    }

    public QrOptions setQrInfo(QrInfoOptions qrInfo) {
        this.qrInfo = qrInfo;
        return this;
    }

    public QrDetectOptions getQrDetect() {
        return qrDetect;
    }

    public QrOptions setQrDetect(QrDetectOptions qrDetect) {
        this.qrDetect = qrDetect;
        return this;
    }

    public LogoOptions getLogo() {
        return logo;
    }

    public QrOptions setLogo(LogoOptions logo) {
        this.logo = logo;
        return this;
    }

    public FrontOptions getFront() {
        return front;
    }

    public QrOptions setFront(FrontOptions front) {
        this.front = front;
        return this;
    }

    public BgOptions getBg() {
        return bg;
    }

    public QrOptions setBg(BgOptions bg) {
        this.bg = bg;
        return this;
    }


    public int getW() {
        if (w == 0 && h == 0) {
            w = h = 200;
        } else if (w == 0) {
            w = h;
        }
        return w;
    }

    public QrOptions setW(int w) {
        this.w = w;
        return this;
    }

    public int getH() {
        if (w == 0 && h == 0) {
            w = h = 200;
        } else if (h == 0) {
            h = w;
        }
        return h;
    }

    public QrOptions setH(int h) {
        this.h = h;
        return this;
    }


    public QrCanvas getQrCanvas() {
        if (qrCanvas == null) {
            qrCanvas = new GraphicQrCanvas(getW(), getH());
        }
        return qrCanvas;
    }

    public ResourceContainer getSources() {
        if (sources == null) {
            sources = new ResourceContainer();
        }
        return sources;
    }

    public QrOptions setSources(ResourceContainer sources) {
        this.sources = sources;
        return this;
    }

    public QrOptions setQrCanvas(QrCanvas qrCanvas) {
        this.qrCanvas = qrCanvas;
        return this;
    }
}
