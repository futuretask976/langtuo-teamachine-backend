package com.langtuo.teamachine.web.controller.menu;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menu.MenuDTO;
import com.langtuo.teamachine.api.model.menu.MenuDispatchDTO;
import com.langtuo.teamachine.api.request.menu.MenuDispatchPutRequest;
import com.langtuo.teamachine.api.request.menu.MenuPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
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
     * url: http://localhost:8080/teamachine/menuset/menu/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{menucode}/get")
    public LangTuoResult<MenuDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "menucode") String menuCode) {
        LangTuoResult<MenuDTO> rtn = service.getByCode(tenantCode, menuCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/menuset/menu/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<MenuDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<MenuDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/menuset/menu/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<MenuDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("menuCode") String menuCode, @RequestParam("menuName") String menuName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<MenuDTO>> rtn = service.search(tenantCode, menuCode, menuName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/menuset/menu/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody MenuPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/menuset/menu/{tenantcode}/{menucode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{menucode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "menucode") String menuCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, menuCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/menuset/menu/put
     * @return
     */
    @PutMapping(value = "/dispatch/put")
    public LangTuoResult<Void> putDispatch(@RequestBody MenuDispatchPutRequest request) {
        LangTuoResult<Void> rtn = service.putDispatch(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/menuset/menu/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/dispatch/{tenantcode}/{menucode}/get")
    public LangTuoResult<MenuDispatchDTO> getDispatchByMenuCode(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "menucode") String menuCode) {
        LangTuoResult<MenuDispatchDTO> rtn = service.listDispatchByMenuCode(tenantCode, menuCode);
        return rtn;
    }
}
