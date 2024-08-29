package com.langtuo.teamachine.mqtt.consume.worker.user;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.user.PermitActDTO;
import com.langtuo.teamachine.api.model.user.RoleDTO;
import com.langtuo.teamachine.api.request.user.RolePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.AdminMgtService;
import com.langtuo.teamachine.api.service.user.PermitActMgtService;
import com.langtuo.teamachine.api.service.user.RoleMgtService;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.util.SpringUtils;
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
        this.tenantCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_TENANT_CODE);
        if (StringUtils.isBlank(tenantCode)) {
            throw new IllegalArgumentException("tenantCode is blank");
        }
    }

    @Override
    public void run() {
        RoleMgtService roleMgtService = SpringUtils.getRoleMgtService();
        RoleDTO superRole4Tenant = getModel(roleMgtService.getByCode(tenantCode, "role_tenant_super_admin"));
        superRole4Tenant = null;
        if (superRole4Tenant == null) {
            RolePutRequest rolePutRequest = new RolePutRequest();
            rolePutRequest.setTenantCode(tenantCode);
            rolePutRequest.setRoleCode("role_tenant_super_admin");
            rolePutRequest.setRoleName("租户超级管理员");
            rolePutRequest.setSysReserved(1);

            PermitActMgtService permitActMgtService = SpringUtils.getPermitActMgtService();
            List<PermitActDTO> permitActDTOList = getListModel(permitActMgtService.listPermitAct());
            rolePutRequest.setPermitActCodeList(permitActDTOList.stream()
                    .map(PermitActDTO::getPermitActCode)
                    .collect(Collectors.toList()));

            TeaMachineResult<Void> putRtn = roleMgtService.put(rolePutRequest);
            if (putRtn == null || !putRtn.isSuccess()) {
                log.error("insert tenant super role error: " + putRtn == null ? null : putRtn.getErrorMsg());
            }
        }
    }
}
