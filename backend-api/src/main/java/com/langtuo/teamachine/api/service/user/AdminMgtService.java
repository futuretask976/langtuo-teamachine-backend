package com.langtuo.teamachine.api.service.user;

import com.langtuo.teamachine.api.model.user.AdminDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.AdminPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface AdminMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<AdminDTO> get(String tenantCode, String loginName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<AdminDTO>> search(String tenantCode, String loginName, String roleName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<AdminDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(AdminPutRequest request);

    /**
     *
     * @param tenantCode
     * @param loginName
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String loginName);

    /**
     *
     * @param tenantCode
     * @param roleCode
     * @return
     */
    LangTuoResult<Integer> countByRoleCode(String tenantCode, String roleCode);
}
