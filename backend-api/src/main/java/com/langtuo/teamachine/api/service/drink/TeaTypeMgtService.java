package com.langtuo.teamachine.api.service.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.TeaTypeDTO;
import com.langtuo.teamachine.api.request.drink.TeaTypePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface TeaTypeMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<TeaTypeDTO> getByCode(String tenantCode, String teaTypeCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<TeaTypeDTO> getByName(String tenantCode, String teaTypeName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<TeaTypeDTO>> search(String tenantCode, String teaTypeCode, String teaTypeName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<TeaTypeDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(TeaTypePutRequest request);

    /**
     *
     * @param tenantCode
     * @param teaTypeCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String teaTypeCode);
}
