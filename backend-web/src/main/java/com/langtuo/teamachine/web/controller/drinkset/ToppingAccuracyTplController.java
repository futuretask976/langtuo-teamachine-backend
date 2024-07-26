package com.langtuo.teamachine.web.controller.drinkset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drinkset.ToppingAccuracyTplDTO;
import com.langtuo.teamachine.api.request.drinkset.ToppingAccuracyTplPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drinkset.ToppingAccuracyTplMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drinkset/topping/accuracy/template")
public class ToppingAccuracyTplController {
    @Resource
    private ToppingAccuracyTplMgtService service;

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/accuracy/template/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{templatecode}/get")
    public LangTuoResult<ToppingAccuracyTplDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "templatecode") String templateCode) {
        LangTuoResult<ToppingAccuracyTplDTO> rtn = service.getByCode(tenantCode, templateCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/accuracy/template/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<ToppingAccuracyTplDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<ToppingAccuracyTplDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/accuracy/template/search?tenantCode=tenant_001&specName=&pageNum=1&pageSize=10
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
     * url: http://localhost:8080/teamachine/drinkset/topping/accuracy/template/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody ToppingAccuracyTplPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/accuracy/template/{tenantcode}/{shopgroupcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{templatecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "templatecode") String templateCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, templateCode);
        return rtn;
    }
}
