package com.hust.hui.quickmedia.web.api.audio;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by yihui on 2017/7/13.
 */
@Getter
@Setter
@ToString
public class AudioRequest {

    private String audioPath;

    private String outType;

    private String sourceType;

}
