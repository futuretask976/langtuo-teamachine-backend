package com.langtuo.teamachine.web.controller.rule;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDTO;
import com.langtuo.teamachine.api.request.rule.WarningRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
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
    public LangTuoResult<WarningRuleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "warningrulecode") String warningRuleCode) {
        LangTuoResult<WarningRuleDTO> rtn = service.getByCode(tenantCode, warningRuleCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/warning/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<WarningRuleDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<WarningRuleDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/warning/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<WarningRuleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("warningRuleCode") String warningRuleCode, @RequestParam("warningRuleName") String warningRuleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<WarningRuleDTO>> rtn = service.search(tenantCode, warningRuleCode, warningRuleName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/warning/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody WarningRulePutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/warning/{tenantcode}/{cleanrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{warningrulecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "warningrulecode") String warningRuleCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, warningRuleCode);
        return rtn;
    }
}
