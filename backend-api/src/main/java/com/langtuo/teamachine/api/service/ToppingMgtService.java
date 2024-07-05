package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.ToppingDTO;
import com.langtuo.teamachine.api.request.ToppingPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface ToppingMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ToppingDTO> getByCode(String tenantCode, String toppingCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ToppingDTO> getByName(String tenantCode, String toppingName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<ToppingDTO>> search(String tenantCode, String toppingCode, String toppingName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<ToppingDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(ToppingPutRequest request);

    /**
     *
     * @param tenantCode
     * @param toppingCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String toppingCode);
}
