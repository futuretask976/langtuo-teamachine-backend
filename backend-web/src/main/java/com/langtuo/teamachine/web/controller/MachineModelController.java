package com.langtuo.teamachine.web.controller;

import com.langtuo.teamachine.api.model.MachineModelDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.MachineModelPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.MachineModelMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/machine/model")
public class MachineModelController {
    @Resource
    private MachineModelMgtService service;

    /**
     * url: http://localhost:8080/teamachine/machine/model/get
     * @return
     */
    @GetMapping(value = "/{modelcode}/get")
    public LangTuoResult<MachineModelDTO> get(@PathVariable(name = "modelcode") String modelCode) {
        LangTuoResult<MachineModelDTO> rtn = service.get(modelCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/model/list?pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<PageDTO<MachineModelDTO>> list() {
        LangTuoResult<PageDTO<MachineModelDTO>> rtn = service.list();
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/model/search?modelCode=model_001&pageNum=1&pageSize=2
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<MachineModelDTO>> search(@RequestParam("modelCode") String modelCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<MachineModelDTO>> rtn = service.search(modelCode, pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/model/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody MachineModelPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/machine/model/put
     * @return
     */
    @DeleteMapping(value = "/{modelcode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "modelcode") String modelCode) {
        LangTuoResult<Void> rtn = service.delete(modelCode);
        return rtn;
    }
}
