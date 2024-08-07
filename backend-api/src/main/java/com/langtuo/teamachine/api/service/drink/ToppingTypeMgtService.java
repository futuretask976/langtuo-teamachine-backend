package com.langtuo.teamachine.api.service.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingTypeDTO;
import com.langtuo.teamachine.api.request.drink.ToppingTypePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface ToppingTypeMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ToppingTypeDTO> getByCode(String tenantCode, String toppingTypeCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ToppingTypeDTO> getByName(String tenantCode, String toppingTypeName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<ToppingTypeDTO>> search(String tenantCode, String toppingTypeCode, String toppingTypeName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<ToppingTypeDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(ToppingTypePutRequest request);

    /**
     *
     * @param tenantCode
     * @param toppingTypeCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String toppingTypeCode);
}
