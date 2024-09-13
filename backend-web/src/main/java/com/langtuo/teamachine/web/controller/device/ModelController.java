package com.langtuo.teamachine.web.controller.device;

import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.device.ModelPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.ModelMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/deviceset/model")
public class ModelController {
    @Resource
    private ModelMgtService service;

    @GetMapping(value = "/get")
    public TeaMachineResult<ModelDTO> get(@RequestParam("modelCode") String modelCode) {
        TeaMachineResult<ModelDTO> rtn = service.get(modelCode);
        return rtn;
    }

    @GetMapping(value = "/list")
    public TeaMachineResult<List<ModelDTO>> list() {
        TeaMachineResult<List<ModelDTO>> rtn = service.list();
        return rtn;
    }

    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<ModelDTO>> search(
            @RequestParam(name = "modelCode", required = false) String modelCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<ModelDTO>> rtn = service.search(modelCode, pageNum, pageSize);
        return rtn;
    }

    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody ModelPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    @DeleteMapping(value = "/delete")
    public TeaMachineResult<Void> delete(@RequestParam("modelCode") String modelCode) {
        TeaMachineResult<Void> rtn = service.delete(modelCode);
        return rtn;
    }
}
