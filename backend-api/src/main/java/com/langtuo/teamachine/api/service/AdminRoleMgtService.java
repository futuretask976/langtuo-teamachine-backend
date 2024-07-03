package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.AdminRoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.AdminRolePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface AdminRoleMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<AdminRoleDTO> get(String tenantCode, String roleCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<AdminRoleDTO>> search(String tenantCode, String roleName, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<AdminRoleDTO>> list(String tenantCode);

    /**
     *
     * @param tenantCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    LangTuoResult<PageDTO<AdminRoleDTO>> page(String tenantCode, int pageNum, int pageSize);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(AdminRolePutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String roleCode);
}
