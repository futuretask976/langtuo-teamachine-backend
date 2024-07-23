package com.langtuo.teamachine.web.controller.ruleset;

import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.ruleset.CleanRuleDTO;
import com.langtuo.teamachine.api.model.ruleset.CleanRuleDispatchDTO;
import com.langtuo.teamachine.api.request.ruleset.CleanRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.ruleset.CleanRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.ruleset.CleanRuleMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ruleset/clean")
public class CleanRuleController {
    @Resource
    private CleanRuleMgtService service;

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{cleanrulecode}/get")
    public LangTuoResult<CleanRuleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "cleanrulecode") String cleanRuleCode) {
        LangTuoResult<CleanRuleDTO> rtn = service.getByCode(tenantCode, cleanRuleCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<CleanRuleDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<CleanRuleDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<CleanRuleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("cleanRuleCode") String cleanRuleCode, @RequestParam("cleanRuleName") String cleanRuleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<CleanRuleDTO>> rtn = service.search(tenantCode, cleanRuleCode, cleanRuleName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody CleanRulePutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
//        return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/{tenantcode}/{cleanrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{cleanrulecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "cleanrulecode") String cleanRuleCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, cleanRuleCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/put
     * @return
     */
    @PutMapping(value = "/dispatch/put")
    public LangTuoResult<Void> putDispatch(@RequestBody CleanRuleDispatchPutRequest request) {
        LangTuoResult<Void> rtn = service.putDispatch(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/ruleset/clean/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/dispatch/{tenantcode}/{cleanrulecode}/get")
    public LangTuoResult<CleanRuleDispatchDTO> getDispatchByMenuCode(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "cleanrulecode") String cleanRuleCode) {
        LangTuoResult<CleanRuleDispatchDTO> rtn = service.getDispatchByCleanRuleCode(tenantCode, cleanRuleCode);
        return rtn;
    }
}
