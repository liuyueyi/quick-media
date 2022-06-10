package com.github.hui.quick.plugin.test.feature;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class SvgRender {

    private static final String PARAM = "%s";

    private static final String PARAM_REGEX = "\\%s";

    public static String write(String phrasePath, Object... args) {
        String ret = new String(phrasePath);
        try {
            if (phrasePath != null) {
                int argsIndex = 1;
                while (phrasePath.indexOf(PARAM) != -1) {
                    if (args == null || args.length < argsIndex) {
                        throw new RuntimeException("格式不对");
                    }
                    phrasePath = phrasePath.replaceFirst(PARAM_REGEX, "\"" + rebuild(String.valueOf(args[argsIndex - 1])) + "\"");
                    argsIndex++;
                }
            }
        } catch (Exception e) {
            return ret;
        }
        return phrasePath;
    }

    private static String rebuild(String content) {
        StringBuffer buf = new StringBuffer();
        char[] chs = content.toCharArray();
        for (char ch : chs) {
            if (ch == '$') {
                buf.append("\\$");
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    private static final String ORDER_LOG_SPLIT_TAG = ",";
    private static final String ORDER_LOG_START_END_TAG = "\"";

    public static String processOrderLogDesc(String desc) {
        if (!desc.startsWith("${")) {
            // 非特定开头的，特殊处理
            return desc;
        }

        int argStartIndex = desc.indexOf(":");
        if (argStartIndex < 0) {
            return desc;
        }

        StringBuilder result = new StringBuilder();

        // 基于KEY_ACTION的日志输出格式形如  ${前缀:"参数1","参数2","参数3"}
        result.append(desc, 0, argStartIndex + 1);
        String args = desc.substring(argStartIndex + 1, desc.length() - 1);
        String[] cells = StringUtils.splitByWholeSeparatorPreserveAllTokens(args, ORDER_LOG_START_END_TAG + ORDER_LOG_SPLIT_TAG + ORDER_LOG_START_END_TAG);
        for (int i = 0; i < cells.length; i++) {
            String cell = cells[i];
            if (i == 0 && cell.startsWith(ORDER_LOG_START_END_TAG)) {
                cell = cell.substring(1);
            } else if (i == cells.length - 1 && cell.endsWith(ORDER_LOG_START_END_TAG)) {
                cell = cell.substring(0, cell.length() - 1);
            }
            if (i > 0) {
                result.append(ORDER_LOG_SPLIT_TAG);
            }
            result.append(ORDER_LOG_START_END_TAG).append(privacy(cell)).append(ORDER_LOG_START_END_TAG);
        }
        return result.append("}").toString();
    }

    public static String privacy(String desc) {
        if (desc.startsWith("${")) {
            // 嵌套的替换标识
            return desc;
        } else if (desc.length() == 1) {
            return "*";
        } else if (desc.length() == 2) {
            return desc.charAt(0) + "*";
        } else {
            return desc.charAt(0) + "*" + desc.charAt(desc.length() - 1);
        }
    }

    @Test
    public void test() {
        String phase = "${test:%s,%s,%s}";
        String a = write(phase, "测试", "\"a", "demo,demo\"\"");
        String ac = processOrderLogDesc(a);
        System.out.printf("-->%s\n-->%s\n", a, ac);
        System.out.println();

        String b = write(phase, "测试", "\"aa\"", "demo,demo\"\"");
        String bc = processOrderLogDesc(b);
        System.out.printf("-->%s\n-->%s\n", b, bc);
        System.out.println();

        String c = write(phase, "${hello:world}", "\"aa\"", "demo,demo\"\"");
        String cc = processOrderLogDesc(c);
        System.out.printf("-->%s\n-->%s\n", c, cc);
    }

}
