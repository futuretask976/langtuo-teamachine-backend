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

    @GetMapping(value = "/get")
    public TeaMachineResult<MachineDTO> get(@RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "machineCode") String machineCode) {
        TeaMachineResult<MachineDTO> rtn = service.getByCode(tenantCode, machineCode);
        return rtn;
    }

    @GetMapping(value = "/list")
    public TeaMachineResult<List<MachineDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        TeaMachineResult<List<MachineDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<MachineDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("machineCode") String machineCode, @RequestParam("screenCode") String screenCode,
            @RequestParam("elecBoardCode") String elecBoardCode, @RequestParam("shopCode") String shopCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<MachineDTO>> rtn = service.search(tenantCode, machineCode, screenCode, elecBoardCode,
                shopCode, pageNum, pageSize);
        return rtn;
    }

    @PutMapping(value = "/activate")
    public TeaMachineResult<MachineDTO> activate(@RequestBody MachineActivatePutRequest request) {
        TeaMachineResult<MachineDTO> rtn = service.activate(request);
        return rtn;
    }

    @PutMapping(value = "/update")
    public TeaMachineResult<Void> update(@RequestBody MachineUpdatePutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    @DeleteMapping(value = "/delete")
    public TeaMachineResult<Void> delete(@RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "machineCode") String machineCode) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, machineCode);
        return rtn;
    }
}
