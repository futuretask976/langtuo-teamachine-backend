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

    @GetMapping(value = "/get")
    public TeaMachineResult<ToppingDTO> get(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("toppingCode") String toppingCode) {
        TeaMachineResult<ToppingDTO> rtn = service.getByToppingCode(tenantCode, toppingCode);
        return rtn;
    }

    @GetMapping(value = "/list")
    public TeaMachineResult<List<ToppingDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<ToppingDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<ToppingDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam(name = "toppingCode", required = false) String toppingCode,
            @RequestParam(name = "toppingName", required = false) String toppingName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<ToppingDTO>> rtn = service.search(tenantCode, toppingCode, toppingName,
                pageNum, pageSize);
        return rtn;
    }

    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody ToppingPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    @DeleteMapping(value = "/delete")
    public TeaMachineResult<Void> delete(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("toppingCode") String toppingCode) {
        TeaMachineResult<Void> rtn = service.deleteByToppingCode(tenantCode, toppingCode);
        return rtn;
    }
}
