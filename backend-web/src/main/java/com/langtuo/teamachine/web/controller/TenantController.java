package com.langtuo.teamachine.web.controller;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.TenantDTO;
import com.langtuo.teamachine.api.request.TenantPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.TenantMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/tenant")
public class TenantController {
    @Resource
    private TenantMgtService service;

    /**
     * url: http://localhost:8080/teamachine/tenant/{tenantcode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/get")
    public LangTuoResult<TenantDTO> get(@PathVariable(name = "tenantcode") String tenantCode) {
        LangTuoResult<TenantDTO> rtn = service.get(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/tenant/list?pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<TenantDTO>> list() {
        LangTuoResult<List<TenantDTO>> rtn = service.list();
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/tenant/search?tenantName=&contactPerson=&pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<TenantDTO>> search(@RequestParam("tenantName") String tenantName,
            @RequestParam("contactPerson") String contactPerson, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<TenantDTO>> rtn = service.search(tenantName, contactPerson, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/tenant/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody TenantPutRequest tenantPutRequest) {
        LangTuoResult<Void> rtn = service.put(tenantPutRequest);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/tenant/{tenantcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode);
        return rtn;
    }
}
