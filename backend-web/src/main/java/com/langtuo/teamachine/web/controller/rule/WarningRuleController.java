package com.langtuo.teamachine.web.controller.rule;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDispatchDTO;
import com.langtuo.teamachine.api.request.rule.WarningRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.WarningRulePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.rule.WarningRuleMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ruleset/warning")
public class WarningRuleController {
    @Resource
    private WarningRuleMgtService service;

    /**
     * url: http://localhost:8080/teamachine/ruleset/warning/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{warningrulecode}/get")
    public TeaMachineResult<WarningRuleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "warningrulecode") String warningRuleCode) {
        TeaMachineResult<WarningRuleDTO> rtn = service.getByCode(tenantCode, warningRuleCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/warning/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<WarningRuleDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<WarningRuleDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/warning/listbyshop?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/listbyshop")
    public TeaMachineResult<List<WarningRuleDTO>> list(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopCode") String shopCode) {
        TeaMachineResult<List<WarningRuleDTO>> rtn = service.listByShopCode(tenantCode, shopCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/warning/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<WarningRuleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("warningRuleCode") String warningRuleCode, @RequestParam("warningRuleName") String warningRuleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<WarningRuleDTO>> rtn = service.search(tenantCode, warningRuleCode, warningRuleName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/warning/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody WarningRulePutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/warning/{tenantcode}/{cleanrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{warningrulecode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "warningrulecode") String warningRuleCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, warningRuleCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/put
     * @return
     */
    @PutMapping(value = "/dispatch/put")
    public TeaMachineResult<Void> putDispatch(@RequestBody WarningRuleDispatchPutRequest request) {
        TeaMachineResult<Void> rtn = service.putDispatch(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/dispatch/{tenantcode}/{warningrulecode}/get")
    public TeaMachineResult<WarningRuleDispatchDTO> getDispatchByMenuCode(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "warningrulecode") String warningRuleCode) {
        TeaMachineResult<WarningRuleDispatchDTO> rtn = service.getDispatchByWarningRuleCode(tenantCode, warningRuleCode);
        return rtn;
    }
}
