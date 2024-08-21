package com.langtuo.teamachine.web.controller.user;

import com.langtuo.teamachine.api.model.user.AdminDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.AdminPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.AdminMgtService;
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
    public TeaMachineResult<AdminDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "loginname") String loginName) {
        TeaMachineResult<AdminDTO> rtn = service.get(tenantCode, loginName);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/list?tenantCode=tenant_001&roleName=系统超级管理员&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<AdminDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("loginName") String loginName, @RequestParam("roleName") String roleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<AdminDTO>> rtn = service.search(tenantCode, loginName, roleName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/role/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody AdminPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/admin/role/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{loginname}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "loginname") String loginName) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, loginName);
        return rtn;
    }
}
