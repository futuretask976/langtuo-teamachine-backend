package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.TenantDTO;
import com.langtuo.teamachine.api.request.TenantPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

public interface TenantMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<TenantDTO> get(String tenantCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<TenantDTO>> search(String tenantCode, String contactPerson, int pageNum, int pageSize);

    /**
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    LangTuoResult<PageDTO<TenantDTO>> list(int pageNum, int pageSize);

    /**
     *
     * @param tenantPutRequest
     * @return
     */
    LangTuoResult<Void> put(TenantPutRequest tenantPutRequest);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode);
}
