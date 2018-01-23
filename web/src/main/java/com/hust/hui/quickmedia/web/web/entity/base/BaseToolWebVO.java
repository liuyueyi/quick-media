package com.hust.hui.quickmedia.web.web.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yihui on 2017/12/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseToolWebVO implements IWebVO {
    private static final long serialVersionUID = -5702593551351819336L;

    private String title;

    private String desc;

    private String url;

    private String logo;
}
