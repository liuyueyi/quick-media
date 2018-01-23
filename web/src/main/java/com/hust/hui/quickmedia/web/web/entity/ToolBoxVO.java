package com.hust.hui.quickmedia.web.web.entity;

import com.hust.hui.quickmedia.web.web.entity.base.BaseToolWebVO;
import lombok.Data;

import java.util.List;

/**
 * Created by yihui on 2017/12/2.
 */
@Data
public class ToolBoxVO {

    /**
     * 工具向name
     */
    private String toolName;

    private List<BaseToolWebVO> tools;

}
