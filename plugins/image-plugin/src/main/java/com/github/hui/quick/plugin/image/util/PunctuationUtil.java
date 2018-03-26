package com.github.hui.quick.plugin.image.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 标点符号工具类
 *
 * Created by yihui on 2017/9/6.
 */
public class PunctuationUtil {

    private static char[] chinesePunctuation = new char[]{
            '。',
            '，',
            '？',
            '！',
            '、',
            '；',
            '：',
            '“',
            '”',
            '‘',
            '’',
            '【',
            '】',
            '（',
            '）',
            '《',
            '》',
            '·'
    };

    private static  Set<Character> set= new HashSet<>();

    static {
        for(char ch: chinesePunctuation) {
            set.add(ch);
        }
    }



    public static boolean isPunctuation(char ch) {
        return set.contains(ch);
    }

}
