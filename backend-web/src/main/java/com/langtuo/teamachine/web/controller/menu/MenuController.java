package com.langtuo.teamachine.web.controller.menu;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menu.MenuDTO;
import com.langtuo.teamachine.api.model.menu.MenuDispatchDTO;
import com.langtuo.teamachine.api.request.menu.MenuDispatchPutRequest;
import com.langtuo.teamachine.api.request.menu.MenuPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.menu.MenuMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/menuset/menu")
public class MenuController {
    @Resource
    private MenuMgtService service;

    /**
     * url: http://{host}:{port}/teamachine/menuset/menu/{tenantcode}/{menucode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{menucode}/get")
    public TeaMachineResult<MenuDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "menucode") String menuCode) {
        TeaMachineResult<MenuDTO> rtn = service.getByCode(tenantCode, menuCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachine/menuset/menu/list?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<MenuDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<MenuDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachine/menuset/menu/trigger?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/trigger")
    public TeaMachineResult<Void> trigger(@RequestParam("tenantCode") String tenantCode
            , @RequestParam("shopCode") String shopCode, @RequestParam("machineCode") String machineCode) {
        TeaMachineResult<Void> rtn = service.triggerDispatchByShopCode(tenantCode, shopCode, machineCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachine/menuset/menu/search?tenantCode={tenantCode}&menuCode={menuCode}&menuName={menuName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<MenuDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("menuCode") String menuCode, @RequestParam("menuName") String menuName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<MenuDTO>> rtn = service.search(tenantCode, menuCode, menuName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachine/menuset/menu/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody MenuPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachine/menuset/menu/{tenantcode}/{menucode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{menucode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "menucode") String menuCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, menuCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachine/menuset/menu/dispatch/put
     * @return
     */
    @PutMapping(value = "/dispatch/put")
    public TeaMachineResult<Void> putDispatch(@RequestBody MenuDispatchPutRequest request) {
        TeaMachineResult<Void> rtn = service.putDispatch(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachine/menuset/menu/dispatch/{tenantcode}/{menucode}/get
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/dispatch/{tenantcode}/{menucode}/get")
    public TeaMachineResult<MenuDispatchDTO> getDispatchByMenuCode(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "menucode") String menuCode) {
        TeaMachineResult<MenuDispatchDTO> rtn = service.getDispatchByCode(tenantCode, menuCode);
        return rtn;
    }
}
