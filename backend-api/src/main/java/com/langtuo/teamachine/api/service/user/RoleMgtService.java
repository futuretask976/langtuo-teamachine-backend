package com.langtuo.teamachine.api.service.user;

import com.langtuo.teamachine.api.model.user.RoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.RolePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface RoleMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<RoleDTO> get(String tenantCode, String roleCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<RoleDTO>> search(String tenantCode, String roleName, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<RoleDTO>> list(String tenantCode);

    /**
     *
     * @param tenantCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    LangTuoResult<PageDTO<RoleDTO>> page(String tenantCode, int pageNum, int pageSize);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(RolePutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String roleCode);
}
