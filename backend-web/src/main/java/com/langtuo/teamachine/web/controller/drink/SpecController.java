package com.langtuo.teamachine.web.controller.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.SpecDTO;
import com.langtuo.teamachine.api.request.drink.SpecPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
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
     * url: http://localhost:8080/teamachine/drinkset/spec/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{speccode}/get")
    public LangTuoResult<SpecDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "speccode") String specCode) {
        LangTuoResult<SpecDTO> rtn = service.getByCode(tenantCode, specCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/spec/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<SpecDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<SpecDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/spec/search?tenantCode=tenant_001&specName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<SpecDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("specCode") String specCode, @RequestParam("specName") String specName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<SpecDTO>> rtn = service.search(tenantCode, specCode, specName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody SpecPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/{tenantcode}/{shopgroupcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{speccode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "speccode") String specCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, specCode);
        return rtn;
    }
}
