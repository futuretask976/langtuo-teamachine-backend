package com.langtuo.teamachine.web.controller.rule;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDTO;
import com.langtuo.teamachine.api.model.rule.CleanRuleDispatchDTO;
import com.langtuo.teamachine.api.request.rule.CleanRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.CleanRulePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.rule.CleanRuleMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ruleset/clean")
public class CleanRuleController {
    @Resource
    private CleanRuleMgtService service;

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/clean/{tenantcode}/{cleanrulecode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{cleanrulecode}/get")
    public TeaMachineResult<CleanRuleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "cleanrulecode") String cleanRuleCode) {
        TeaMachineResult<CleanRuleDTO> rtn = service.getByCode(tenantCode, cleanRuleCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/clean/list?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<CleanRuleDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<CleanRuleDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/clean/listbyshop?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/listbyshop")
    public TeaMachineResult<List<CleanRuleDTO>> list(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopCode") String shopCode) {
        TeaMachineResult<List<CleanRuleDTO>> rtn = service.listByShopCode(tenantCode, shopCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/clean/search?tenantCode={tenantCode}&cleanRuleCode={cleanRuleCode}&cleanRuleName={cleanRuleName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<CleanRuleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("cleanRuleCode") String cleanRuleCode, @RequestParam("cleanRuleName") String cleanRuleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<CleanRuleDTO>> rtn = service.search(tenantCode, cleanRuleCode, cleanRuleName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/clean/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody CleanRulePutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/clean/{tenantcode}/{cleanrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{cleanrulecode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "cleanrulecode") String cleanRuleCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, cleanRuleCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/clean/put
     * @return
     */
    @PutMapping(value = "/dispatch/put")
    public TeaMachineResult<Void> putDispatch(@RequestBody CleanRuleDispatchPutRequest request) {
        TeaMachineResult<Void> rtn = service.putDispatch(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/clean/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/dispatch/{tenantcode}/{cleanrulecode}/get")
    public TeaMachineResult<CleanRuleDispatchDTO> getDispatchByMenuCode(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "cleanrulecode") String cleanRuleCode) {
        TeaMachineResult<CleanRuleDispatchDTO> rtn = service.getDispatchByCleanRuleCode(tenantCode, cleanRuleCode);
        return rtn;
    }
}
