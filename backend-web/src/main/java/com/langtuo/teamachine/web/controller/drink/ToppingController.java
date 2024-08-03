package com.langtuo.teamachine.web.controller.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingDTO;
import com.langtuo.teamachine.api.request.drink.ToppingPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drinkset/topping")
public class ToppingController {
    @Resource
    private ToppingMgtService service;

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{toppingcode}/get")
    public LangTuoResult<ToppingDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "toppingcode") String toppingCode) {
        LangTuoResult<ToppingDTO> rtn = service.getByCode(tenantCode, toppingCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<ToppingDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<ToppingDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<ToppingDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("toppingCode") String toppingCode, @RequestParam("toppingName") String toppingName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<ToppingDTO>> rtn = service.search(tenantCode, toppingCode, toppingName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody ToppingPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/{tenantcode}/{shopgroupcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{toppingcode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "toppingcode") String toppingCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, toppingCode);
        return rtn;
    }
}
