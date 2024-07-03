package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.AdminDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.AdminPutRequest;
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
}
