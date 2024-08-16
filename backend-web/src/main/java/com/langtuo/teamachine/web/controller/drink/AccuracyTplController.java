package com.langtuo.teamachine.web.controller.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.AccuracyTplDTO;
import com.langtuo.teamachine.api.request.drink.AccuracyTplPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.AccuracyTplMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drinkset/accuracy")
public class AccuracyTplController {
    @Resource
    private AccuracyTplMgtService service;

    /**
     * url: http://localhost:8080/teamachine/drinkset/accuracy/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{templatecode}/get")
    public LangTuoResult<AccuracyTplDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "templatecode") String templateCode) {
        LangTuoResult<AccuracyTplDTO> rtn = service.getByCode(tenantCode, templateCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/accuracy/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<AccuracyTplDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<AccuracyTplDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/accuracy/search?tenantCode=tenant_001&specName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<AccuracyTplDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("templateCode") String templateCode, @RequestParam("templateName") String templateName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<AccuracyTplDTO>> rtn = service.search(tenantCode, templateCode, templateName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/accuracy/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody AccuracyTplPutRequest request) {
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
