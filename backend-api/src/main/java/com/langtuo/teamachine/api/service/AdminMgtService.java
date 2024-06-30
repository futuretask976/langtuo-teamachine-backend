package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.AdminDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.AdminPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

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
    LangTuoResult<PageDTO<AdminDTO>> search(String tenantCode, String loginName, String roleName, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    LangTuoResult<PageDTO<AdminDTO>> list(String tenantCode, int pageNum, int pageSize);

    /**
     *
     * @param adminPutRequest
     * @return
     */
    LangTuoResult<Void> put(AdminPutRequest adminPutRequest);

    /**
     *
     * @param tenantCode
     * @param loginName
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String loginName);
}
