package com.langtuo.teamachine.web.controller.rule;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDTO;
import com.langtuo.teamachine.api.model.rule.DrainRuleDispatchDTO;
import com.langtuo.teamachine.api.request.rule.DrainRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.DrainRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
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
     * url: http://localhost:8080/teamachine/ruleset/drain/{tenantcode}/{drainrulecode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{drainrulecode}/get")
    public LangTuoResult<DrainRuleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "drainrulecode") String drainRuleCode) {
        LangTuoResult<DrainRuleDTO> rtn = service.getByCode(tenantCode, drainRuleCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/drain/list?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<DrainRuleDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<DrainRuleDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/drain/search?tenantCode={tenantCode}&drainRuleCode={drainRuleCode}&drainRuleName={drainRuleName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<DrainRuleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("drainRuleCode") String drainRuleCode, @RequestParam("drainRuleName") String drainRuleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<DrainRuleDTO>> rtn = service.search(tenantCode, drainRuleCode, drainRuleName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/drain/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody DrainRulePutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/drain/{tenantcode}/{drainrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{drainrulecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "drainrulecode") String drainRuleCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, drainRuleCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/drain/dispatch/put
     * @return
     */
    @PutMapping(value = "/dispatch/put")
    public LangTuoResult<Void> putDispatch(@RequestBody DrainRuleDispatchPutRequest request) {
        LangTuoResult<Void> rtn = service.putDispatch(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/drain/dispatch/{tenantcode}/{drainrulecode}/get
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/dispatch/{tenantcode}/{drainrulecode}/get")
    public LangTuoResult<DrainRuleDispatchDTO> getDispatchByMenuCode(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "drainrulecode") String drainRuleCode) {
        LangTuoResult<DrainRuleDispatchDTO> rtn = service.getDispatchByCode(tenantCode, drainRuleCode);
        return rtn;
    }
}
