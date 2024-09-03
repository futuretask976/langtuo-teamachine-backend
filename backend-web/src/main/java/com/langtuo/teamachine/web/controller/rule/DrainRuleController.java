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

@RestController
@RequestMapping("/ruleset/drain")
public class DrainRuleController {
    @Resource
    private DrainRuleMgtService service;

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/drain/{tenantcode}/{drainrulecode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{drainrulecode}/get")
    public TeaMachineResult<DrainRuleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "drainrulecode") String drainRuleCode) {
        TeaMachineResult<DrainRuleDTO> rtn = service.getByCode(tenantCode, drainRuleCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/drain/list?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<DrainRuleDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<DrainRuleDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/drain/listbyshop?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/listbyshop")
    public TeaMachineResult<List<DrainRuleDTO>> list(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopCode") String shopCode) {
        TeaMachineResult<List<DrainRuleDTO>> rtn = service.listByShopCode(tenantCode, shopCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/drain/search?tenantCode={tenantCode}&drainRuleCode={drainRuleCode}&drainRuleName={drainRuleName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<DrainRuleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("drainRuleCode") String drainRuleCode, @RequestParam("drainRuleName") String drainRuleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<DrainRuleDTO>> rtn = service.search(tenantCode, drainRuleCode, drainRuleName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/drain/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody DrainRulePutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/drain/{tenantcode}/{drainrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{drainrulecode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "drainrulecode") String drainRuleCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, drainRuleCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/drain/dispatch/put
     * @return
     */
    @PutMapping(value = "/dispatch/put")
    public TeaMachineResult<Void> putDispatch(@RequestBody DrainRuleDispatchPutRequest request) {
        TeaMachineResult<Void> rtn = service.putDispatch(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/ruleset/drain/dispatch/{tenantcode}/{drainrulecode}/get
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/dispatch/{tenantcode}/{drainrulecode}/get")
    public TeaMachineResult<DrainRuleDispatchDTO> getDispatchByMenuCode(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "drainrulecode") String drainRuleCode) {
        TeaMachineResult<DrainRuleDispatchDTO> rtn = service.getDispatchByDrainRuleCode(tenantCode, drainRuleCode);
        return rtn;
    }
}
