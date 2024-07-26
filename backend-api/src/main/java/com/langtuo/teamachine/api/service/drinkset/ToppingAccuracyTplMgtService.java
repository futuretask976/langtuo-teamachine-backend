package com.langtuo.teamachine.api.service.drinkset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drinkset.ToppingAccuracyTplDTO;
import com.langtuo.teamachine.api.request.drinkset.ToppingAccuracyTplPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface ToppingAccuracyTplMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ToppingAccuracyTplDTO> getByCode(String tenantCode, String templateCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ToppingAccuracyTplDTO> getByName(String tenantCode, String templateName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<ToppingAccuracyTplDTO>> search(String tenantCode, String templateCode, String templateName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<ToppingAccuracyTplDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(ToppingAccuracyTplPutRequest request);

    /**
     *
     * @param tenantCode
     * @param templateCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String templateCode);
}
