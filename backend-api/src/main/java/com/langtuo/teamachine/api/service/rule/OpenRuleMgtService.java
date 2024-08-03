package com.langtuo.teamachine.api.service.rule;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.OpenRuleDTO;
import com.langtuo.teamachine.api.request.rule.OpenRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface OpenRuleMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<OpenRuleDTO> getByCode(String tenantCode, String openRuleCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<OpenRuleDTO> getByName(String tenantCode, String openRuleName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<OpenRuleDTO>> search(String tenantCode, String openRuleCode, String openRuleName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<OpenRuleDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(OpenRulePutRequest request);

    /**
     *
     * @param tenantCode
     * @param openRuleCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String openRuleCode);
}
