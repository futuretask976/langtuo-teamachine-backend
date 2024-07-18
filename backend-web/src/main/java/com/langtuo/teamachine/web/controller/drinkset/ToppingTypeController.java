package com.langtuo.teamachine.web.controller.drinkset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drinkset.ToppingTypeDTO;
import com.langtuo.teamachine.api.request.drinkset.ToppingTypePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drinkset.ToppingTypeMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drinkset/topping/type")
public class ToppingTypeController {
    @Resource
    private ToppingTypeMgtService service;

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/type/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{toppingtypecode}/get")
    public LangTuoResult<ToppingTypeDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "toppingtypecode") String toppingTypeCode) {
        LangTuoResult<ToppingTypeDTO> rtn = service.getByCode(tenantCode, toppingTypeCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/type/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<ToppingTypeDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<ToppingTypeDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/type/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<ToppingTypeDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("toppingTypeCode") String toppingTypeCode,
            @RequestParam("toppingTypeName") String toppingTypeName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<ToppingTypeDTO>> rtn = service.search(tenantCode, toppingTypeCode, toppingTypeName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/type/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody ToppingTypePutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/type/{tenantcode}/{shopgroupcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{toppingtypecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "toppingtypecode") String toppingTypeCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, toppingTypeCode);
        return rtn;
    }
}
