package com.langtuo.teamachine.web.controller.device;

import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.device.MachineActivatePutRequest;
import com.langtuo.teamachine.api.request.device.MachineUpdatePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
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
     * url: http://localhost:8080/teamachine/machine/{tenantcode}/{machinecode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{machinecode}/get")
    public LangTuoResult<MachineDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "machinecode") String machineCode) {
        LangTuoResult<MachineDTO> rtn = service.get(tenantCode, machineCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/list?tenantCode={tenantCode}
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<MachineDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<MachineDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/search?tenantCode={tenantCode}&screenCode={screenCode}&elecBoardCode={elecBoardCode}&modelCode={modelCode}&shopName={shopName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<MachineDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("screenCode") String screenCode, @RequestParam("elecBoardCode") String elecBoardCode,
            @RequestParam("modelCode") String modelCode, @RequestParam("shopName") String shopName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<MachineDTO>> rtn = service.search(tenantCode, screenCode, elecBoardCode, modelCode,
                shopName, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/activate
     * @return
     */
    @PutMapping(value = "/activate")
    public LangTuoResult<MachineDTO> activate(@RequestBody MachineActivatePutRequest request) {
        LangTuoResult<MachineDTO> rtn = service.activate(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/update
     * @return
     */
    @PutMapping(value = "/update")
    public LangTuoResult<Void> update(@RequestBody MachineUpdatePutRequest request) {
        LangTuoResult<Void> rtn = service.update(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/{tenantcode}/{machinecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{machinecode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "machinecode") String machineCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, machineCode);
        return rtn;
    }
}
