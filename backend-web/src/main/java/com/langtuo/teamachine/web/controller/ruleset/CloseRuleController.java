package com.langtuo.teamachine.web.controller.ruleset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.ruleset.CloseRuleDTO;
import com.langtuo.teamachine.api.request.ruleset.CloseRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.ruleset.CloseRuleMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ruleset/close")
public class CloseRuleController {
    @Resource
    private CloseRuleMgtService service;

    /**
     * url: http://localhost:8080/teamachine/ruleset/flushair/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{closerulecode}/get")
    public LangTuoResult<CloseRuleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "closerulecode") String closeRuleCode) {
        LangTuoResult<CloseRuleDTO> rtn = service.getByCode(tenantCode, closeRuleCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/close/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<CloseRuleDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<CloseRuleDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/close/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<CloseRuleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("closeRuleCode") String closeRuleCode, @RequestParam("closeRuleName") String closeRuleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<CloseRuleDTO>> rtn = service.search(tenantCode, closeRuleCode, closeRuleName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/close/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody CloseRulePutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/close/{tenantcode}/{cleanrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{closerulecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "closerulecode") String closeRuleCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, closeRuleCode);
        return rtn;
    }
}
