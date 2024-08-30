package com.langtuo.teamachine.biz.service.util;

import com.langtuo.teamachine.biz.service.constant.BizConsts;

import java.security.SecureRandom;

public class DeployUtils {
    public static String genRandomStr(int length) {
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(BizConsts.DEPLOY_CHAR_SET.length());
            char randomChar = BizConsts.DEPLOY_CHAR_SET.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
