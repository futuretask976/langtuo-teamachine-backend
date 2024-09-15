package com.langtuo.teamachine.biz.aync.worker.user;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.user.OrgDTO;
import com.langtuo.teamachine.api.model.user.PermitActDTO;
import com.langtuo.teamachine.api.model.user.RoleDTO;
import com.langtuo.teamachine.api.request.user.OrgPutRequest;
import com.langtuo.teamachine.api.request.user.RolePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.OrgMgtService;
import com.langtuo.teamachine.api.service.user.PermitActMgtService;
import com.langtuo.teamachine.api.service.user.RoleMgtService;
import com.langtuo.teamachine.biz.util.SpringServiceUtils;
import com.langtuo.teamachine.dao.constant.DaoConsts;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getListModel;
import static com.langtuo.teamachine.api.result.TeaMachineResult.getModel;

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
        RoleMgtService roleMgtService = SpringServiceUtils.getRoleMgtService();
        RoleDTO superRole4Tenant = getModel(roleMgtService.getByCode(tenantCode, DaoConsts.ROLE_CODE_TENANT_SUPER));
        if (superRole4Tenant == null) {
            RolePutRequest rolePutRequest = new RolePutRequest();
            rolePutRequest.setTenantCode(tenantCode);
            rolePutRequest.setRoleCode(DaoConsts.ROLE_CODE_TENANT_SUPER);
            rolePutRequest.setRoleName(DaoConsts.ROLE_NAME_TENANT_SUPER);
            rolePutRequest.setSysReserved(DaoConsts.ROLE_SYS_RESERVED);

            PermitActMgtService permitActMgtService = SpringServiceUtils.getPermitActMgtService();
            List<PermitActDTO> permitActDTOList = getListModel(permitActMgtService.listPermitAct());
            rolePutRequest.setPermitActCodeList(permitActDTOList.stream()
                    .map(PermitActDTO::getPermitActCode)
                    .collect(Collectors.toList()));

            TeaMachineResult<Void> putRtn = roleMgtService.put(rolePutRequest);
            if (putRtn == null || !putRtn.isSuccess()) {
                log.error("insert tenant super role error: " + putRtn == null ? null : putRtn.getErrorMsg());
            }
        }

        OrgMgtService orgMgtService = SpringServiceUtils.getOrgMgtService();
        OrgDTO orgDTO = getModel(orgMgtService.get(tenantCode, DaoConsts.ORG_NAME_TOP));
        if (orgDTO == null) {
            OrgPutRequest orgPutRequest = new OrgPutRequest();
            orgPutRequest.setTenantCode(tenantCode);
            orgPutRequest.setOrgName(DaoConsts.ORG_NAME_TOP);
            TeaMachineResult<Void> putRtn = orgMgtService.put(orgPutRequest);
            if (putRtn == null || !putRtn.isSuccess()) {
                log.error("insert org top error: " + putRtn == null ? null : putRtn.getErrorMsg());
            }
        }
    }
}
