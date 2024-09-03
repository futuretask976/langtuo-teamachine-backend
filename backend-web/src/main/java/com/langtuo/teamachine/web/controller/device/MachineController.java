package com.langtuo.teamachine.web.controller.device;

import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.device.MachineActivatePutRequest;
import com.langtuo.teamachine.api.request.device.MachineUpdatePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/deviceset/machine")
public class MachineController {
    @Resource
    private MachineMgtService service;

    /**
     * url: http://{host}:{port}/teamachinebackend/machine/{tenantcode}/{machinecode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{machinecode}/get")
    public TeaMachineResult<MachineDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "machinecode") String machineCode) {
        TeaMachineResult<MachineDTO> rtn = service.getByCode(tenantCode, machineCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/machine/list?tenantCode={tenantCode}
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<MachineDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<MachineDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/machine/search?tenantCode={tenantCode}&screenCode={screenCode}&elecBoardCode={elecBoardCode}&modelCode={modelCode}&shopName={shopName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<MachineDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("screenCode") String screenCode, @RequestParam("elecBoardCode") String elecBoardCode,
            @RequestParam("modelCode") String modelCode, @RequestParam("shopName") String shopName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<MachineDTO>> rtn = service.search(tenantCode, screenCode, elecBoardCode, modelCode,
                shopName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/machine/activate
     * @return
     */
    @PutMapping(value = "/activate")
    public TeaMachineResult<MachineDTO> activate(@RequestBody MachineActivatePutRequest request) {
        TeaMachineResult<MachineDTO> rtn = service.activate(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/machine/update
     * @return
     */
    @PutMapping(value = "/update")
    public TeaMachineResult<Void> update(@RequestBody MachineUpdatePutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/machine/{tenantcode}/{machinecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{machinecode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "machinecode") String machineCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, machineCode);
        return rtn;
    }
}
