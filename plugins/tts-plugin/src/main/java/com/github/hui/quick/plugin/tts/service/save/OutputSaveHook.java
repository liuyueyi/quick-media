package com.github.hui.quick.plugin.tts.service.save;

import okio.ByteString;

/**
 * 输出的语音保存方式
 *
 * @author YiHui
 * @date 2024/4/30
 */
@FunctionalInterface
public interface OutputSaveHook<T> {

    /**
     * 输出文件类型
     *
     * @param data     数据
     * @param saveName 文件名
     * @param suffix   后缀
     * @return T 返回保存后的信息
     */
    T save(ByteString data, String saveName, String suffix) throws Exception;
}
