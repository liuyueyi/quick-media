package com.github.hui.media.console.action.qrcode;

import com.github.hui.media.console.action.BaseRequest;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yihui on 2017/10/15.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class QrCodeEncRequest extends BaseRequest {

    private static final long serialVersionUID = -5451353741058229910L;

    private String content;

    private int size;

    private int errorLevel;

    private int padding;

    private String preColor;

    private String bgColor;

    private String detectColor;

    private String style;

    private Boolean scale;

    @Override
    public boolean validate() {
        return super.validate() && StringUtils.isNotBlank(content);
    }
}
