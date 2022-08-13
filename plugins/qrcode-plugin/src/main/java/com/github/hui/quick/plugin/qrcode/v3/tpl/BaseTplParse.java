package com.github.hui.quick.plugin.qrcode.v3.tpl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YiHui
 * @date 2022/8/13
 */
public class BaseTplParse {
    private static final String SIZE_SPLIT = ",";
    private static final String SIZE_WH_SPLIT = "x";

    /**
     * size解析
     *
     * @param size 形如 1x2,2x4
     * @return
     */
    public static List<ImmutablePair<Integer, Integer>> decodeSize(String size) {
        List<ImmutablePair<Integer, Integer>> result = new ArrayList<>();
        if (StringUtils.isBlank(size)) {
            result.add(ImmutablePair.of(1, 1));
            return result;
        }

        for (String sub : StringUtils.split(size, SIZE_SPLIT)) {
            String[] wh = StringUtils.split(sub, SIZE_WH_SPLIT);
            result.add(new ImmutablePair<>(Integer.valueOf(wh[0].trim()), Integer.valueOf(wh[1].trim())));
        }
        return result;
    }

}
