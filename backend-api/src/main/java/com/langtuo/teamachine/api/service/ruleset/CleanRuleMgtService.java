package com.langtuo.teamachine.api.service.ruleset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menuset.MenuDispatchDTO;
import com.langtuo.teamachine.api.model.ruleset.CleanRuleDTO;
import com.langtuo.teamachine.api.model.ruleset.CleanRuleDispatchDTO;
import com.langtuo.teamachine.api.request.menuset.MenuDispatchPutRequest;
import com.langtuo.teamachine.api.request.ruleset.CleanRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.ruleset.CleanRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface CleanRuleMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<CleanRuleDTO> getByCode(String tenantCode, String cleanRuleCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<CleanRuleDTO> getByName(String tenantCode, String cleanRuleName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<CleanRuleDTO>> search(String tenantCode, String cleanRuleCode, String cleanRuleName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<CleanRuleDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(CleanRulePutRequest request);

    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String cleanRuleCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> putDispatch(CleanRuleDispatchPutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<CleanRuleDispatchDTO> getDispatchByCleanRuleCode(String tenantCode, String cleanRuleCode);
}
