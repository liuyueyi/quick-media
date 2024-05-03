package com.github.hui.quick.plugin.tts.constant;

/**
 * https://learn.microsoft.com/zh-cn/azure/cognitive-services/speech-service/speech-synthesis-markup-voice#speaking-styles-and-roles
 *
 * @author zh-hq
 * @date 2023/3/29
 */
public enum TtsStyleEnum {
    /**
     * 用兴奋和精力充沛的语气推广产品或服务
     */
    advertisement_upbeat("advertisement_upbeat"),
    /**
     * 以较高的音调和音量表达温暖而亲切的语气。 说话者处于吸引听众注意力的状态。 说话者的个性往往是讨喜的
     */
    affectionate("affectionate"),
    /**
     * 表达生气和厌恶的语气
     */
    angry("angry"),
    /**
     * 数字助理用的是热情而轻松的语气
     */
    assistant("assistant"),
    /**
     * 以沉着冷静的态度说话。 语气、音调和韵律与其他语音类型相比要统一得多
     */
    calm("calm"),
    /**
     * 表达轻松随意的语气
     */
    chat("chat"),
    /**
     * 表达积极愉快的语气
     */
    cheerful("cheerful"),
    /**
     * 以友好热情的语气为客户提供支持
     */
    customerservice("customerservice"),
    /**
     * 调低音调和音量来表达忧郁、沮丧的语气
     */
    depressed("depressed"),
    /**
     * 表达轻蔑和抱怨的语气。 这种情绪的语音表现出不悦和蔑视
     */
    disgruntled("disgruntled"),
    /**
     * 用一种轻松、感兴趣和信息丰富的风格讲述纪录片，适合配音纪录片、专家评论和类似内容
     */
    documentary_narration("documentary-narration"),
    /**
     * 在说话者感到不舒适时表达不确定、犹豫的语气
     */
    embarrassed("embarrassed"),
    /**
     * 表达关心和理解
     */
    empathetic("empathetic"),
    /**
     * 当你渴望别人拥有的东西时，表达一种钦佩的语气
     */
    envious("envious"),
    /**
     * 表达乐观和充满希望的语气。 似乎发生了一些美好的事情，说话人对此非常满意
     */
    excited("excited"),
    /**
     * 以较高的音调、较高的音量和较快的语速来表达恐惧、紧张的语气。 说话人处于紧张和不安的状态
     */
    fearful("fearful"),
    /**
     * 表达一种愉快、怡人且温暖的语气。 听起来很真诚且满怀关切
     */
    friendly("friendly"),
    /**
     * 以较低的音调和音量表达温和、礼貌和愉快的语气
     */
    gentle("gentle"),
    /**
     * 表达一种温暖且渴望的语气。 听起来像是会有好事发生在说话人身上
     */
    hopeful("hopeful"),
    /**
     * 以优美又带感伤的方式表达情感
     */
    lyrical("lyrical"),
    /**
     * 以专业、客观的语气朗读内容
     */
    narration_professional("narration-professional"),
    /**
     * 为内容阅读表达一种舒缓而悦耳的语气
     */
    narration_relaxed("narration-relaxed"),
    /**
     * 以正式专业的语气叙述新闻
     */
    newscast("newscast"),
    /**
     * 以通用、随意的语气发布一般新闻
     */
    newscast_casual("newscast-casual"),
    /**
     * 以正式、自信和权威的语气发布新闻
     */
    newscast_formal("newscast-formal"),
    /**
     * 在读诗时表达出带情感和节奏的语气
     */
    poetry_reading("poetry-reading"),
    /**
     * 表达悲伤语气
     */
    sad("sad"),
    /**
     * 表达严肃和命令的语气。 说话者的声音通常比较僵硬，节奏也不那么轻松
     */
    serious("serious"),
    /**
     * 表达严肃和命令的语气。 说话者的声音通常比较僵硬，节奏也不那么轻松
     */
    shouting("shouting"),
    /**
     * 用轻松有趣的语气播报体育赛事
     */
    sports_commentary("sports_commentary"),
    /**
     * 用快速且充满活力的语气播报体育赛事精彩瞬间
     */
    sports_commentary_excited("sports_commentary_excited"),
    /**
     * 说话非常柔和，发出的声音小且温柔
     */
    whispering("whispering"),
    /**
     * 表达一种非常害怕的语气，语速快且声音颤抖。 听起来说话人处于不稳定的疯狂状态
     */
    terrified("terrified"),
    /**
     * 表达一种冷淡无情的语气
     */
    unfriendly("unfriendly"),
    ;

    private final String value;

    TtsStyleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
