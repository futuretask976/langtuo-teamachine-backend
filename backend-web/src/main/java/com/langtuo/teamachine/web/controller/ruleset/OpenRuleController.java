package com.langtuo.teamachine.web.controller.ruleset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.OpenRuleDTO;
import com.langtuo.teamachine.api.request.rule.OpenRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.rule.OpenRuleMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ruleset/open")
public class OpenRuleController {
    @Resource
    private OpenRuleMgtService service;

    /**
     * url: http://localhost:8080/teamachine/ruleset/flushair/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{openrulecode}/get")
    public LangTuoResult<OpenRuleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "openrulecode") String openRuleCode) {
        LangTuoResult<OpenRuleDTO> rtn = service.getByCode(tenantCode, openRuleCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<OpenRuleDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<OpenRuleDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<OpenRuleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("openRuleCode") String openRuleCode, @RequestParam("openRuleName") String openRuleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<OpenRuleDTO>> rtn = service.search(tenantCode, openRuleCode, openRuleName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody OpenRulePutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/{tenantcode}/{cleanrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{openrulecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "openrulecode") String openRuleCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, openRuleCode);
        return rtn;
    }
}
