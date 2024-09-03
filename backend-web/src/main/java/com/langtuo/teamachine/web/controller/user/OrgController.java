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

    /**
     * url: http://{host}:{port}/teamachinebackend/org/{tenantcode}/{orgname}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{orgname}/get")
    public TeaMachineResult<OrgDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "orgname") String orgName) {
        TeaMachineResult<OrgDTO> rtn = service.get(tenantCode, orgName);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/org/listbydepth?tenantCode={tenantCode}
     * @return
     */
    @GetMapping(value = "/listbydepth")
    public TeaMachineResult<OrgDTO> listByDepth(@RequestParam(name = "tenantCode") String tenantCode) {
        TeaMachineResult<OrgDTO> rtn = service.getTop(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/org/list?tenantCode={tenantCode}&pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<OrgDTO>> list(@RequestParam(name = "tenantCode") String tenantCode) {
        TeaMachineResult<List<OrgDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/org/search?tenantCode={tenantCode}&orgName=江苏分公司&pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<OrgDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("orgName") String orgName, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<OrgDTO>> rtn = service.search(tenantCode, orgName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/tenant/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody OrgPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/tenant/{tenantcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{orgname}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "orgname") String orgName) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, orgName);
        return rtn;
    }
}
