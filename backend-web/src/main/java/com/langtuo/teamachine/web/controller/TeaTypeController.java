package com.langtuo.teamachine.web.controller;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.TeaTypeDTO;
import com.langtuo.teamachine.api.request.TeaTypePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.TeaTypeMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drinkset/tea/type")
public class TeaTypeController {
    @Resource
    private TeaTypeMgtService service;

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/type/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{teatypecode}/get")
    public LangTuoResult<TeaTypeDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "teatypecode") String teaTypeCode) {
        LangTuoResult<TeaTypeDTO> rtn = service.getByCode(tenantCode, teaTypeCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/type/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<TeaTypeDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<TeaTypeDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/type/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<TeaTypeDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("teaTypeCode") String teaTypeCode,
            @RequestParam("teaTypeName") String teaTypeName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<TeaTypeDTO>> rtn = service.search(tenantCode, teaTypeCode, teaTypeName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/type/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody TeaTypePutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/type/{tenantcode}/{shopgroupcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{teatypecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "teatypecode") String teaTypeCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, teaTypeCode);
        return rtn;
    }
}
