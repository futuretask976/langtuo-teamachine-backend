package com.langtuo.teamachine.web.controller.userset;

import com.langtuo.teamachine.api.model.userset.AdminDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.userset.AdminPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.userset.AdminMgtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/userset/admin")
@Slf4j
public class AdminController {
    @Resource
    private AdminMgtService service;

    /**
     * url: http://localhost:8080/teamachine/admin/tenant_001/jiaqing001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{loginname}/get")
    public LangTuoResult<AdminDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "loginname") String loginName) {
        LangTuoResult<AdminDTO> rtn = service.get(tenantCode, loginName);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/list?tenantCode=tenant_001&roleName=系统超级管理员&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<AdminDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("loginName") String loginName, @RequestParam("roleName") String roleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<AdminDTO>> rtn = service.search(tenantCode, loginName, roleName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/role/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody AdminPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/role/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{loginname}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "loginname") String loginName) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, loginName);
        return rtn;
    }
}
