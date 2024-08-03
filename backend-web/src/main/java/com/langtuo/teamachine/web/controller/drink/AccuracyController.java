package com.langtuo.teamachine.web.controller.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingAccuracyTplDTO;
import com.langtuo.teamachine.api.request.drink.ToppingAccuracyTplPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.ToppingAccuracyTplMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drinkset/accuracy")
public class AccuracyController {
    @Resource
    private ToppingAccuracyTplMgtService service;

    /**
     * url: http://localhost:8080/teamachine/drinkset/accuracy/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{templatecode}/get")
    public LangTuoResult<ToppingAccuracyTplDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "templatecode") String templateCode) {
        LangTuoResult<ToppingAccuracyTplDTO> rtn = service.getByCode(tenantCode, templateCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/accuracy/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<ToppingAccuracyTplDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<ToppingAccuracyTplDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/accuracy/search?tenantCode=tenant_001&specName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<ToppingAccuracyTplDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("templateCode") String templateCode, @RequestParam("templateName") String templateName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<ToppingAccuracyTplDTO>> rtn = service.search(tenantCode, templateCode, templateName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/accuracy/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody ToppingAccuracyTplPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/accuracy/{tenantcode}/{shopgroupcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{templatecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "templatecode") String templateCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, templateCode);
        return rtn;
    }
}
