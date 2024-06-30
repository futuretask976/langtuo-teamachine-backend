package com.langtuo.teamachine.web.controller;

import com.langtuo.teamachine.api.model.AdminDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.AdminPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.AdminMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminMgtService adminMgtService;

    /**
     * Access: http://localhost:8080/teamachine/admin/tenant_001/jiaqing001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{loginname}/get")
    public LangTuoResult<AdminDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "loginname") String loginName) {
        LangTuoResult<AdminDTO> rtn = adminMgtService.get(tenantCode, loginName);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/admin/list?tenantCode=tenant_001&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<PageDTO<AdminDTO>> list(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<AdminDTO>> rtn = adminMgtService.list(tenantCode, pageNum, pageSize);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/admin/list?tenantCode=tenant_001&roleName=系统超级管理员&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<AdminDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("loginName") String loginName, @RequestParam("roleName") String roleName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<AdminDTO>> rtn = adminMgtService.search(tenantCode, loginName, roleName, pageNum, pageSize);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/admin/role/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody AdminPutRequest adminPutRequest) {
        LangTuoResult<Void> rtn = adminMgtService.put(adminPutRequest);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/admin/role/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{loginname}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "loginname") String loginName) {
        LangTuoResult<Void> rtn = adminMgtService.delete(tenantCode, loginName);
        return rtn;
    }
}
