package com.langtuo.teamachine.api.service.user;

import com.langtuo.teamachine.api.model.user.RoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.RolePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

import java.util.List;

public interface RoleMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<RoleDTO> getByCode(String tenantCode, String roleCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<RoleDTO> getByName(String tenantCode, String roleName);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<RoleDTO>> search(String tenantCode, String roleName, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<RoleDTO>> list(String tenantCode);

    /**
     *
     * @param tenantCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    TeaMachineResult<PageDTO<RoleDTO>> page(String tenantCode, int pageNum, int pageSize);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(RolePutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<Void> delete(String tenantCode, String roleCode);
}
