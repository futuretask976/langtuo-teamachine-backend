package com.gx.sp3.demo.web.controller.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineToppingDTO;
import com.gx.sp3.demo.api.result.GxResult;
import com.gx.sp3.demo.api.service.langtuo.MachineToppingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/topping")
public class MachineToppingController {
    @Resource
    private MachineToppingService machineToppingService;

    /**
     * Access this method by http://localhost:8080/gxsp3demo/topping/list
     * @return
     */
    @GetMapping(value = "/list")
    public GxResult<List<MachineToppingDTO>> list() {
        GxResult<List<MachineToppingDTO>> result = machineToppingService.list();
        return result;
    }

    /**
     * Access this method by http://localhost:8080/gxsp3demo/topping/get
     * @return
     */
    @GetMapping(value = "/get")
    public GxResult<MachineToppingDTO> get(@RequestParam("machineCode") String machineCode, @RequestParam("toppingCode") String toppingCode) {
        GxResult<MachineToppingDTO> result = machineToppingService.get(machineCode, toppingCode);
        return result;
    }
}
