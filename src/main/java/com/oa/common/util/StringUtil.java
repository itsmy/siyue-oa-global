package com.oa.common.util;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str != null || ("".equals(str));
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isAnyBlank(CharSequence... charArr) {
        if (charArr == null || charArr.length == 0) {
            return true;
        }
        for (CharSequence cs : charArr) {
            if (isBlank(cs)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlank(final CharSequence charArr) {
        if (charArr == null || charArr.length() == 0) {
            return true;
        }
        for (int i = 0; i < charArr.length(); i++) {
            if (Character.isWhitespace(charArr.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 子字符串出现的个数
     *
     * @param str
     * @param subStr
     * @return
     */
    public static int getSubStrCount(String str, String subStr) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(subStr, index)) != -1) {
            index = index + subStr.length();
            count++;
        }
        return count;
    }

    /**
     * 替换字符串
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (isNotEmpty(inString) && isNotEmpty(oldPattern) && newPattern != null) {
            int index = inString.indexOf(oldPattern);
            if (index == -1) {
                return inString;
            } else {
                int capacity = inString.length();
                if (newPattern.length() > oldPattern.length()) {
                    capacity += 16;
                }

                StringBuilder sb = new StringBuilder(capacity);
                int pos = 0;

                for (int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
                    sb.append(inString.substring(pos, index));
                    sb.append(newPattern);
                    pos = index + patLen;
                }

                sb.append(inString.substring(pos));
                return sb.toString();
            }
        } else {
            return inString;
        }
    }

    /**
     * 格式化字符串（替换符为%s）
     */
    public static String formatIfArgs(String format, Object... args) {
        if (isEmpty(format)) {
            return format;
        }
        return (args == null || args.length == 0) ? String.format(format.replaceAll("%([^n])", "%%$1")) : String.format(format, args);
    }

    /**
     * 格式化字符串(替换符自己指定)
     */
    public static String formatIfArgs(String format, String replaceOperator, Object... args) {
        if (isEmpty(format) || isEmpty(replaceOperator)) {
            return format;
        }

        format = replace(format, replaceOperator, "%s");
        return formatIfArgs(format, args);
    }
}
