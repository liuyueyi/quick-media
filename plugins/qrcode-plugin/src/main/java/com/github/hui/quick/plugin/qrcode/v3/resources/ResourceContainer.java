package com.github.hui.quick.plugin.qrcode.v3.resources;

import com.github.hui.quick.plugin.base.ColorUtil;
import com.github.hui.quick.plugin.qrcode.util.QrUtil;
import com.github.hui.quick.plugin.qrcode.v3.options.qr.QrDetectPosition;
import com.github.hui.quick.plugin.qrcode.v3.options.source.DetectSourceOptions;
import com.github.hui.quick.plugin.qrcode.v3.options.source.SourceOptions;
import com.github.hui.quick.plugin.qrcode.v3.resources.impl.BasicRenderSource;
import com.github.hui.quick.plugin.qrcode.v3.resources.impl.detect.DetectRenderSource;
import com.google.zxing.qrcode.encoder.ByteMatrix;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @date 2022/6/10
 */
public class ResourceContainer {

    /**
     * 最后一个资源，必须是 1x1 的背景渲染资源
     */
    private List<RenderSourceDecorate<?>> bgSources;

    /**
     * 最后一个资源，必须是 1x1 的二维码信息渲染资源
     */
    private List<RenderSourceDecorate<?>> preSources;

    /**
     * 码眼对应的资源
     */
    private List<RenderSourceDecorate<?>> detectSources;

    private void checkAndInitDefaultRenderSources() {
        if (bgSources == null) {
            bgSources = new ArrayList<>();
            bgSources.add(RenderSourceDecorate.create(new BasicRenderSource<>(new SourceOptions().setColor(ColorUtil.OPACITY))));
        }

        if (preSources == null) {
            preSources = new ArrayList<>();
            preSources.add(RenderSourceDecorate.create(new BasicRenderSource<>(new SourceOptions().setColor(Color.BLACK))));
        }

        if (detectSources == null) {
            detectSources = new ArrayList<>();
            detectSources.add(RenderSourceDecorate.create(new DetectRenderSource<>(new DetectSourceOptions().addColor(DetectSourceOptions.DetectSourceArea.BG, Color.ORANGE).addColor(DetectSourceOptions.DetectSourceArea.OUT, Color.BLACK).addColor(DetectSourceOptions.DetectSourceArea.IN, Color.BLACK))));
        }
    }

    public <T> RenderSourceDecorate<T> getSource(ByteMatrix byteMatrix, int x, int y) {
        QrDetectPosition position = QrUtil.judgeDetectArea(byteMatrix, x, y);
        if (position != QrDetectPosition.NONE) {
            return getDetectSource(position);
        } else {
            return getQrInfoSource(byteMatrix, x, y);
        }
    }

    /**
     * @param position 0 left, 1 right 2 bottom
     * @return
     */
    @SuppressWarnings("unchecked")

    public <T> RenderSourceDecorate<T> getDetectSource(QrDetectPosition position) {
        checkAndInitDefaultRenderSources();
        if (detectSources.size() == 1) {
            return (RenderSourceDecorate<T>) detectSources.get(0);
        }
        return (RenderSourceDecorate<T>) detectSources.get(position.getPosition());
    }

    @SuppressWarnings("unchecked")
    public <T> RenderSourceDecorate<T> getQrInfoSource(ByteMatrix byteMatrix, int x, int y) {
        checkAndInitDefaultRenderSources();
        if (byteMatrix.get(x, y) == 0) {
            for (RenderSourceDecorate d : bgSources) {
                if (d.match(byteMatrix, x, y)) {
                    return d;
                }
            }
            return (RenderSourceDecorate<T>) bgSources.get(bgSources.size() - 1);
        } else {
            for (RenderSourceDecorate d : preSources) {
                if (d.match(byteMatrix, x, y)) {
                    return d;
                }
            }

            return (RenderSourceDecorate<T>) preSources.get(preSources.size() - 1);
        }
    }
}
