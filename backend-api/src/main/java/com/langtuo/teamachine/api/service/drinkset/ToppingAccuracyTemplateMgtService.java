package com.langtuo.teamachine.api.service.drinkset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drinkset.ToppingAccuracyTemplateDTO;
import com.langtuo.teamachine.api.request.drinkset.ToppingAccuracyTemplatePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface ToppingAccuracyTemplateMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ToppingAccuracyTemplateDTO> getByCode(String tenantCode, String templateCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<ToppingAccuracyTemplateDTO> getByName(String tenantCode, String templateName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<ToppingAccuracyTemplateDTO>> search(String tenantCode, String templateCode, String templateName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<ToppingAccuracyTemplateDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(ToppingAccuracyTemplatePutRequest request);

    /**
     *
     * @param tenantCode
     * @param templateCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String templateCode);
}
