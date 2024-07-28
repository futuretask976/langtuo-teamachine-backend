package com.langtuo.teamachine.web.controller.drinkset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drinkset.TeaDTO;
import com.langtuo.teamachine.api.request.drinkset.TeaPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drinkset.TeaMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drinkset/tea")
public class TeaController {
    @Resource
    private TeaMgtService service;

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/tenant_001/TEA_03/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{teacode}/get")
    public LangTuoResult<TeaDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "teacode") String teaCode) {
        LangTuoResult<TeaDTO> rtn = service.getByCode(tenantCode, teaCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<TeaDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<TeaDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/type/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<TeaDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("teaCode") String teaCode, @RequestParam("teaName") String teaName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<TeaDTO>> rtn = service.search(tenantCode, teaCode, teaName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody TeaPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/{tenantcode}/{teacode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{teacode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "teacode") String teaCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, teaCode);
        return rtn;
    }
}
