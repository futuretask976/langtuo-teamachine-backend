package com.langtuo.teamachine.api.service.rule;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.CloseRuleDTO;
import com.langtuo.teamachine.api.request.rule.CloseRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface CloseRuleMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<CloseRuleDTO> getByCode(String tenantCode, String closeRuleCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<CloseRuleDTO> getByName(String tenantCode, String closeRuleName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<CloseRuleDTO>> search(String tenantCode, String closeRuleCode, String closeRuleName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<CloseRuleDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(CloseRulePutRequest request);

    /**
     *
     * @param tenantCode
     * @param closeRuleCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String closeRuleCode);
}
