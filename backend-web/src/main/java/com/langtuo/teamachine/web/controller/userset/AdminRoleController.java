package com.langtuo.teamachine.web.controller.userset;

import com.langtuo.teamachine.api.model.userset.RoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.userset.RolePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.userset.AdminRoleMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/userset/admin/role")
public class AdminRoleController {
    @Resource
    private AdminRoleMgtService service;

    /**
     * url: http://localhost:8080/teamachine/admin/role/tenant_001/{rolecode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{rolecode}/get")
    public LangTuoResult<RoleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "rolecode") String roleCode) {
        LangTuoResult<RoleDTO> rtn = service.get(tenantCode, roleCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/role/list?tenantCode=tenant_001
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<RoleDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<RoleDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/role/list?tenantCode=tenant_001&roleName=系统超级管理员&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<RoleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("roleName") String roleName, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<RoleDTO>> rtn = service.search(tenantCode, roleName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/role/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody RolePutRequest request) {
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