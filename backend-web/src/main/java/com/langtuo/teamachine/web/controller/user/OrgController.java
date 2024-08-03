package com.langtuo.teamachine.web.controller.user;

import com.langtuo.teamachine.api.model.user.OrgDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.OrgPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.user.OrgMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/userset/org")
public class OrgController {
    @Resource
    private OrgMgtService service;

    /**
     * url: http://localhost:8080/teamachine/org/tenant_001/{orgname}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{orgname}/get")
    public LangTuoResult<OrgDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "orgname") String orgName) {
        LangTuoResult<OrgDTO> rtn = service.get(tenantCode, orgName);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/org/listbydepth?tenantCode=tenant_001
     * @return
     */
    @GetMapping(value = "/listbydepth")
    public LangTuoResult<OrgDTO> listByDepth(@RequestParam(name = "tenantCode") String tenantCode) {
        LangTuoResult<OrgDTO> rtn = service.listByDepth(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/org/list?tenantCode=tenant_001&pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<OrgDTO>> list(@RequestParam(name = "tenantCode") String tenantCode) {
        LangTuoResult<List<OrgDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/org/search?tenantCode=tenant_001&orgName=江苏分公司&pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<OrgDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("orgName") String orgName, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<OrgDTO>> rtn = service.search(tenantCode, orgName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/tenant/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody OrgPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/tenant/{tenantcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{orgname}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "orgname") String orgName) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, orgName);
        return rtn;
    }
}
