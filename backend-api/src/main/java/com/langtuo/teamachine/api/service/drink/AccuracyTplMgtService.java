package com.langtuo.teamachine.api.service.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.AccuracyTplDTO;
import com.langtuo.teamachine.api.request.drink.AccuracyTplPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface AccuracyTplMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<AccuracyTplDTO> getByCode(String tenantCode, String templateCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<AccuracyTplDTO> getByName(String tenantCode, String templateName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<AccuracyTplDTO>> search(String tenantCode, String templateCode, String templateName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<AccuracyTplDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(AccuracyTplPutRequest request);

    /**
     *
     * @param tenantCode
     * @param templateCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String templateCode);
}
