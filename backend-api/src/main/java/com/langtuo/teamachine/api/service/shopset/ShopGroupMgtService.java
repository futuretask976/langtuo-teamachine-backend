package com.langtuo.teamachine.api.service.shopset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shopset.ShopGroupDTO;
import com.langtuo.teamachine.api.request.shopset.ShopGroupPutRequest;
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
