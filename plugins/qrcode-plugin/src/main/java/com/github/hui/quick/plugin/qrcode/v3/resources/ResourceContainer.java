package com.github.hui.quick.plugin.qrcode.v3.resources;

import java.util.List;

/**
 * @author
 * @date 2022/6/10
 */
public class ResourceContainer {
    private List<RenderSource> sources;

    public RenderSource getSource() {
        return sources.get(0);
    }

}
