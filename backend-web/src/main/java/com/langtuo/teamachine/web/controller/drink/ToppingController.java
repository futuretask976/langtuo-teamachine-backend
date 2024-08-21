package com.langtuo.teamachine.web.controller.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingDTO;
import com.langtuo.teamachine.api.request.drink.ToppingPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
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
     * url: http://localhost:8080/teamachine/drinkset/topping/{tenantcode}/{toppingcode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{toppingcode}/get")
    public TeaMachineResult<ToppingDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "toppingcode") String toppingCode) {
        TeaMachineResult<ToppingDTO> rtn = service.getByCode(tenantCode, toppingCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/list?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<ToppingDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<ToppingDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/search?tenantCode={tenantCode}&toppingCode={toppingCode}&toppingName={toppingName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<ToppingDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("toppingCode") String toppingCode, @RequestParam("toppingName") String toppingName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<ToppingDTO>> rtn = service.search(tenantCode, toppingCode, toppingName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody ToppingPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/{tenantcode}/{toppingcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{toppingcode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "toppingcode") String toppingCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, toppingCode);
        return rtn;
    }
}
