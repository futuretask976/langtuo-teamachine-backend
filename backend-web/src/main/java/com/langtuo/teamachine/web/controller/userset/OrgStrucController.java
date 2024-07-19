package com.langtuo.teamachine.web.controller.userset;

import com.langtuo.teamachine.api.model.userset.OrgStrucDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.userset.OrgStrucPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.userset.OrgStrucMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/userset/orgstruc")
public class OrgStrucController {
    @Resource
    private OrgStrucMgtService service;

    /**
     * url: http://localhost:8080/teamachine/orgstruc/tenant_001/{orgname}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{orgname}/get")
    public LangTuoResult<OrgStrucDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "orgname") String orgName) {
        LangTuoResult<OrgStrucDTO> rtn = service.get(tenantCode, orgName);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/orgstruc/listbydepth?tenantCode=tenant_001
     * @return
     */
    @GetMapping(value = "/listbydepth")
    public LangTuoResult<OrgStrucDTO> listByDepth(@RequestParam(name = "tenantCode") String tenantCode) {
        LangTuoResult<OrgStrucDTO> rtn = service.listByDepth(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/orgstruc/list?tenantCode=tenant_001&pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<OrgStrucDTO>> list(@RequestParam(name = "tenantCode") String tenantCode) {
        LangTuoResult<List<OrgStrucDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/orgstruc/search?tenantCode=tenant_001&orgName=江苏分公司&pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<OrgStrucDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("orgName") String orgName, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<OrgStrucDTO>> rtn = service.search(tenantCode, orgName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/tenant/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody OrgStrucPutRequest request) {
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
