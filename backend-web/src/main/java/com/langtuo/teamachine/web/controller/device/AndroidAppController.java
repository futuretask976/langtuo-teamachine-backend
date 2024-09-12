package com.langtuo.teamachine.web.controller.device;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.device.AndroidAppDTO;
import com.langtuo.teamachine.api.request.device.AndroidAppPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.AndroidAppMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/deviceset/android/app")
public class AndroidAppController {
    @Resource
    private AndroidAppMgtService service;

    @GetMapping(value = "/get")
    public TeaMachineResult<AndroidAppDTO> get(@RequestParam(name = "version") String version) {
        TeaMachineResult<AndroidAppDTO> rtn = service.get(version);
        return rtn;
    }

    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<AndroidAppDTO>> search(@RequestParam("version") String version,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<AndroidAppDTO>> rtn = service.search(version, pageNum, pageSize);
        return rtn;
    }

    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody AndroidAppPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    @DeleteMapping(value = "/delete")
    public TeaMachineResult<Void> delete(@RequestParam(name = "version") String version) {
        TeaMachineResult<Void> rtn = service.delete(version);
        return rtn;
    }

    @GetMapping(value = "/dispatch")
    public TeaMachineResult<Void> dispatch(@RequestParam(name = "version") String version) {
        TeaMachineResult<Void> rtn = service.delete(version);
        return rtn;
    }
}
