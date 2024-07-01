package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.ShopGroupDTO;
import com.langtuo.teamachine.api.request.ShopGroupPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface ShopGroupMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ShopGroupDTO> get(String tenantCode, String shopGroupCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<ShopGroupDTO>> search(String tenantCode, String shopGroupName, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<ShopGroupDTO>> list(String tenantCode);

    /**
     *
     * @param shopGroupPutRequest
     * @return
     */
    LangTuoResult<Void> put(ShopGroupPutRequest shopGroupPutRequest);

    /**
     *
     * @param tenantCode
     * @param shopGroupCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String shopGroupCode);
}
