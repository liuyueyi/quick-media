package com.hust.hui.quickmedia.web.web.entity;

import com.hust.hui.quickmedia.web.web.entity.base.IWebVO;
import lombok.Data;

import java.util.List;

/**
 * Created by yihui on 2017/12/2.
 */
@Data
public class HeadBannerVO {

    private String title;

    private String subTitle;

    private List<IWebVO> menuList;
}
