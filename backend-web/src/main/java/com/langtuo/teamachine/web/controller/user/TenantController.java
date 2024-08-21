package com.langtuo.teamachine.web.controller.user;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.user.TenantDTO;
import com.langtuo.teamachine.api.request.user.TenantPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.TenantMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/userset/tenant")
public class TenantController {
    @Resource
    private TenantMgtService service;

    /**
     * url: http://localhost:8080/teamachine/tenant/{tenantcode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/get")
    public TeaMachineResult<TenantDTO> get(@PathVariable(name = "tenantcode") String tenantCode) {
        TeaMachineResult<TenantDTO> rtn = service.get(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/userset/tenant/list
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<TenantDTO>> list() {
        TeaMachineResult<List<TenantDTO>> rtn = service.list();
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/tenant/search?tenantName=&contactPerson=&pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<TenantDTO>> search(@RequestParam("tenantName") String tenantName,
            @RequestParam("contactPerson") String contactPerson, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<TenantDTO>> rtn = service.search(tenantName, contactPerson, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/tenant/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody TenantPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/tenant/{tenantcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode);
        return rtn;
    }
}
