package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.TeaDTO;
import com.langtuo.teamachine.api.request.TeaPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface TeaMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<TeaDTO> getByCode(String tenantCode, String teaCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<TeaDTO> getByName(String tenantCode, String teaName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<TeaDTO>> search(String tenantCode, String teaCode, String teaName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<TeaDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(TeaPutRequest request);

    /**
     *
     * @param tenantCode
     * @param teaCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String teaCode);
}
