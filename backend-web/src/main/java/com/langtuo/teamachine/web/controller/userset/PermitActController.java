package com.langtuo.teamachine.web.controller.userset;

import com.langtuo.teamachine.api.model.PermitActGroupDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.PermitActMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/permitact")
public class PermitActController {
    @Resource
    private PermitActMgtService service;

    /**
     * url: http://localhost:8080/teamachine/permitact/list
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<PermitActGroupDTO>> list() {
        LangTuoResult<List<PermitActGroupDTO>> rtn = service.list();
        return rtn;
    }
}
