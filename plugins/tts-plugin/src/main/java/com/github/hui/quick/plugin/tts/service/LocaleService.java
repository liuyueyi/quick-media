package com.github.hui.quick.plugin.tts.service;

import com.github.hui.quick.plugin.tts.constant.LocaleEnum;
import com.github.hui.quick.plugin.tts.constant.VoiceEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 语音相关服务
 *
 * @author YiHui
 * @date 2024/7/26
 */
public class LocaleService {

    /**
     * 根据大语言类型，获取对应的语言枚举
     *
     * @return
     */
    public Map<String, List<LocaleEnum>> getLocals() {
        return Arrays.stream(LocaleEnum.values()).collect(Collectors.groupingBy(LocaleEnum::getLocale));
    }


    /**
     * 获取某个语言对应的语音列表
     *
     * @param local
     * @return
     */
    public List<VoiceEnum> getVoiceByLocale(LocaleEnum local) {
        return Arrays.stream(VoiceEnum.values()).filter(s -> s.getLocale().equals(local.getLocale())).collect(Collectors.toList());
    }
}
