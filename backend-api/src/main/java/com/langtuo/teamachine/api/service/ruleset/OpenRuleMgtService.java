package com.langtuo.teamachine.api.service.ruleset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.ruleset.OpenRuleDTO;
import com.langtuo.teamachine.api.request.ruleset.OpenRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface OpenRuleMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<OpenRuleDTO> getByCode(String tenantCode, String flushAirRuleCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<OpenRuleDTO> getByName(String tenantCode, String cleanRuleName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<OpenRuleDTO>> search(String tenantCode, String flushAirRuleCode, String cleanRuleName,
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
     * @param flushAirRuleCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String flushAirRuleCode);
}
