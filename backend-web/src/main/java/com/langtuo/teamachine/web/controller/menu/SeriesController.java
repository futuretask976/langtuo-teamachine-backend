package com.langtuo.teamachine.web.controller.menu;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menu.SeriesDTO;
import com.langtuo.teamachine.api.request.menu.SeriesPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.menu.SeriesMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/menuset/series")
public class SeriesController {
    @Resource
    private SeriesMgtService service;

    /**
     * url: http://{host}:{port}/teamachinebackend/menuset/series/{tenantcode}/{seriescode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{seriescode}/get")
    public TeaMachineResult<SeriesDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "seriescode") String seriesCode) {
        TeaMachineResult<SeriesDTO> rtn = service.getByCode(tenantCode, seriesCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/menuset/series/list?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<SeriesDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<SeriesDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/menuset/series/search?tenantCode={tenantCode}&seriesCode={seriesCode}&seriesName={seriesName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<SeriesDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("seriesCode") String seriesCode, @RequestParam("seriesName") String seriesName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<SeriesDTO>> rtn = service.search(tenantCode, seriesCode, seriesName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/menuset/series/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody SeriesPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/menuset/series/{tenantcode}/{seriescode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{seriescode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "seriescode") String seriesCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, seriesCode);
        return rtn;
    }
}
