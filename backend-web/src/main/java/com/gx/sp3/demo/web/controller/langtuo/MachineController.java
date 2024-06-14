package com.gx.sp3.demo.web.controller.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineDTO;
import com.gx.sp3.demo.api.result.GxResult;
import com.gx.sp3.demo.api.service.langtuo.MachineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/machine")
public class MachineController {
    @Resource
    private MachineService machineService;

    /**
     * Access this method by http://localhost:8080/gxsp3demo/machine/list
     * @return
     */
    @GetMapping(value = "/list")
    public GxResult<List<MachineDTO>> list() {
        System.out.printf("MachineController#list entering: %s\n", System.currentTimeMillis());
        GxResult<List<MachineDTO>> result = machineService.list();
        return result;
    }

    /**
     * Access this method by http://localhost:8080/gxsp3demo/machine/get
     * @return
     */
    @GetMapping(value = "/get")
    public GxResult<MachineDTO> get(@RequestParam("machineCode") String machineCode) {
        GxResult<MachineDTO> result = machineService.get(machineCode);
        return result;
    }
}
