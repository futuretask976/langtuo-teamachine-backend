package com.langtuo.teamachine.web.controller;

import com.langtuo.teamachine.api.model.AdminRoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.AdminRolePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.AdminRoleMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/role")
public class AdminRoleController {
    @Resource
    private AdminRoleMgtService adminRoleMgtService;

    /**
     * Access: http://localhost:8080/teamachine/admin/role/tenant_001/{rolecode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{rolecode}/get")
    public LangTuoResult<AdminRoleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "rolecode") String roleCode) {
        LangTuoResult<AdminRoleDTO> rtn = adminRoleMgtService.get(tenantCode, roleCode);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/admin/role/list?tenantCode=tenant_001&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<PageDTO<AdminRoleDTO>> list(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<AdminRoleDTO>> rtn = adminRoleMgtService.list(tenantCode, pageNum, pageSize);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/admin/role/list?tenantCode=tenant_001&roleName=系统超级管理员&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<AdminRoleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("roleName") String roleName, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<AdminRoleDTO>> rtn = adminRoleMgtService.search(tenantCode, roleName, pageNum, pageSize);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/admin/role/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody AdminRolePutRequest adminRolePutRequest) {
        LangTuoResult<Void> rtn = adminRoleMgtService.put(adminRolePutRequest);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/admin/role/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{rolecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "rolecode") String roleCode) {
        LangTuoResult<Void> rtn = adminRoleMgtService.delete(tenantCode, roleCode);
        return rtn;
    }
}
