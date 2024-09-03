package com.langtuo.teamachine.web.controller.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.SpecDTO;
import com.langtuo.teamachine.api.request.drink.SpecPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.SpecMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drinkset/spec")
public class SpecController {
    @Resource
    private SpecMgtService service;

    /**
     * url: http://{host}:{port}/teamachinebackend/drinkset/spec/{tenantcode}/{speccode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{speccode}/get")
    public TeaMachineResult<SpecDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "speccode") String specCode) {
        TeaMachineResult<SpecDTO> rtn = service.getByCode(tenantCode, specCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/drinkset/spec/list?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<SpecDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<SpecDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/drinkset/spec/search?tenantCode={tenantCode}&specCode={specCode}&specName={specName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<SpecDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("specCode") String specCode, @RequestParam("specName") String specName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<SpecDTO>> rtn = service.search(tenantCode, specCode, specName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/drinkset/spec/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody SpecPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/drinkset/spec/{tenantcode}/{speccode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{speccode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "speccode") String specCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, specCode);
        return rtn;
    }
}
