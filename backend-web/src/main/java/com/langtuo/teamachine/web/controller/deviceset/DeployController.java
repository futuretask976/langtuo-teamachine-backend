package com.langtuo.teamachine.web.controller.deviceset;

import com.langtuo.teamachine.api.model.deviceset.DeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.deviceset.DeployPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.deviceset.DeployMgtService;
import com.langtuo.teamachine.biz.service.util.DeployUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/deviceset/deploy")
public class DeployController {
    @Resource
    private DeployMgtService service;

    /**
     * url: http://localhost:8080/teamachine/deploy/tenant_001/123456/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{deploycode}/get")
    public LangTuoResult<DeployDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "deploycode") String deployCode) {
        LangTuoResult<DeployDTO> rtn = service.get(tenantCode, deployCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/deploy/list?tenantCode=tenant_001&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<DeployDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<DeployDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/deploy/search?tenantCode=tenant_001&deployCode=&shopName=&state=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<DeployDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("deployCode") String deployCode, @RequestParam("machineCode") String machineCode,
            @RequestParam("shopName") String shopName, @RequestParam(required = false, name = "state") Integer state,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<DeployDTO>> rtn = service.search(tenantCode, deployCode, machineCode, shopName, state,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/deploy/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody DeployPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/deploy/tenant_001/123456/delete
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
