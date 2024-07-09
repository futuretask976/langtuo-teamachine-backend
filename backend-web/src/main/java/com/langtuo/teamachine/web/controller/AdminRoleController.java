package com.langtuo.teamachine.web.controller;

import com.langtuo.teamachine.api.model.AdminRoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.AdminRolePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.AdminRoleMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/role")
public class AdminRoleController {
    @Resource
    private AdminRoleMgtService service;

    /**
     * url: http://localhost:8080/teamachine/admin/role/tenant_001/{rolecode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{rolecode}/get")
    public LangTuoResult<AdminRoleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "rolecode") String roleCode) {
        LangTuoResult<AdminRoleDTO> rtn = service.get(tenantCode, roleCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/role/list?tenantCode=tenant_001
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<AdminRoleDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<AdminRoleDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/role/list?tenantCode=tenant_001&roleName=系统超级管理员&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<AdminRoleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("roleName") String roleName, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<AdminRoleDTO>> rtn = service.search(tenantCode, roleName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/role/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody AdminRolePutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/role/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{rolecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "rolecode") String roleCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, roleCode);
        return rtn;
    }
}
