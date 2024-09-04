package com.langtuo.teamachine.web.controller.user;

import com.langtuo.teamachine.api.model.user.OrgDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.OrgPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.OrgMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/userset/org")
public class OrgController {
    @Resource
    private OrgMgtService service;
    
    @GetMapping(value = "/get")
    public TeaMachineResult<OrgDTO> get(@RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "orgName") String orgName) {
        TeaMachineResult<OrgDTO> rtn = service.get(tenantCode, orgName);
        return rtn;
    }
    
    @GetMapping(value = "/listbydepth")
    public TeaMachineResult<OrgDTO> listByDepth(@RequestParam(name = "tenantCode") String tenantCode) {
        TeaMachineResult<OrgDTO> rtn = service.getTop(tenantCode);
        return rtn;
    }
    
    @GetMapping(value = "/list")
    public TeaMachineResult<List<OrgDTO>> list(@RequestParam(name = "tenantCode") String tenantCode) {
        TeaMachineResult<List<OrgDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<OrgDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("orgName") String orgName, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<OrgDTO>> rtn = service.search(tenantCode, orgName, pageNum, pageSize);
        return rtn;
    }
    
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody OrgPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    @DeleteMapping(value = "/delete")
    public TeaMachineResult<Void> delete(@RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "orgName") String orgName) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, orgName);
        return rtn;
    }
}
