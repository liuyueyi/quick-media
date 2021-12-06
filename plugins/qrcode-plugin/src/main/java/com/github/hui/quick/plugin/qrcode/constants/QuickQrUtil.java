package com.github.hui.quick.plugin.qrcode.constants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import java.awt.*;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by @author yihui in 21:13 20/6/23.
 */
public class QuickQrUtil {

    public static String DEFAULT_QR_TXT =
            "天地玄黄宇宙洪荒日月盈昃辰宿列张寒来暑往秋收冬藏闰余成岁律吕调阳云腾致雨露结为霜金生丽水玉出昆冈剑号巨阙珠称夜光果珍李柰菜重芥姜海咸河淡鳞潜羽翔龙师火帝鸟官人皇始制文字乃服衣裳推位让国有虞陶唐吊民伐罪周发殷汤坐朝问道垂拱平章爱育黎首臣伏戎羌遐迩一体率宾归王鸣凤在竹白驹食\n" +
                    "场化被草木赖及万方盖此身发四大五常恭惟鞠养岂敢毁伤女慕贞洁男效才良知过必改得能莫忘罔谈彼短靡恃己长信使可覆器欲难量墨悲丝染诗赞羔羊景行维贤克念作圣德建名立形端表正空谷传声虚堂习听祸因恶积福缘善庆尺璧非宝寸阴是竞资父事君曰严与敬孝当竭力忠则尽命临深履薄夙兴温凊似兰斯馨如松\n" +
                    "之盛川流不息渊澄取映容止若思言辞安定笃初诚美慎终宜令荣业所基籍甚无竟学优登仕摄职从政存以甘棠去而益咏乐殊贵贱礼别尊卑上和下睦夫唱妇随外受傅训入奉母仪诸姑伯叔犹子比儿孔怀兄弟同气连枝交友投分切磨箴规仁慈隐恻造次弗离节义廉退颠沛匪亏性静情逸心动神疲守真志满逐物意移坚持雅操好\n" +
                    "爵自縻都邑华夏东西二京背邙面洛浮渭据泾宫殿盘郁楼观飞惊图写禽兽画彩仙灵丙舍旁启甲帐对楹肆筵设席鼓瑟吹笙升阶纳陛弁转疑星右通广内左达承明既集坟典亦聚群英杜稿钟隶漆书壁经府罗将相路侠槐卿户封八县家给千兵高冠陪辇驱毂振缨世禄侈富车驾肥轻策功茂实勒碑刻铭盘溪伊尹佐时阿衡奄宅曲阜\n" +
                    "微旦孰营桓公匡合济弱扶倾绮回汉惠说感武丁俊义密勿多士实宁晋楚更霸赵魏困横假途灭虢践土会盟何遵约法韩弊烦刑起翦颇牧用军最精宣威沙漠驰誉丹青九州禹迹百郡秦并岳宗泰岱禅主云亭雁门紫塞鸡田赤诚昆池碣石钜野洞庭旷远绵邈岩岫杳冥治本于农务兹稼穑俶载南亩我艺黍稷税熟贡新劝赏黜陟孟轲敦\n" +
                    "素史鱼秉直庶几中庸劳谦谨敕聆音察理鉴貌辨色贻厥嘉猷勉其祗植省躬讥诫宠增抗极殆辱近耻林皋幸即两疏见机解组谁逼索居闲处沉默寂寥求古寻论散虑逍遥欣奏累遣戚谢欢招渠荷的历园莽抽条枇杷晚翠梧桐蚤凋陈根委翳落叶飘摇游鹍独运凌摩绛霄耽读玩市寓目囊箱易輶攸畏属耳垣墙具膳餐饭适口充肠饱饫\n" +
                    "烹宰饥厌糟糠亲戚故旧老少异粮妾御绩纺侍巾帷房纨扇圆洁银烛炜煌昼眠夕寐蓝笋象床弦歌酒宴接杯举殇矫手顿足悦豫且康嫡后嗣续祭祀烝尝稽颡再拜悚惧恐惶笺牒简要顾答审详骸垢想浴执热愿凉驴骡犊特骇跃超骧诛斩贼盗捕获叛亡布射僚丸嵇琴阮箫恬笔伦纸钧巧任钓释纷利俗并皆佳妙毛施淑姿工颦妍笑年\n" +
                    "矢每催曦晖朗曜璇玑悬斡晦魄环照指薪修祜永绥吉劭矩步引领俯仰廊庙束带矜庄徘徊瞻眺孤陋寡闻愚蒙等诮谓语助者焉哉乎也";

    public static Font DEFAULT_FONT;
    public static String DEFAULT_FONT_NAME = "宋体";
    public static int DEFAULT_FONT_STYLE = Font.BOLD;

    private static Map<Triple<String, Integer, Integer>, Font> fontCache = new ConcurrentHashMap<>();

    static {
        DEFAULT_FONT = new Font(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, 5);
        fontCache.put(Triple.of(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, 5), DEFAULT_FONT);
    }

    public static Font font(String name, int style, int fontSize) {
        Triple<String, Integer, Integer> triple = Triple.of(name, style, fontSize);
        return fontCache.computeIfAbsent(triple, (k) -> new Font(k.getLeft(), k.getMiddle(), k.getRight()));
    }


    private static Random RANDOM = new Random();

    public static ThreadLocal<AtomicInteger> TXT_INDEX_LOCAL = new ThreadLocal<>();

    public static int getIndex() {
        // 实际上是否加锁，对业务影响并不大
        AtomicInteger integer = TXT_INDEX_LOCAL.get();
        if (integer == null) {
            synchronized (QuickQrUtil.class) {
                integer = TXT_INDEX_LOCAL.get();
                if (integer == null) {
                    integer = new AtomicInteger();
                    TXT_INDEX_LOCAL.set(integer);
                }
            }
        }
        return integer.getAndIncrement();
    }

    public static void clear() {
        TXT_INDEX_LOCAL.remove();
    }

    public static String qrTxt(String txt, boolean random) {
        if (StringUtils.isBlank(txt)) {
            return "";
        }

        int index;
        if (random) {
            index = RANDOM.nextInt(txt.length());
        } else {
            index = getIndex() % txt.length();
        }

        return txt.substring(index, index + 1);
    }
}
