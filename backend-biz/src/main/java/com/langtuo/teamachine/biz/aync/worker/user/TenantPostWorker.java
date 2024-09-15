package com.langtuo.teamachine.biz.aync.worker.user;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.dao.accessor.user.OrgAccessor;
import com.langtuo.teamachine.dao.accessor.user.PermitActAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleActRelAccessor;
import com.langtuo.teamachine.dao.constant.DaoConsts;
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
            throw new IllegalArgumentException("tenantCode is blank");
        }
    }

    @Override
    public void run() {
        RolePO rolePO = new RolePO();
        rolePO.setTenantCode(tenantCode);
        rolePO.setRoleCode(DaoConsts.ROLE_CODE_TENANT_SUPER);
        rolePO.setRoleName(DaoConsts.ROLE_NAME_TENANT_SUPER);
        rolePO.setSysReserved(DaoConsts.ROLE_SYS_RESERVED);

        RoleAccessor roleAccessor = SpringAccessorUtils.getRoleAccessor();
        int inserted4Role = roleAccessor.insert(rolePO);

        PermitActAccessor permitActAccessor = SpringAccessorUtils.getPermitActAccessor();
        List<PermitActPO> permitActPOList = permitActAccessor.selectPermitActList();
        if (CollectionUtils.isEmpty(permitActPOList)) {
            return;
        }

        RoleActRelAccessor roleActRelAccessor = SpringAccessorUtils.getRoleActRelAccessor();
        for (PermitActPO permitActPO : permitActPOList) {
            RoleActRelPO actRelPO = new RoleActRelPO();
            actRelPO.setTenantCode(tenantCode);
            actRelPO.setRoleCode(DaoConsts.ROLE_CODE_TENANT_SUPER);
            actRelPO.setPermitActCode(permitActPO.getPermitActCode());
            int inserted4ActRel = roleActRelAccessor.insert(actRelPO);
        }

        OrgNode orgNode = new OrgNode();
        orgNode.setTenantCode(tenantCode);
        orgNode.setOrgName(DaoConsts.ORG_NAME_TOP);

        OrgAccessor orgAccessor = SpringAccessorUtils.getOrgAccessor();
        int inserted4Org = orgAccessor.insert(orgNode);
    }
}
