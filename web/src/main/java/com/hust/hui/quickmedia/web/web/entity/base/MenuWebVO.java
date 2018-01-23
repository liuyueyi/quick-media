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
public class MenuWebVO implements IWebVO {
    private static final long serialVersionUID = 6658162905279251868L;

    /**
     * 链接地址
     */
    private String url;


    /**
     * 菜单文字
     */
    private String menu;
}
