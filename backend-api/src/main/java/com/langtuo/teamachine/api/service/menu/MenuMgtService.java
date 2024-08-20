package com.langtuo.teamachine.api.service.menu;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menu.MenuDTO;
import com.langtuo.teamachine.api.model.menu.MenuDispatchDTO;
import com.langtuo.teamachine.api.request.menu.MenuDispatchPutRequest;
import com.langtuo.teamachine.api.request.menu.MenuPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface MenuMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<MenuDTO> getByCode(String tenantCode, String menuCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<MenuDTO> getByName(String tenantCode, String menuName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<MenuDTO>> search(String tenantCode, String menuCode, String menuName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<MenuDTO>> list(String tenantCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<MenuDTO>> listByShopCode(String tenantCode, String shopCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<Void> triggerDispatchByShopCode(String tenantCode, String shopCode, String machineCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(MenuPutRequest request);

    /**
     *
     * @param tenantCode
     * @param menuCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String menuCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> putDispatch(MenuDispatchPutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<MenuDispatchDTO> getDispatchByCode(String tenantCode, String menuCode);
}
