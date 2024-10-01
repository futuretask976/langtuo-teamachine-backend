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
    
    @GetMapping(value = "/get")
    public TeaMachineResult<AdminDTO> get(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("loginName") String loginName) {
        log.info("adminController|get|entering|" + tenantCode + "|" + loginName + "|001");
        TeaMachineResult<AdminDTO> rtn = service.getByLoginName(tenantCode, loginName);
        return rtn;
    }
    
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<AdminDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam(name = "loginName", required = false) String loginName,
            @RequestParam(name = "roleCode", required = false) String roleCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        log.info("adminController|search|entering|" + tenantCode + "|" + loginName);
        System.out.println("adminController|search|entering|" + tenantCode + "|" + loginName);
        TeaMachineResult<PageDTO<AdminDTO>> rtn = service.search(tenantCode, loginName, roleCode, pageNum, pageSize);
        return rtn;
    }
    
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody AdminPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }
    
    @DeleteMapping(value = "/delete")
    public TeaMachineResult<Void> delete(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("loginName") String loginName) {
        TeaMachineResult<Void> rtn = service.deleteByLoginName(tenantCode, loginName);
        return rtn;
    }
}
