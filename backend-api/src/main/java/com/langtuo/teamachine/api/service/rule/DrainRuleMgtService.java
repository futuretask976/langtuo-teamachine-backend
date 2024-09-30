package com.langtuo.teamachine.api.service.rule;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDispatchDTO;
import com.langtuo.teamachine.api.request.rule.DrainRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.DrainRulePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

import java.util.List;

public interface DrainRuleMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<DrainRuleDTO> getByDrainRuleCode(String tenantCode, String drainRuleCode);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<DrainRuleDTO>> search(String tenantCode, String drainRuleCode, String drainRuleName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<DrainRuleDTO>> list(String tenantCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<DrainRuleDTO>> listByShopCode(String tenantCode, String shopCode);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(DrainRulePutRequest request);

    /**
     *
     * @param tenantCode
     * @param drainRuleCode
     * @return
     */
    TeaMachineResult<Void> deleteByDrainRuleCode(String tenantCode, String drainRuleCode);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> putDispatch(DrainRuleDispatchPutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<DrainRuleDispatchDTO> getDispatchByDrainRuleCode(String tenantCode, String drainRuleCode);
}
