package com.github.hui.quick.plugin.tts.constant;

import java.util.Optional;

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

    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    af_ZA_AdriNeural("af-ZA-AdriNeural", "女", "af-ZA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    af_ZA_WillemNeural("af-ZA-WillemNeural", "男", "af-ZA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sq_AL_AnilaNeural("sq-AL-AnilaNeural", "女", "sq-AL"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sq_AL_IlirNeural("sq-AL-IlirNeural", "男", "sq-AL"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    am_ET_AmehaNeural("am-ET-AmehaNeural", "男", "am-ET"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    am_ET_MekdesNeural("am-ET-MekdesNeural", "女", "am-ET"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_DZ_AminaNeural("ar-DZ-AminaNeural", "女", "ar-DZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_DZ_IsmaelNeural("ar-DZ-IsmaelNeural", "男", "ar-DZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_BH_AliNeural("ar-BH-AliNeural", "男", "ar-BH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_BH_LailaNeural("ar-BH-LailaNeural", "女", "ar-BH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_EG_SalmaNeural("ar-EG-SalmaNeural", "女", "ar-EG"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_EG_ShakirNeural("ar-EG-ShakirNeural", "男", "ar-EG"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_IQ_BasselNeural("ar-IQ-BasselNeural", "男", "ar-IQ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_IQ_RanaNeural("ar-IQ-RanaNeural", "女", "ar-IQ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_JO_SanaNeural("ar-JO-SanaNeural", "女", "ar-JO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_JO_TaimNeural("ar-JO-TaimNeural", "男", "ar-JO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_KW_FahedNeural("ar-KW-FahedNeural", "男", "ar-KW"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_KW_NouraNeural("ar-KW-NouraNeural", "女", "ar-KW"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_LB_LaylaNeural("ar-LB-LaylaNeural", "女", "ar-LB"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_LB_RamiNeural("ar-LB-RamiNeural", "男", "ar-LB"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_LY_ImanNeural("ar-LY-ImanNeural", "女", "ar-LY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_LY_OmarNeural("ar-LY-OmarNeural", "男", "ar-LY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_MA_JamalNeural("ar-MA-JamalNeural", "男", "ar-MA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_MA_MounaNeural("ar-MA-MounaNeural", "女", "ar-MA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_OM_AbdullahNeural("ar-OM-AbdullahNeural", "男", "ar-OM"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_OM_AyshaNeural("ar-OM-AyshaNeural", "女", "ar-OM"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_QA_AmalNeural("ar-QA-AmalNeural", "女", "ar-QA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_QA_MoazNeural("ar-QA-MoazNeural", "男", "ar-QA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_SA_HamedNeural("ar-SA-HamedNeural", "男", "ar-SA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_SA_ZariyahNeural("ar-SA-ZariyahNeural", "女", "ar-SA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_SY_AmanyNeural("ar-SY-AmanyNeural", "女", "ar-SY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_SY_LaithNeural("ar-SY-LaithNeural", "男", "ar-SY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_TN_HediNeural("ar-TN-HediNeural", "男", "ar-TN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_TN_ReemNeural("ar-TN-ReemNeural", "女", "ar-TN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_AE_FatimaNeural("ar-AE-FatimaNeural", "女", "ar-AE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_AE_HamdanNeural("ar-AE-HamdanNeural", "男", "ar-AE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_YE_MaryamNeural("ar-YE-MaryamNeural", "女", "ar-YE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ar_YE_SalehNeural("ar-YE-SalehNeural", "男", "ar-YE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    az_AZ_BabekNeural("az-AZ-BabekNeural", "男", "az-AZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    az_AZ_BanuNeural("az-AZ-BanuNeural", "女", "az-AZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    bn_BD_NabanitaNeural("bn-BD-NabanitaNeural", "女", "bn-BD"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    bn_BD_PradeepNeural("bn-BD-PradeepNeural", "男", "bn-BD"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    bn_IN_BashkarNeural("bn-IN-BashkarNeural", "男", "bn-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    bn_IN_TanishaaNeural("bn-IN-TanishaaNeural", "女", "bn-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    bs_BA_GoranNeural("bs-BA-GoranNeural", "男", "bs-BA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    bs_BA_VesnaNeural("bs-BA-VesnaNeural", "女", "bs-BA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    bg_BG_BorislavNeural("bg-BG-BorislavNeural", "男", "bg-BG"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    bg_BG_KalinaNeural("bg-BG-KalinaNeural", "女", "bg-BG"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    my_MM_NilarNeural("my-MM-NilarNeural", "女", "my-MM"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    my_MM_ThihaNeural("my-MM-ThihaNeural", "男", "my-MM"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ca_ES_EnricNeural("ca-ES-EnricNeural", "男", "ca-ES"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ca_ES_JoanaNeural("ca-ES-JoanaNeural", "女", "ca-ES"),

    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    hr_HR_GabrijelaNeural("hr-HR-GabrijelaNeural", "女", "hr-HR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    hr_HR_SreckoNeural("hr-HR-SreckoNeural", "男", "hr-HR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    cs_CZ_AntoninNeural("cs-CZ-AntoninNeural", "男", "cs-CZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    cs_CZ_VlastaNeural("cs-CZ-VlastaNeural", "女", "cs-CZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    da_DK_ChristelNeural("da-DK-ChristelNeural", "女", "da-DK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    da_DK_JeppeNeural("da-DK-JeppeNeural", "男", "da-DK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    nl_BE_ArnaudNeural("nl-BE-ArnaudNeural", "男", "nl-BE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    nl_BE_DenaNeural("nl-BE-DenaNeural", "女", "nl-BE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    nl_NL_ColetteNeural("nl-NL-ColetteNeural", "女", "nl-NL"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    nl_NL_FennaNeural("nl-NL-FennaNeural", "女", "nl-NL"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    nl_NL_MaartenNeural("nl-NL-MaartenNeural", "男", "nl-NL"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_AU_NatashaNeural("en-AU-NatashaNeural", "女", "en-AU"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_AU_WilliamNeural("en-AU-WilliamNeural", "男", "en-AU"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_CA_ClaraNeural("en-CA-ClaraNeural", "女", "en-CA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_CA_LiamNeural("en-CA-LiamNeural", "男", "en-CA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_HK_SamNeural("en-HK-SamNeural", "男", "en-HK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_HK_YanNeural("en-HK-YanNeural", "女", "en-HK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_IN_NeerjaExpressiveNeural("en-IN-NeerjaExpressiveNeural", "女", "en-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_IN_NeerjaNeural("en-IN-NeerjaNeural", "女", "en-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_IN_PrabhatNeural("en-IN-PrabhatNeural", "男", "en-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_IE_ConnorNeural("en-IE-ConnorNeural", "男", "en-IE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_IE_EmilyNeural("en-IE-EmilyNeural", "女", "en-IE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_KE_AsiliaNeural("en-KE-AsiliaNeural", "女", "en-KE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_KE_ChilembaNeural("en-KE-ChilembaNeural", "男", "en-KE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_NZ_MitchellNeural("en-NZ-MitchellNeural", "男", "en-NZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_NZ_MollyNeural("en-NZ-MollyNeural", "女", "en-NZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_NG_AbeoNeural("en-NG-AbeoNeural", "男", "en-NG"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_NG_EzinneNeural("en-NG-EzinneNeural", "女", "en-NG"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_PH_JamesNeural("en-PH-JamesNeural", "男", "en-PH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_PH_RosaNeural("en-PH-RosaNeural", "女", "en-PH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_SG_LunaNeural("en-SG-LunaNeural", "女", "en-SG"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_SG_WayneNeural("en-SG-WayneNeural", "男", "en-SG"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_ZA_LeahNeural("en-ZA-LeahNeural", "女", "en-ZA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_ZA_LukeNeural("en-ZA-LukeNeural", "男", "en-ZA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_TZ_ElimuNeural("en-TZ-ElimuNeural", "男", "en-TZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_TZ_ImaniNeural("en-TZ-ImaniNeural", "女", "en-TZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_GB_LibbyNeural("en-GB-LibbyNeural", "女", "en-GB"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_GB_MaisieNeural("en-GB-MaisieNeural", "女", "en-GB"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_GB_RyanNeural("en-GB-RyanNeural", "男", "en-GB"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_GB_SoniaNeural("en-GB-SoniaNeural", "女", "en-GB"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    en_GB_ThomasNeural("en-GB-ThomasNeural", "男", "en-GB"),
    // {"VoicePersonalities":["Expressive","Caring","Pleasant","Friendly"],"ContentCategories":["Conversation","Copilot"]}
    en_US_AvaMultilingualNeural("en-US-AvaMultilingualNeural", "女", "en-US"),
    // {"VoicePersonalities":["Warm","Confident","Authentic","Honest"],"ContentCategories":["Conversation","Copilot"]}
    en_US_AndrewMultilingualNeural("en-US-AndrewMultilingualNeural", "男", "en-US"),
    // {"VoicePersonalities":["Cheerful","Clear","Conversational"],"ContentCategories":["Conversation","Copilot"]}
    en_US_EmmaMultilingualNeural("en-US-EmmaMultilingualNeural", "女", "en-US"),
    // {"VoicePersonalities":["Approachable","Casual","Sincere"],"ContentCategories":["Conversation","Copilot"]}
    en_US_BrianMultilingualNeural("en-US-BrianMultilingualNeural", "男", "en-US"),
    // {"VoicePersonalities":["Expressive","Caring","Pleasant","Friendly"],"ContentCategories":["Conversation","Copilot"]}
    en_US_AvaNeural("en-US-AvaNeural", "女", "en-US"),
    // {"VoicePersonalities":["Warm","Confident","Authentic","Honest"],"ContentCategories":["Conversation","Copilot"]}
    en_US_AndrewNeural("en-US-AndrewNeural", "男", "en-US"),
    // {"VoicePersonalities":["Cheerful","Clear","Conversational"],"ContentCategories":["Conversation","Copilot"]}
    en_US_EmmaNeural("en-US-EmmaNeural", "女", "en-US"),
    // {"VoicePersonalities":["Approachable","Casual","Sincere"],"ContentCategories":["Conversation","Copilot"]}
    en_US_BrianNeural("en-US-BrianNeural", "男", "en-US"),



    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    et_EE_AnuNeural("et-EE-AnuNeural", "女", "et-EE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    et_EE_KertNeural("et-EE-KertNeural", "男", "et-EE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fil_PH_AngeloNeural("fil-PH-AngeloNeural", "男", "fil-PH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fil_PH_BlessicaNeural("fil-PH-BlessicaNeural", "女", "fil-PH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fi_FI_HarriNeural("fi-FI-HarriNeural", "男", "fi-FI"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fi_FI_NooraNeural("fi-FI-NooraNeural", "女", "fi-FI"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_BE_CharlineNeural("fr-BE-CharlineNeural", "女", "fr-BE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_BE_GerardNeural("fr-BE-GerardNeural", "男", "fr-BE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_CA_ThierryNeural("fr-CA-ThierryNeural", "男", "fr-CA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_CA_AntoineNeural("fr-CA-AntoineNeural", "男", "fr-CA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_CA_JeanNeural("fr-CA-JeanNeural", "男", "fr-CA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_CA_SylvieNeural("fr-CA-SylvieNeural", "女", "fr-CA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_FR_VivienneMultilingualNeural("fr-FR-VivienneMultilingualNeural", "女", "fr-FR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_FR_RemyMultilingualNeural("fr-FR-RemyMultilingualNeural", "男", "fr-FR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_FR_DeniseNeural("fr-FR-DeniseNeural", "女", "fr-FR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_FR_EloiseNeural("fr-FR-EloiseNeural", "女", "fr-FR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_FR_HenriNeural("fr-FR-HenriNeural", "男", "fr-FR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_CH_ArianeNeural("fr-CH-ArianeNeural", "女", "fr-CH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fr_CH_FabriceNeural("fr-CH-FabriceNeural", "男", "fr-CH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    gl_ES_RoiNeural("gl-ES-RoiNeural", "男", "gl-ES"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    gl_ES_SabelaNeural("gl-ES-SabelaNeural", "女", "gl-ES"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ka_GE_EkaNeural("ka-GE-EkaNeural", "女", "ka-GE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ka_GE_GiorgiNeural("ka-GE-GiorgiNeural", "男", "ka-GE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    de_AT_IngridNeural("de-AT-IngridNeural", "女", "de-AT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    de_AT_JonasNeural("de-AT-JonasNeural", "男", "de-AT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    de_DE_SeraphinaMultilingualNeural("de-DE-SeraphinaMultilingualNeural", "女", "de-DE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    de_DE_FlorianMultilingualNeural("de-DE-FlorianMultilingualNeural", "男", "de-DE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    de_DE_AmalaNeural("de-DE-AmalaNeural", "女", "de-DE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    de_DE_ConradNeural("de-DE-ConradNeural", "男", "de-DE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    de_DE_KatjaNeural("de-DE-KatjaNeural", "女", "de-DE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    de_DE_KillianNeural("de-DE-KillianNeural", "男", "de-DE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    de_CH_JanNeural("de-CH-JanNeural", "男", "de-CH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    de_CH_LeniNeural("de-CH-LeniNeural", "女", "de-CH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    el_GR_AthinaNeural("el-GR-AthinaNeural", "女", "el-GR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    el_GR_NestorasNeural("el-GR-NestorasNeural", "男", "el-GR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    gu_IN_DhwaniNeural("gu-IN-DhwaniNeural", "女", "gu-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    gu_IN_NiranjanNeural("gu-IN-NiranjanNeural", "男", "gu-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    he_IL_AvriNeural("he-IL-AvriNeural", "男", "he-IL"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    he_IL_HilaNeural("he-IL-HilaNeural", "女", "he-IL"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    hi_IN_MadhurNeural("hi-IN-MadhurNeural", "男", "hi-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    hi_IN_SwaraNeural("hi-IN-SwaraNeural", "女", "hi-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    hu_HU_NoemiNeural("hu-HU-NoemiNeural", "女", "hu-HU"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    hu_HU_TamasNeural("hu-HU-TamasNeural", "男", "hu-HU"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    is_IS_GudrunNeural("is-IS-GudrunNeural", "女", "is-IS"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    is_IS_GunnarNeural("is-IS-GunnarNeural", "男", "is-IS"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    id_ID_ArdiNeural("id-ID-ArdiNeural", "男", "id-ID"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    id_ID_GadisNeural("id-ID-GadisNeural", "女", "id-ID"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ga_IE_ColmNeural("ga-IE-ColmNeural", "男", "ga-IE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ga_IE_OrlaNeural("ga-IE-OrlaNeural", "女", "ga-IE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    it_IT_GiuseppeNeural("it-IT-GiuseppeNeural", "男", "it-IT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    it_IT_DiegoNeural("it-IT-DiegoNeural", "男", "it-IT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    it_IT_ElsaNeural("it-IT-ElsaNeural", "女", "it-IT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    it_IT_IsabellaNeural("it-IT-IsabellaNeural", "女", "it-IT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ja_JP_KeitaNeural("ja-JP-KeitaNeural", "男", "ja-JP"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ja_JP_NanamiNeural("ja-JP-NanamiNeural", "女", "ja-JP"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    jv_ID_DimasNeural("jv-ID-DimasNeural", "男", "jv-ID"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    jv_ID_SitiNeural("jv-ID-SitiNeural", "女", "jv-ID"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    kn_IN_GaganNeural("kn-IN-GaganNeural", "男", "kn-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    kn_IN_SapnaNeural("kn-IN-SapnaNeural", "女", "kn-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    kk_KZ_AigulNeural("kk-KZ-AigulNeural", "女", "kk-KZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    kk_KZ_DauletNeural("kk-KZ-DauletNeural", "男", "kk-KZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    km_KH_PisethNeural("km-KH-PisethNeural", "男", "km-KH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    km_KH_SreymomNeural("km-KH-SreymomNeural", "女", "km-KH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ko_KR_HyunsuNeural("ko-KR-HyunsuNeural", "男", "ko-KR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ko_KR_InJoonNeural("ko-KR-InJoonNeural", "男", "ko-KR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ko_KR_SunHiNeural("ko-KR-SunHiNeural", "女", "ko-KR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    lo_LA_ChanthavongNeural("lo-LA-ChanthavongNeural", "男", "lo-LA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    lo_LA_KeomanyNeural("lo-LA-KeomanyNeural", "女", "lo-LA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    lv_LV_EveritaNeural("lv-LV-EveritaNeural", "女", "lv-LV"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    lv_LV_NilsNeural("lv-LV-NilsNeural", "男", "lv-LV"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    lt_LT_LeonasNeural("lt-LT-LeonasNeural", "男", "lt-LT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    lt_LT_OnaNeural("lt-LT-OnaNeural", "女", "lt-LT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    mk_MK_AleksandarNeural("mk-MK-AleksandarNeural", "男", "mk-MK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    mk_MK_MarijaNeural("mk-MK-MarijaNeural", "女", "mk-MK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ms_MY_OsmanNeural("ms-MY-OsmanNeural", "男", "ms-MY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ms_MY_YasminNeural("ms-MY-YasminNeural", "女", "ms-MY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ml_IN_MidhunNeural("ml-IN-MidhunNeural", "男", "ml-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ml_IN_SobhanaNeural("ml-IN-SobhanaNeural", "女", "ml-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    mt_MT_GraceNeural("mt-MT-GraceNeural", "女", "mt-MT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    mt_MT_JosephNeural("mt-MT-JosephNeural", "男", "mt-MT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    mr_IN_AarohiNeural("mr-IN-AarohiNeural", "女", "mr-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    mr_IN_ManoharNeural("mr-IN-ManoharNeural", "男", "mr-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    mn_MN_BataaNeural("mn-MN-BataaNeural", "男", "mn-MN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    mn_MN_YesuiNeural("mn-MN-YesuiNeural", "女", "mn-MN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ne_NP_HemkalaNeural("ne-NP-HemkalaNeural", "女", "ne-NP"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ne_NP_SagarNeural("ne-NP-SagarNeural", "男", "ne-NP"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    nb_NO_FinnNeural("nb-NO-FinnNeural", "男", "nb-NO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    nb_NO_PernilleNeural("nb-NO-PernilleNeural", "女", "nb-NO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ps_AF_GulNawazNeural("ps-AF-GulNawazNeural", "男", "ps-AF"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ps_AF_LatifaNeural("ps-AF-LatifaNeural", "女", "ps-AF"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fa_IR_DilaraNeural("fa-IR-DilaraNeural", "女", "fa-IR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    fa_IR_FaridNeural("fa-IR-FaridNeural", "男", "fa-IR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    pl_PL_MarekNeural("pl-PL-MarekNeural", "男", "pl-PL"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    pl_PL_ZofiaNeural("pl-PL-ZofiaNeural", "女", "pl-PL"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    pt_BR_ThalitaNeural("pt-BR-ThalitaNeural", "女", "pt-BR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    pt_BR_AntonioNeural("pt-BR-AntonioNeural", "男", "pt-BR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    pt_BR_FranciscaNeural("pt-BR-FranciscaNeural", "女", "pt-BR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    pt_PT_DuarteNeural("pt-PT-DuarteNeural", "男", "pt-PT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    pt_PT_RaquelNeural("pt-PT-RaquelNeural", "女", "pt-PT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ro_RO_AlinaNeural("ro-RO-AlinaNeural", "女", "ro-RO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ro_RO_EmilNeural("ro-RO-EmilNeural", "男", "ro-RO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ru_RU_DmitryNeural("ru-RU-DmitryNeural", "男", "ru-RU"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ru_RU_SvetlanaNeural("ru-RU-SvetlanaNeural", "女", "ru-RU"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sr_RS_NicholasNeural("sr-RS-NicholasNeural", "男", "sr-RS"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sr_RS_SophieNeural("sr-RS-SophieNeural", "女", "sr-RS"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    si_LK_SameeraNeural("si-LK-SameeraNeural", "男", "si-LK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    si_LK_ThiliniNeural("si-LK-ThiliniNeural", "女", "si-LK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sk_SK_LukasNeural("sk-SK-LukasNeural", "男", "sk-SK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sk_SK_ViktoriaNeural("sk-SK-ViktoriaNeural", "女", "sk-SK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sl_SI_PetraNeural("sl-SI-PetraNeural", "女", "sl-SI"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sl_SI_RokNeural("sl-SI-RokNeural", "男", "sl-SI"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    so_SO_MuuseNeural("so-SO-MuuseNeural", "男", "so-SO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    so_SO_UbaxNeural("so-SO-UbaxNeural", "女", "so-SO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_AR_ElenaNeural("es-AR-ElenaNeural", "女", "es-AR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_AR_TomasNeural("es-AR-TomasNeural", "男", "es-AR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_BO_MarceloNeural("es-BO-MarceloNeural", "男", "es-BO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_BO_SofiaNeural("es-BO-SofiaNeural", "女", "es-BO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_CL_CatalinaNeural("es-CL-CatalinaNeural", "女", "es-CL"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_CL_LorenzoNeural("es-CL-LorenzoNeural", "男", "es-CL"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_ES_XimenaNeural("es-ES-XimenaNeural", "女", "es-ES"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_CO_GonzaloNeural("es-CO-GonzaloNeural", "男", "es-CO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_CO_SalomeNeural("es-CO-SalomeNeural", "女", "es-CO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_CR_JuanNeural("es-CR-JuanNeural", "男", "es-CR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_CR_MariaNeural("es-CR-MariaNeural", "女", "es-CR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_CU_BelkysNeural("es-CU-BelkysNeural", "女", "es-CU"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_CU_ManuelNeural("es-CU-ManuelNeural", "男", "es-CU"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_DO_EmilioNeural("es-DO-EmilioNeural", "男", "es-DO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_DO_RamonaNeural("es-DO-RamonaNeural", "女", "es-DO"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_EC_AndreaNeural("es-EC-AndreaNeural", "女", "es-EC"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_EC_LuisNeural("es-EC-LuisNeural", "男", "es-EC"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_SV_LorenaNeural("es-SV-LorenaNeural", "女", "es-SV"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_SV_RodrigoNeural("es-SV-RodrigoNeural", "男", "es-SV"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_GQ_JavierNeural("es-GQ-JavierNeural", "男", "es-GQ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_GQ_TeresaNeural("es-GQ-TeresaNeural", "女", "es-GQ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_GT_AndresNeural("es-GT-AndresNeural", "男", "es-GT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_GT_MartaNeural("es-GT-MartaNeural", "女", "es-GT"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_HN_CarlosNeural("es-HN-CarlosNeural", "男", "es-HN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_HN_KarlaNeural("es-HN-KarlaNeural", "女", "es-HN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_MX_DaliaNeural("es-MX-DaliaNeural", "女", "es-MX"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_MX_JorgeNeural("es-MX-JorgeNeural", "男", "es-MX"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_NI_FedericoNeural("es-NI-FedericoNeural", "男", "es-NI"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_NI_YolandaNeural("es-NI-YolandaNeural", "女", "es-NI"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_PA_MargaritaNeural("es-PA-MargaritaNeural", "女", "es-PA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_PA_RobertoNeural("es-PA-RobertoNeural", "男", "es-PA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_PY_MarioNeural("es-PY-MarioNeural", "男", "es-PY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_PY_TaniaNeural("es-PY-TaniaNeural", "女", "es-PY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_PE_AlexNeural("es-PE-AlexNeural", "男", "es-PE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_PE_CamilaNeural("es-PE-CamilaNeural", "女", "es-PE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_PR_KarinaNeural("es-PR-KarinaNeural", "女", "es-PR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_PR_VictorNeural("es-PR-VictorNeural", "男", "es-PR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_ES_AlvaroNeural("es-ES-AlvaroNeural", "男", "es-ES"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_ES_ElviraNeural("es-ES-ElviraNeural", "女", "es-ES"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_US_AlonsoNeural("es-US-AlonsoNeural", "男", "es-US"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_US_PalomaNeural("es-US-PalomaNeural", "女", "es-US"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_UY_MateoNeural("es-UY-MateoNeural", "男", "es-UY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_UY_ValentinaNeural("es-UY-ValentinaNeural", "女", "es-UY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_VE_PaolaNeural("es-VE-PaolaNeural", "女", "es-VE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    es_VE_SebastianNeural("es-VE-SebastianNeural", "男", "es-VE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    su_ID_JajangNeural("su-ID-JajangNeural", "男", "su-ID"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    su_ID_TutiNeural("su-ID-TutiNeural", "女", "su-ID"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sw_KE_RafikiNeural("sw-KE-RafikiNeural", "男", "sw-KE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sw_KE_ZuriNeural("sw-KE-ZuriNeural", "女", "sw-KE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sw_TZ_DaudiNeural("sw-TZ-DaudiNeural", "男", "sw-TZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sw_TZ_RehemaNeural("sw-TZ-RehemaNeural", "女", "sw-TZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sv_SE_MattiasNeural("sv-SE-MattiasNeural", "男", "sv-SE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    sv_SE_SofieNeural("sv-SE-SofieNeural", "女", "sv-SE"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ta_IN_PallaviNeural("ta-IN-PallaviNeural", "女", "ta-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ta_IN_ValluvarNeural("ta-IN-ValluvarNeural", "男", "ta-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ta_MY_KaniNeural("ta-MY-KaniNeural", "女", "ta-MY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ta_MY_SuryaNeural("ta-MY-SuryaNeural", "男", "ta-MY"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ta_SG_AnbuNeural("ta-SG-AnbuNeural", "男", "ta-SG"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ta_SG_VenbaNeural("ta-SG-VenbaNeural", "女", "ta-SG"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ta_LK_KumarNeural("ta-LK-KumarNeural", "男", "ta-LK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ta_LK_SaranyaNeural("ta-LK-SaranyaNeural", "女", "ta-LK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    te_IN_MohanNeural("te-IN-MohanNeural", "男", "te-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    te_IN_ShrutiNeural("te-IN-ShrutiNeural", "女", "te-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    th_TH_NiwatNeural("th-TH-NiwatNeural", "男", "th-TH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    th_TH_PremwadeeNeural("th-TH-PremwadeeNeural", "女", "th-TH"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    tr_TR_AhmetNeural("tr-TR-AhmetNeural", "男", "tr-TR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    tr_TR_EmelNeural("tr-TR-EmelNeural", "女", "tr-TR"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    uk_UA_OstapNeural("uk-UA-OstapNeural", "男", "uk-UA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    uk_UA_PolinaNeural("uk-UA-PolinaNeural", "女", "uk-UA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ur_IN_GulNeural("ur-IN-GulNeural", "女", "ur-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ur_IN_SalmanNeural("ur-IN-SalmanNeural", "男", "ur-IN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ur_PK_AsadNeural("ur-PK-AsadNeural", "男", "ur-PK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    ur_PK_UzmaNeural("ur-PK-UzmaNeural", "女", "ur-PK"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    uz_UZ_MadinaNeural("uz-UZ-MadinaNeural", "女", "uz-UZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    uz_UZ_SardorNeural("uz-UZ-SardorNeural", "男", "uz-UZ"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    vi_VN_HoaiMyNeural("vi-VN-HoaiMyNeural", "女", "vi-VN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    vi_VN_NamMinhNeural("vi-VN-NamMinhNeural", "男", "vi-VN"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    cy_GB_AledNeural("cy-GB-AledNeural", "男", "cy-GB"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    cy_GB_NiaNeural("cy-GB-NiaNeural", "女", "cy-GB"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    zu_ZA_ThandoNeural("zu-ZA-ThandoNeural", "女", "zu-ZA"),
    // {"VoicePersonalities":["Friendly","Positive"],"ContentCategories":["General"]}
    zu_ZA_ThembaNeural("zu-ZA-ThembaNeural", "男", "zu-ZA"),




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

    public static VoiceEnum of(String name) {
        for (VoiceEnum v : VoiceEnum.values()) {
            if (v.name().equalsIgnoreCase(name)) {
                return v;
            }
        }
        return null;
    }

    public static VoiceEnum ofOrDefault(String name) {
        return Optional.ofNullable(VoiceEnum.of(name)).orElse(VoiceEnum.zh_CN_XiaoxiaoNeural);
    }
}
