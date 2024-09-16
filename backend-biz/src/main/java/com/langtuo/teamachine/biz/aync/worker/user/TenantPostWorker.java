package com.langtuo.teamachine.biz.aync.worker.user;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.dao.accessor.user.OrgAccessor;
import com.langtuo.teamachine.dao.accessor.user.PermitActAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleActRelAccessor;
import com.langtuo.teamachine.dao.node.user.OrgNode;
import com.langtuo.teamachine.dao.po.user.PermitActPO;
import com.langtuo.teamachine.dao.po.user.RoleActRelPO;
import com.langtuo.teamachine.dao.po.user.RolePO;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
public class TenantPostWorker implements Runnable {
    /**
     *
     */
    private String tenantCode;

    public TenantPostWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(CommonConsts.JSON_KEY_TENANT_CODE);
        if (StringUtils.isBlank(tenantCode)) {
            log.error("tenantPostWorker|init|illegalArgument|" + tenantCode);
            throw new IllegalArgumentException("tenantCode is blank");
        }
    }

    @Override
    public void run() {
        RolePO rolePO = new RolePO();
        rolePO.setTenantCode(tenantCode);
        rolePO.setRoleCode(CommonConsts.ROLE_CODE_TENANT_SUPER);
        rolePO.setRoleName(CommonConsts.ROLE_NAME_TENANT_SUPER);
        rolePO.setSysReserved(CommonConsts.ROLE_SYS_RESERVED);

        RoleAccessor roleAccessor = SpringAccessorUtils.getRoleAccessor();
        int inserted4Role = roleAccessor.insert(rolePO);
        if (CommonConsts.NUM_ONE != inserted4Role) {
            log.error("tenantPostWorker|insertRole|error|" + inserted4Role);
        }

        PermitActAccessor permitActAccessor = SpringAccessorUtils.getPermitActAccessor();
        List<PermitActPO> permitActPOList = permitActAccessor.selectPermitActList();
        if (CollectionUtils.isEmpty(permitActPOList)) {
            log.error("tenantPostWorker|getPermitActPOList|error|" + permitActPOList);
            return;
        }

        RoleActRelAccessor roleActRelAccessor = SpringAccessorUtils.getRoleActRelAccessor();
        for (PermitActPO permitActPO : permitActPOList) {
            RoleActRelPO actRelPO = new RoleActRelPO();
            actRelPO.setTenantCode(tenantCode);
            actRelPO.setRoleCode(CommonConsts.ROLE_CODE_TENANT_SUPER);
            actRelPO.setPermitActCode(permitActPO.getPermitActCode());
            int inserted4ActRel = roleActRelAccessor.insert(actRelPO);
            if (CommonConsts.NUM_ONE != inserted4Role) {
                log.error("tenantPostWorker|insert4ActRel|error|" + inserted4ActRel);
            }
        }

        OrgNode orgNode = new OrgNode();
        orgNode.setTenantCode(tenantCode);
        orgNode.setOrgName(CommonConsts.ORG_NAME_TOP);

        OrgAccessor orgAccessor = SpringAccessorUtils.getOrgAccessor();
        int inserted4Org = orgAccessor.insert(orgNode);
        if (CommonConsts.NUM_ONE != inserted4Org) {
            log.error("tenantPostWorker|insert4Org|error|" + inserted4Org);
        }
    }
}
