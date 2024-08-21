package com.langtuo.teamachine.web.controller.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.TeaTypeDTO;
import com.langtuo.teamachine.api.request.drink.TeaTypePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.TeaTypeMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drinkset/tea/type")
public class TeaTypeController {
    @Resource
    private TeaTypeMgtService service;

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/type/{tenantcode}/{teatypecode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{teatypecode}/get")
    public TeaMachineResult<TeaTypeDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "teatypecode") String teaTypeCode) {
        TeaMachineResult<TeaTypeDTO> rtn = service.getByCode(tenantCode, teaTypeCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/type/list?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<TeaTypeDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<TeaTypeDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/type/search?tenantCode={tenantCode}&teaTypeCode={teaTypeCode}&teaTypeName={teaTypeName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<TeaTypeDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("teaTypeCode") String teaTypeCode,
            @RequestParam("teaTypeName") String teaTypeName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<TeaTypeDTO>> rtn = service.search(tenantCode, teaTypeCode, teaTypeName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/type/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody TeaTypePutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/type/{tenantcode}/{teatypecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{teatypecode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "teatypecode") String teaTypeCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, teaTypeCode);
        return rtn;
    }
}
