package com.langtuo.teamachine.api.service.menu;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menu.MenuDTO;
import com.langtuo.teamachine.api.model.menu.MenuDispatchDTO;
import com.langtuo.teamachine.api.request.menu.MenuDispatchPutRequest;
import com.langtuo.teamachine.api.request.menu.MenuPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

import java.util.List;

public interface MenuMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<MenuDTO> getByCode(String tenantCode, String menuCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<MenuDTO> getByName(String tenantCode, String menuName);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<MenuDTO>> search(String tenantCode, String menuCode, String menuName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<MenuDTO>> list(String tenantCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<MenuDTO>> listByShopCode(String tenantCode, String shopCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<Void> triggerDispatchByShopCode(String tenantCode, String shopCode, String machineCode);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(MenuPutRequest request);

    /**
     *
     * @param tenantCode
     * @param menuCode
     * @return
     */
    TeaMachineResult<Void> delete(String tenantCode, String menuCode);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> putDispatch(MenuDispatchPutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<MenuDispatchDTO> getDispatchByMenuCode(String tenantCode, String menuCode);
}
