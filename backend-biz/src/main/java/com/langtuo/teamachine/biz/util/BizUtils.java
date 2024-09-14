package com.langtuo.teamachine.biz.util;

import com.langtuo.teamachine.dao.node.user.OrgNode;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import com.langtuo.teamachine.dao.po.user.AdminPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class BizUtils {
    /**
     *
     * @param length
     * @return
     */
    public static String genRandomStr(int length) {
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CommonConsts.DEPLOY_CHAR_SET.length());
            char randomChar = CommonConsts.DEPLOY_CHAR_SET.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }






}
