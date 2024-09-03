package com.langtuo.teamachine.web.controller.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.AccuracyTplDTO;
import com.langtuo.teamachine.api.request.drink.AccuracyTplPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
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
     * url: http://{host}:{port}/teamachinebackend/drinkset/accuracy/{tenantcode}/{templatecode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{templatecode}/get")
    public TeaMachineResult<AccuracyTplDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "templatecode") String templateCode) {
        TeaMachineResult<AccuracyTplDTO> rtn = service.getByCode(tenantCode, templateCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/drinkset/accuracy/list?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<AccuracyTplDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<AccuracyTplDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/drinkset/accuracy/search?tenantCode={tenantCode}&templateCode={templateCode}&templateName={templateName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<AccuracyTplDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("templateCode") String templateCode, @RequestParam("templateName") String templateName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<AccuracyTplDTO>> rtn = service.search(tenantCode, templateCode, templateName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/drinkset/accuracy/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody AccuracyTplPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/drinkset/accuracy/{tenantcode}/{templatecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{templatecode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "templatecode") String templateCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, templateCode);
        return rtn;
    }
}
