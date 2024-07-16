package com.langtuo.teamachine.web.controller.menuset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.SeriesDTO;
import com.langtuo.teamachine.api.request.SeriesPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.SeriesMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/menuset/series")
public class SeriesController {
    @Resource
    private SeriesMgtService service;

    /**
     * url: http://localhost:8080/teamachine/menuset/series/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{seriescode}/get")
    public LangTuoResult<SeriesDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "seriescode") String seriesCode) {
        LangTuoResult<SeriesDTO> rtn = service.getByCode(tenantCode, seriesCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/menuset/series/list?tenantCode=tenant_001
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<SeriesDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<SeriesDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/menuset/series/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<SeriesDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("seriesCode") String seriesCode, @RequestParam("seriesName") String seriesName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<SeriesDTO>> rtn = service.search(tenantCode, seriesCode, seriesName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/menuset/series/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody SeriesPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/menuset/series/{tenantcode}/{seriescode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{seriescode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "seriescode") String seriesCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, seriesCode);
        return rtn;
    }
}
