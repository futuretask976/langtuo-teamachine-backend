package com.langtuo.teamachine.web.controller.shop;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.request.shop.ShopPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shopset/shop")
public class ShopController {
    @Resource
    private ShopMgtService service;

    /**
     * url: http://{host}:{port}/teamachinebackend/shop/tenant_001/shop_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{shopcode}/get")
    public TeaMachineResult<ShopDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "shopcode") String shopCode) {
        TeaMachineResult<ShopDTO> rtn = service.getByCode(tenantCode, shopCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/shop/list?tenantCode=tenant_001
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<ShopDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<ShopDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/shop/list?tenantCode=tenant_001
     * @return
     */
    @GetMapping(value = "/listbyadminorg")
    public TeaMachineResult<List<ShopDTO>> listByAdminOrg(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<ShopDTO>> rtn = service.listByAdminOrg(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/shop/search?tenantCode=tenant_001&shopName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<ShopDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopName") String shopName, @RequestParam("shopGroupName") String shopGroupName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<ShopDTO>> rtn = service.search(tenantCode, shopName, shopGroupName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/shop/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody ShopPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/shop//{tenantcode}/{shopcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{shopcode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "shopcode") String shopCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, shopCode);
        return rtn;
    }
}
