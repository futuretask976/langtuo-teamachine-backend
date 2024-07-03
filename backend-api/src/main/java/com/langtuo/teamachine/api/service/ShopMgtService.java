package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.ShopDTO;
import com.langtuo.teamachine.api.request.ShopPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface ShopMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ShopDTO> get(String tenantCode, String shopCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<ShopDTO>> search(String tenantCode, String shopName, String shopGroupName, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<ShopDTO>> list(String tenantCode);

    /**
     *
     * @param shopPutRequest
     * @return
     */
    LangTuoResult<Void> put(ShopPutRequest request);

    /**
     *
     * @param tenantCode
     * @param shopCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String shopCode);
}
