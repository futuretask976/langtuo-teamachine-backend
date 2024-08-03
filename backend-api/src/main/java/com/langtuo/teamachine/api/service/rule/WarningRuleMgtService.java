package com.langtuo.teamachine.api.service.rule;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDTO;
import com.langtuo.teamachine.api.request.rule.WarningRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface WarningRuleMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<WarningRuleDTO> getByCode(String tenantCode, String warningRuleCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<WarningRuleDTO> getByName(String tenantCode, String warningRuleName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<WarningRuleDTO>> search(String tenantCode, String warningRuleCode, String warningRuleName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<WarningRuleDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(WarningRulePutRequest request);

    /**
     *
     * @param tenantCode
     * @param warningRuleCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String warningRuleCode);
}
