package com.langtuo.teamachine.api.service.user;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.user.TenantDTO;
import com.langtuo.teamachine.api.request.user.TenantPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

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
     * @return
     */
    LangTuoResult<List<TenantDTO>> list();

    /**
     *
     * @param tenantPutRequest
     * @return
     */
    LangTuoResult<Void> put(TenantPutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode);
}
