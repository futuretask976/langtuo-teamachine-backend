package com.langtuo.teamachine.biz.service.util;

import com.langtuo.teamachine.dao.node.user.OrgNode;
import com.langtuo.teamachine.dao.po.user.AdminPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class BizUtils {
    /**
     * 根据当前登录的管理员获取其下属的组织列表
     * @param tenantCode
     * @return
     */
    public static List<String> getAdminOrgNameList(String tenantCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalArgumentException("couldn't find login session");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String adminLoginName = userDetails.getUsername();
        if (StringUtils.isBlank(adminLoginName)) {
            throw new IllegalArgumentException("couldn't find login session");
        }

        AdminPO adminPO = SpringUtils.getAdminAccessor().selectOneByLoginName(tenantCode, adminLoginName);
        String orgName = adminPO.getOrgName();
        List<OrgNode> orgPOList = SpringUtils.getOrgAccessor().selectListByParent(tenantCode, orgName);
        List<String> orgNameList = orgPOList.stream()
                .map(orgNode -> orgNode.getOrgName())
                .collect(Collectors.toList());
        return orgNameList;
    }
}
