package com.langtuo.teamachine.web.controller.user;

import com.langtuo.teamachine.api.model.user.RoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.RolePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.RoleMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/userset/role")
public class RoleController {
    @Resource
    private RoleMgtService service;

    /**
     * url: http://{host}:{port}/teamachinebackend/role/tenant_001/{rolecode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{rolecode}/get")
    public TeaMachineResult<RoleDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "rolecode") String roleCode) {
        TeaMachineResult<RoleDTO> rtn = service.getByCode(tenantCode, roleCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/role/list?tenantCode=tenant_001
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<RoleDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<RoleDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/role/list?tenantCode=tenant_001&roleName=系统超级管理员&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<RoleDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("roleName") String roleName, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<RoleDTO>> rtn = service.search(tenantCode, roleName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/role/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody RolePutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/role/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{rolecode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "rolecode") String roleCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, roleCode);
        return rtn;
    }
}
