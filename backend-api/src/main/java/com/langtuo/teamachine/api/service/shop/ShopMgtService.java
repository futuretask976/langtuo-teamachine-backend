package com.langtuo.teamachine.api.service.shop;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.request.shop.ShopPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface ShopMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ShopDTO> getByCode(String tenantCode, String shopCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ShopDTO> getByName(String tenantCode, String shopName);

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
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<ShopDTO>> listByShopGroupCode(String tenantCode, String shopGroupCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<ShopDTO>> listByAdmin(String tenantCode);

    /**
     *
     * @param request
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
