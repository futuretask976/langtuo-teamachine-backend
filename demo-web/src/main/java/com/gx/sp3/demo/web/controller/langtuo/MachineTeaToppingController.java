package com.gx.sp3.demo.web.controller.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineTeaToppingDTO;
import com.gx.sp3.demo.api.result.GxResult;
import com.gx.sp3.demo.api.service.langtuo.MachineTeaToppingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/teatopping")
public class MachineTeaToppingController {
    @Resource
    private MachineTeaToppingService machineTeaToppingService;

    /**
     * Access this method by http://localhost:8080/gxsp3demo/topping/list
     * @return
     */
    @GetMapping(value = "/list")
    public GxResult<List<MachineTeaToppingDTO>> list() {
        GxResult<List<MachineTeaToppingDTO>> result = machineTeaToppingService.list();
        return result;
    }

    /**
     * Access this method by http://localhost:8080/gxsp3demo/topping/get
     * @return
     */
    @GetMapping(value = "/get")
    public GxResult<MachineTeaToppingDTO> get(@RequestParam("machineCode") String machineCode, @RequestParam("teaCode") String teaCode, @RequestParam("toppingCode") String toppingCode) {
        GxResult<MachineTeaToppingDTO> result = machineTeaToppingService.get(machineCode, teaCode, toppingCode);
        return result;
    }
}
