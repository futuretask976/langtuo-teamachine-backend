package com.langtuo.teamachine.biz.service.util;

import java.security.SecureRandom;

public class StringUtils {
    public static String genRandomStr(int length) {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charSet.length());
            char randomChar = charSet.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        for (int i = 0; i < 10000; i++) {
            System.out.println(genRandomStr(30));
        }
        System.out.println(System.currentTimeMillis());
    }
}
