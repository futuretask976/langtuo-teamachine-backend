package com.langtuo.teamachine.biz.service.impl;

import com.langtuo.teamachine.api.model.AdminRoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.AdminRolePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.AdminRoleMgtService;
import com.langtuo.teamachine.dao.accessor.AdminRoleAccessor;
import com.langtuo.teamachine.dao.accessor.AdminRolePermitActRelAccessor;
import com.langtuo.teamachine.dao.po.AdminRolePO;

import javax.annotation.Resource;

public class AdminRoleMgtServiceImpl implements AdminRoleMgtService {
    @Resource
    private AdminRoleAccessor adminRoleAccessor;

    @Resource
    private AdminRolePermitActRelAccessor adminRolePermitActRelAccessor;

    @Override
    public LangTuoResult<AdminRoleDTO> get(String tenantCode, String roleCode) {
        return null;
    }

    @Override
    public LangTuoResult<PageDTO<AdminRoleDTO>> search(String tenantCode, String roleName, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public LangTuoResult<PageDTO<AdminRoleDTO>> list(int pageNum, int pageSize) {
        return null;
    }

    @Override
    public LangTuoResult<Void> put(AdminRolePutRequest adminRolePutRequest) {
        return null;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String roleCode) {
        return null;
    }

    private AdminRoleDTO convert(AdminRolePO po) {
        if (po == null) {
            return null;
        }

        AdminRoleDTO dto = new AdminRoleDTO();
        dto.setId(po.getId());
        dto.setRoleCode(po.getRoleCode());
        dto.setRoleName(po.getRoleName());
        dto.setComment(po.getComment());
        dto.setTenantCode(po.getTenantCode());
        dto.setExtraInfo(po.getExtraInfo());

        return dto;
    }
}
