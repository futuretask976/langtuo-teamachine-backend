package com.langtuo.teamachine.web.controller;

import com.langtuo.teamachine.api.model.MachineDeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.MachineDeployPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.MachineDeployMgtService;
import com.langtuo.teamachine.biz.service.util.DeployUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/machine/deploy")
public class MachineDeployController {
    @Resource
    private MachineDeployMgtService service;

    /**
     * url: http://localhost:8080/teamachine/machine/deploy/tenant_001/123456/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{deploycode}/get")
    public LangTuoResult<MachineDeployDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "deploycode") String deployCode) {
        LangTuoResult<MachineDeployDTO> rtn = service.get(tenantCode, deployCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/deploy/list?tenantCode=tenant_001&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<MachineDeployDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<MachineDeployDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/deploy/search?tenantCode=tenant_001&deployCode=&shopName=&state=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<MachineDeployDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("deployCode") String deployCode, @RequestParam("machineCode") String machineCode,
            @RequestParam("shopName") String shopName, @RequestParam(required = false, name = "state") Integer state,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<MachineDeployDTO>> rtn = service.search(tenantCode, deployCode, machineCode, shopName, state,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/deploy/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody MachineDeployPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/deploy/tenant_001/123456/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{deploycode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "deploycode") String deployCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, deployCode);
        return rtn;
    }

    @GetMapping(value = "/generate")
    public LangTuoResult<String> generateDeployCode(@RequestParam(name = "tenantCode") String tenantCode) {
        LangTuoResult<String> rtn = LangTuoResult.success(DeployUtils.genRandomStr(20));
        return rtn;
    }
}
