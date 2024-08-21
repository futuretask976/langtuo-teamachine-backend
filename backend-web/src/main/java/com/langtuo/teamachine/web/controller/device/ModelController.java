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

    /**
     * url: http://localhost:8080/teamachine/model/{modelcode}/get
     * @return
     */
    @GetMapping(value = "/{modelcode}/get")
    public TeaMachineResult<ModelDTO> get(@PathVariable(name = "modelcode") String modelCode) {
        TeaMachineResult<ModelDTO> rtn = service.get(modelCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/model/list
     * @return
     */
    @GetMapping(value = "/list")
    public TeaMachineResult<List<ModelDTO>> list() {
        TeaMachineResult<List<ModelDTO>> rtn = service.list();
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/model/search?modelCode={modelCode}&pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<ModelDTO>> search(@RequestParam("modelCode") String modelCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<ModelDTO>> rtn = service.search(modelCode, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/model/put
     * @return
     */
    @PutMapping(value = "/put")
    public TeaMachineResult<Void> put(@RequestBody ModelPutRequest request) {
        TeaMachineResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/model/{modelcode}/delete
     * @return
     */
    @DeleteMapping(value = "/{modelcode}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "modelcode") String modelCode) {
        TeaMachineResult<Void> rtn = service.delete(modelCode);
        return rtn;
    }
}
