package com.langtuo.teamachine.biz.service.util;

import java.security.SecureRandom;

public class DeployUtils {
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String genRandomStr(int length) {
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHAR_SET.length());
            char randomChar = CHAR_SET.charAt(randomIndex);
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
