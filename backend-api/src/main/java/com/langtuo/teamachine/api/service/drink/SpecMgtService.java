package com.langtuo.teamachine.api.service.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.SpecDTO;
import com.langtuo.teamachine.api.request.drink.SpecPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface SpecMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<SpecDTO> getByCode(String tenantCode, String specCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<SpecDTO> getByName(String tenantCode, String specName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<SpecDTO>> search(String tenantCode, String specCode, String specName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<SpecDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(SpecPutRequest request);

    /**
     *
     * @param tenantCode
     * @param specCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String specCode);
}
