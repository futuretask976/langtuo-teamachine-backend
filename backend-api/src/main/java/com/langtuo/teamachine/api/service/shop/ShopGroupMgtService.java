package com.langtuo.teamachine.api.service.shop;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.request.shop.ShopGroupPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

import java.util.List;

public interface ShopGroupMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<ShopGroupDTO> getByCode(String tenantCode, String shopGroupCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<ShopGroupDTO> getByName(String tenantCode, String shopGroupName);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<ShopGroupDTO>> search(String tenantCode, String shopGroupName, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<ShopGroupDTO>> list(String tenantCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<ShopGroupDTO>> listByAdminOrg(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(ShopGroupPutRequest request);

    /**
     *
     * @param tenantCode
     * @param shopGroupCode
     * @return
     */
    TeaMachineResult<Void> delete(String tenantCode, String shopGroupCode);
}
