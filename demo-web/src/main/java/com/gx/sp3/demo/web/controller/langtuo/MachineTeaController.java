package com.gx.sp3.demo.web.controller.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineTeaDTO;
import com.gx.sp3.demo.api.result.GxResult;
import com.gx.sp3.demo.api.service.langtuo.MachineTeaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/tea")
public class MachineTeaController {
    @Resource
    private MachineTeaService machineTeaService;

    /**
     * Access this method by http://localhost:8080/gxsp3demo/tea/list
     * @return
     */
    @GetMapping(value = "/list")
    public GxResult<List<MachineTeaDTO>> list() {
        GxResult<List<MachineTeaDTO>> result = machineTeaService.list();
        return result;
    }

    /**
     * Access this method by http://localhost:8080/gxsp3demo/tea/get
     * @return
     */
    @GetMapping(value = "/get")
    public GxResult<MachineTeaDTO> get(@RequestParam("machineCode") String machineCode, @RequestParam("teaCode") String teaCode) {
        GxResult<MachineTeaDTO> result = machineTeaService.get(machineCode, teaCode);
        return result;
    }
}
