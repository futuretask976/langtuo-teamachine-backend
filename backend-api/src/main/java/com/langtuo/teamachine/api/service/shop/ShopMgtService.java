package com.langtuo.teamachine.api.service.shop;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.request.shop.ShopPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

import java.util.List;

public interface ShopMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<ShopDTO> getByCode(String tenantCode, String shopCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<ShopDTO> getByName(String tenantCode, String shopName);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<ShopDTO>> search(String tenantCode, String shopName, String shopGroupCode, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<ShopDTO>> listByShopGroupCode(String tenantCode, String shopGroupCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<ShopDTO>> listByAdminOrg(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(ShopPutRequest request);

    /**
     *
     * @param tenantCode
     * @param shopCode
     * @return
     */
    TeaMachineResult<Void> delete(String tenantCode, String shopCode);

    /**
     *
     * @param tenantCode
     * @param shopGroupCode
     * @return
     */
    TeaMachineResult<Integer> countByShopGroupCode(String tenantCode, String shopGroupCode);
}
