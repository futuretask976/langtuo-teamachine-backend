package com.langtuo.teamachine.api.service.shop;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.request.shop.ShopGroupPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface ShopGroupMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ShopGroupDTO> getByCode(String tenantCode, String shopGroupCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ShopGroupDTO> getByName(String tenantCode, String shopGroupName);

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
     * @param request
     * @return
     */
    LangTuoResult<Void> put(ShopGroupPutRequest request);

    /**
     *
     * @param tenantCode
     * @param shopGroupCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String shopGroupCode);
}
