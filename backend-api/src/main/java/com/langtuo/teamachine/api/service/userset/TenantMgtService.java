package com.langtuo.teamachine.api.service.userset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.userset.TenantDTO;
import com.langtuo.teamachine.api.request.userset.TenantPutRequest;
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
