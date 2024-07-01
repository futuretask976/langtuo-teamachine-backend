package com.langtuo.teamachine.web.controller;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.ShopGroupDTO;
import com.langtuo.teamachine.api.request.ShopGroupPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.ShopGroupMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shop/group")
public class ShopGroupController {
    @Resource
    private ShopGroupMgtService shopGroupMgtService;

    /**
     * url: http://localhost:8080/teamachine/shop/group/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{shopgroupcode}/get")
    public LangTuoResult<ShopGroupDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "shopgroupcode") String shopGroupCode) {
        LangTuoResult<ShopGroupDTO> rtn = shopGroupMgtService.get(tenantCode, shopGroupCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/shop/group/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<ShopGroupDTO>> search(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<ShopGroupDTO>> rtn = shopGroupMgtService.list(tenantCode);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/admin/list?tenantCode=tenant_001&roleName=系统超级管理员&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<ShopGroupDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopGroupName") String shopGroupName, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<ShopGroupDTO>> rtn = shopGroupMgtService.search(tenantCode, shopGroupName, pageNum, pageSize);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/admin/role/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody ShopGroupPutRequest shopGroupPutRequest) {
        LangTuoResult<Void> rtn = shopGroupMgtService.put(shopGroupPutRequest);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/admin/role/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{shopgroupcode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "shopgroupcode") String shopGroupCode) {
        LangTuoResult<Void> rtn = shopGroupMgtService.delete(tenantCode, shopGroupCode);
        return rtn;
    }
}
