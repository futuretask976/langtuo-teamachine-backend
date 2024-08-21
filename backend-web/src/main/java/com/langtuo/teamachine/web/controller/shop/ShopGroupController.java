package com.langtuo.teamachine.web.controller.shop;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.request.shop.ShopGroupPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shopset/shop/group")
public class ShopGroupController {
    @Resource
    private ShopGroupMgtService service;

    /**
     * url: http://localhost:8080/teamachine/shop/group/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{shopgroupcode}/get")
    public TeaMachineResult<ShopGroupDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "shopgroupcode") String shopGroupCode) {
        TeaMachineResult<ShopGroupDTO> rtn = service.getByCode(tenantCode, shopGroupCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/shop/group/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<ShopGroupDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<ShopGroupDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/shop/group/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/listbyadminorg")
    public TeaMachineResult<List<ShopGroupDTO>> listByAdminOrg(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<ShopGroupDTO>> rtn = service.listByAdminOrg(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/shop/group/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<ShopGroupDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopGroupName") String shopGroupName, @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<ShopGroupDTO>> rtn = service.search(tenantCode, shopGroupName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/shop/group/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody ShopGroupPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/shop/group/{tenantcode}/{shopgroupcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{shopgroupcode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "shopgroupcode") String shopGroupCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, shopGroupCode);
        return rtn;
    }
}
