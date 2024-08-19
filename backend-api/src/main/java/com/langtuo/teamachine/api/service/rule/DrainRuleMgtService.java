package com.langtuo.teamachine.api.service.rule;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDispatchDTO;
import com.langtuo.teamachine.api.request.rule.DrainRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.DrainRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface DrainRuleMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<DrainRuleDTO> getByCode(String tenantCode, String drainRuleCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<DrainRuleDTO> getByName(String tenantCode, String drainRuleName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<DrainRuleDTO>> search(String tenantCode, String drainRuleCode, String drainRuleName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<DrainRuleDTO>> list(String tenantCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<DrainRuleDTO>> listByShopCode(String tenantCode, String shopCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(DrainRulePutRequest request);

    /**
     *
     * @param tenantCode
     * @param drainRuleCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String drainRuleCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> putDispatch(DrainRuleDispatchPutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<DrainRuleDispatchDTO> getDispatchByCode(String tenantCode, String drainRuleCode);
}
