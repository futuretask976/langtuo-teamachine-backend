package com.langtuo.teamachine.dao.util;

import com.langtuo.teamachine.dao.accessor.device.MachineAccessor;
import com.langtuo.teamachine.dao.accessor.user.TenantAccessor;
import com.langtuo.teamachine.dao.node.user.OrgNode;
import com.langtuo.teamachine.dao.po.device.MachinePO;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import com.langtuo.teamachine.dao.po.user.AdminPO;
import com.langtuo.teamachine.dao.po.user.TenantPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DaoUtils {
    public static List<String> getTenantCodeList() {
        TenantAccessor tenantAccessor = SpringAccessorUtils.getTenantAccessor();
        List<TenantPO> list = tenantAccessor.selectList();

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(TenantPO::getTenantCode).collect(Collectors.toList());
    }

    /**
     * 获取指定店铺编码列表下的机器列表
     * @param tenantCode
     * @param shopCodeList
     * @return
     */
    public static List<String> getMachineCodeListByShopCodeList(String tenantCode, List<String> shopCodeList) {
        if (StringUtils.isBlank(tenantCode) || CollectionUtils.isEmpty(shopCodeList)) {
            return null;
        }

        MachineAccessor machineAccessor = SpringAccessorUtils.getMachineItemAccessor();
        List<String> machineCodeList = shopCodeList.stream()
                .map(shopCode -> {
                    List<MachinePO> machinePOList = machineAccessor.selectListByShopCode(tenantCode, shopCode);
                    if (CollectionUtils.isEmpty(machinePOList)) {
                        return null;
                    }
                    return machinePOList.stream()
                            .map(shop -> shop.getMachineCode())
                            .collect(Collectors.toList());
                })
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return machineCodeList;
    }

    /**
     * 根据店铺组编码获取其关联的店铺编码列表
     * @param tenantCode
     * @return
     */
    public static List<String> getShopCodeListByShopGroupCodeList(String tenantCode, List<String> shopGroupCodeList) {
        if (CollectionUtils.isEmpty(shopGroupCodeList)) {
            return null;
        }

        List<ShopPO> shopPOList = SpringAccessorUtils.getShopAccessor().selectListByShopGroupCode(tenantCode,
                shopGroupCodeList);
        if (CollectionUtils.isEmpty(shopPOList)) {
            return null;
        }

        List<String> shopCodeList = shopPOList.stream()
                .map(ShopPO::getShopCode)
                .collect(Collectors.toList());
        return shopCodeList;
    }

    /**
     * 根据当前登录的管理员获取其下属的店铺编码列表
     * @param tenantCode
     * @return
     */
    public static List<String> getShopCodeListByAdmin(String tenantCode) {
        List<String> orgNameListByAdmin = getOrgNameListByAdmin(tenantCode);
        if (CollectionUtils.isEmpty(orgNameListByAdmin)) {
            return null;
        }

        List<String> shopGroupCodeList = getShopGroupCodeListByAdmin(tenantCode);
        if (CollectionUtils.isEmpty(shopGroupCodeList)) {
            return null;
        }

        List<ShopPO> shopPOList = SpringAccessorUtils.getShopAccessor().selectListByShopGroupCode(tenantCode,
                shopGroupCodeList);
        if (CollectionUtils.isEmpty(shopPOList)) {
            return null;
        }
        List<String> shopCodeListByAdmin = shopPOList.stream()
                .map(ShopPO::getShopCode)
                .collect(Collectors.toList());
        return shopCodeListByAdmin;
    }

    /**
     * 根据当前登录的管理员获取其下属的店铺组编码列表
     * @param tenantCode
     * @return
     */
    public static List<String> getShopGroupCodeListByAdmin(String tenantCode) {
        List<String> orgNameListByAdmin = getOrgNameListByAdmin(tenantCode);
        if (CollectionUtils.isEmpty(orgNameListByAdmin)) {
            return null;
        }

        List<ShopGroupPO> shopGroupPOList = SpringAccessorUtils.getShopGroupAccessor()
                .selectListByOrgName(tenantCode, orgNameListByAdmin);
        if (CollectionUtils.isEmpty(shopGroupPOList)) {
            return null;
        }
        List<String> shopGroupCodeListByAdmin = shopGroupPOList.stream()
                .map(ShopGroupPO::getShopGroupCode)
                .collect(Collectors.toList());
        return shopGroupCodeListByAdmin;
    }

    /**
     * 根据当前登录的管理员获取其下属的组织名称列表
     * @param tenantCode
     * @return
     */
    public static List<String> getOrgNameListByAdmin(String tenantCode) {
        AdminPO adminPO = getLoginAdminPO(tenantCode);
        String orgName = adminPO.getOrgName();
        List<OrgNode> orgPOList = SpringAccessorUtils.getOrgAccessor().selectListByParent(tenantCode, orgName);
        List<String> orgNameList = orgPOList.stream()
                .map(OrgNode::getOrgName)
                .collect(Collectors.toList());
        return orgNameList;
    }

    /**
     * 获取当前登录的管理员
     * @param tenantCode
     * @return
     */
    public static AdminPO getLoginAdminPO(String tenantCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalArgumentException("couldn't find login session");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String adminLoginName = userDetails.getUsername();
        if (StringUtils.isBlank(adminLoginName)) {
            throw new IllegalArgumentException("couldn't find login session");
        }

        AdminPO adminPO = SpringAccessorUtils.getAdminAccessor().selectOneByLoginName(tenantCode, adminLoginName);
        return adminPO;
    }
}