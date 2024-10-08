package com.langtuo.teamachine.web.controller.rule;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDispatchDTO;
import com.langtuo.teamachine.api.request.rule.DrainRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.DrainRulePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.rule.DrainRuleMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jiaqing
 */
@RestController
@RequestMapping("/ruleset/drain")
public class DrainRuleController {
    @Resource
    private DrainRuleMgtService service;
    
    @GetMapping(value = "/get")
    public TeaMachineResult<DrainRuleDTO> get(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("drainRuleCode") String drainRuleCode) {
        TeaMachineResult<DrainRuleDTO> rtn = service.getByDrainRuleCode(tenantCode, drainRuleCode);
        return rtn;
    }
    
    @GetMapping(value = "/listbyshop")
    public TeaMachineResult<List<DrainRuleDTO>> listByShop(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopCode") String shopCode) {
        TeaMachineResult<List<DrainRuleDTO>> rtn = service.listByShopCode(tenantCode, shopCode);
        return rtn;
    }
    
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<DrainRuleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam(name = "drainRuleCode", required = false) String drainRuleCode,
            @RequestParam(name = "drainRuleName", required = false) String drainRuleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<DrainRuleDTO>> rtn = service.search(tenantCode, drainRuleCode, drainRuleName,
                pageNum, pageSize);
        return rtn;
    }
    
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody DrainRulePutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }
    
    @DeleteMapping(value = "/delete")
    public TeaMachineResult<Void> delete(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("drainRuleCode") String drainRuleCode) {
        TeaMachineResult<Void> rtn = service.deleteByDrainRuleCode(tenantCode, drainRuleCode);
        return rtn;
    }

    @PutMapping(value = "/dispatch/put")
    public TeaMachineResult<Void> putDispatch(@RequestBody DrainRuleDispatchPutRequest request) {
        TeaMachineResult<Void> rtn = service.putDispatch(request);
        return rtn;
    }
    
    @GetMapping(value = "/dispatch/get")
    public TeaMachineResult<DrainRuleDispatchDTO> getDispatchByMenuCode(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("drainRuleCode") String drainRuleCode) {
        TeaMachineResult<DrainRuleDispatchDTO> rtn = service.getDispatchByDrainRuleCode(tenantCode, drainRuleCode);
        return rtn;
    }
}
