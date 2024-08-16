package com.langtuo.teamachine.api.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class RegexUtils {
    public static boolean isValidCode(String input, boolean required) {
        if (StringUtils.isBlank(input)) {
            if (!required) {
                return true;
            }
        } else {
            String pattern = "^[A-Za-z0-9_]{0,50}$";
            boolean isMatch = Pattern.matches(pattern, input);
            if (isMatch) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidName(String input, boolean required) {
        if (StringUtils.isBlank(input)) {
            if (!required) {
                return true;
            }
        } else {
            String pattern = "^[A-Za-z0-9_\\u4e00-\\u9fa5]{0,50}$";
            boolean isMatch = Pattern.matches(pattern, input);
            if (isMatch) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidText(String input, boolean required) {
        if (StringUtils.isBlank(input)) {
            if (!required) {
                return true;
            }
        } else {
            String pattern = "^[A-Za-z0-9_\\u4e00-\\u9fa5\\p{P}]{0,50}$";
            boolean isMatch = Pattern.matches(pattern, input);
            if (isMatch) {
                return true;
            }
        }
        return false;
    }

    public static void main(String args[]) {
//        System.out.println(isValidStr(null, true));
//        System.out.println(isValidStr("", true));
//        System.out.println(isValidStr(null, false));
//        System.out.println(isValidStr("", false));
//        System.out.println(isValidStr("aAbce01234", false));
//        System.out.println(isValidStr("aAbce_01234", false));
//        System.out.println(isValidStr("aAbce-01234", false));
        System.out.println(isValidText("这里是我的", false));
    }
}
