package com.github.hui.quick.plugin.tts.constant;

/**
 * @author zh-hq
 * @date 2023/3/30
 */
public enum VoiceEnum {


    /*
     edge voice list
      https://speech.platform.bing.com/consumer/speech/synthesize/readaloud/voices/list?trustedclienttoken=6A5AA1D4EAFF4E9FB37E23D68491D6F4
     */
    /**
     * "曉佳(HiuGaai),粤语女声
     */
    zh_HK_HiuGaaiNeural("zh-HK-HiuGaaiNeural", "女", "zh-HK"),
    /**
     * 曉曼(HiuMaan),粤语女声
     */
    zh_HK_HiuMaanNeural("zh-HK-HiuMaanNeural", "女", "zh-HK"),
    /**
     * 雲龍(WanLung),粤语男声
     */
    zh_HK_WanLungNeural("zh-HK-WanLungNeural", "男", "zh-HK"),
    /**
     * 晓晓 活泼、温暖的声音，具有多种场景风格和情感
     */
    zh_CN_XiaoxiaoNeural("zh-CN-XiaoxiaoNeural", "女", "zh-CN"),
    /**
     * 晓伊
     */
    zh_CN_XiaoyiNeural("zh-CN-XiaoyiNeural", "女", "zh-CN"),
    /**
     * 云健 适合影视和体育解说
     */
    zh_CN_YunjianNeural("zh-CN-YunjianNeural", "男", "zh-CN"),
    /**
     * 云希 活泼、阳光的声音，具有丰富的情感，可用于许多对话场景
     */
    zh_CN_YunxiNeural("zh-CN-YunxiNeural", "男", "zh-CN"),
    /**
     * 云夏 少年年男声
     */
    zh_CN_YunxiaNeural("zh-CN-YunxiaNeural", "男", "zh-CN"),
    /**
     * 云扬 专业、流利的声音，具有多种场景风格
     */
    zh_CN_YunyangNeural("zh-CN-YunyangNeural", "男", "zh-CN"),
    /**
     * 晓北 辽宁 东北大妹子
     */
    zh_CN_liaoning_XiaobeiNeural("zh-CN-liaoning-XiaobeiNeural", "女", "zh-CN-liaoning"),
    /**
     * 曉臻(HsiaoChen),湾湾女声
     */
    zh_TW_HsiaoChenNeural("zh-TW-HsiaoChenNeural", "女", "zh-TW"),
    /**
     * 雲哲(YunJhe),湾湾男声
     */
    zh_TW_YunJheNeural("zh-TW-YunJheNeural", "男", "zh-TW"),
    /**
     * 曉雨(HsiaoYu),湾湾女声
     */
    zh_TW_HsiaoYuNeural("zh-TW-HsiaoYuNeural", "女", "zh-TW"),
    /**
     *
     */
    zh_CN_shaanxi_XiaoniNeural("zh-CN-shaanxi-XiaoniNeural", "女", "zh-CN-shaanxi"),

    /**
     * 英语
     */
    en_US_AriaNeural("en-US-AriaNeural", "女", "en-US"),
    en_US_AnaNeural("en-US-AnaNeural", "女", "en-US"),
    en_US_ChristopherNeural("en-US-ChristopherNeural", "男", "en-US"),
    en_US_EricNeural("en-US-EricNeural", "男", "en-US"),
    en_US_GuyNeural("en-US-GuyNeural", "男", "en-US"),
    en_US_JennyNeural("en-US-JennyNeural", "女", "en-US"),
    en_US_MichelleNeural("en-US-MichelleNeural", "女", "en-US"),
    en_US_RogerNeural("en-US-RogerNeural", "男", "en-US"),
    en_US_SteffanNeural("en-US-SteffanNeural", "男", "en-US"),

    /*
    AZURE 语音库补充
    https://learn.microsoft.com/zh-cn/azure/cognitive-services/speech-service/language-support?tabs=tts
    中文（普通话，简体）
    */
    /**
     * 晓辰 休闲、放松的语音，用于自发性对话和会议听录
     */
    zh_CN_XiaochenNeural("zh-CN-XiaochenNeural", "女", "zh-CN"),
    /**
     * 晓涵 温暖、甜美、富有感情的声音，可用于许多对话场景
     */
    zh_CN_XiaohanNeural("zh-CN-XiaohanNeural", "女", "zh-CN"),
    /**
     * 晓梦
     */
    zh_CN_XiaomengNeural("zh-CN-XiaomengNeural", "女", "zh-CN"),
    /**
     * 晓墨 清晰、放松的声音，具有丰富的角色扮演和情感，适合音频书籍
     */
    zh_CN_XiaomoNeural("zh-CN-XiaomoNeural", "女", "zh-CN"),
    /**
     * 晓秋 智能、舒适的语音，适合阅读长内容
     */
    zh_CN_XiaoqiuNeural("zh-CN-XiaoqiuNeural", "女", "zh-CN"),
    /**
     * 晓睿 成熟、睿智的声音，具有丰富的情感，适合音频书籍
     */
    zh_CN_XiaoruiNeural("zh-CN-XiaoruiNeural", "女", "zh-CN"),
    /**
     * 晓双 可爱、愉悦的语音，可应用于许多儿童相关场景
     */
    zh_CN_XiaoshuangNeural("zh-CN-XiaoshuangNeural", "女", "zh-CN"),
    /**
     * 晓萱 自信、有能力的声音，具有丰富的角色扮演和情感，适合音频书籍
     */
    zh_CN_XiaoxuanNeural("zh-CN-XiaoxuanNeural", "女", "zh-CN"),
    /**
     * 晓颜 训练有素、舒适的语音，用于客户服务和对话场景
     */
    zh_CN_XiaoyanNeural("zh-CN-XiaoyanNeural", "女", "zh-CN"),
    /**
     * 晓悠 天使般的清晰声音，可以应用于许多儿童相关场景
     */
    zh_CN_XiaoyouNeural("zh-CN-XiaoyouNeural", "女", "zh-CN"),
    /**
     * 晓甄
     */
    zh_CN_XiaozhenNeural("zh-CN-XiaozhenNeural", "女", "zh-CN"),
    /**
     * 云枫
     */
    zh_CN_YunfengNeural("zh-CN-YunfengNeural", "男", "zh-CN"),
    /**
     * 云皓
     */
    zh_CN_YunhaoNeural("zh-CN-YunhaoNeural", "男", "zh-CN"),
    /**
     * 云野 成熟、放松的声音，具有多种情感，适合音频书籍
     */
    zh_CN_YunyeNeural("zh-CN-YunyeNeural", "男", "zh-CN"),
    /**
     * 云泽 老年男声
     */
    zh_CN_YunzeNeural("zh-CN-YunzeNeural", "男", "zh-CN"),
    ;


    private final String shortName;
    private final String gender;
    private final String locale;

    VoiceEnum(String shortName, String gender, String locale) {
        this.shortName = shortName;
        this.gender = gender;
        this.locale = locale;
    }

    public String getShortName() {
        return shortName;
    }

    public String getGender() {
        return gender;
    }

    public String getLocale() {
        return locale;
    }
}
