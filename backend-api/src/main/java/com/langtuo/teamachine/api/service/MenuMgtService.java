package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.MenuDTO;
import com.langtuo.teamachine.api.request.MenuPutRequest;
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
}
