package com.langtuo.teamachine.web.controller;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.PermitActGroupDTO;
import com.langtuo.teamachine.api.model.TenantDTO;
import com.langtuo.teamachine.api.request.TenantPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.PermitActMgtService;
import com.langtuo.teamachine.api.service.TenantMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/permitact")
public class PermitActController {
    @Resource
    private PermitActMgtService permitActMgtService;

    /**
     * Access: http://localhost:8080/teamachine/permitact/group/list
     * @return
     */
    @GetMapping(value = "/group/list")
    public LangTuoResult<List<PermitActGroupDTO>> list() {
        LangTuoResult<List<PermitActGroupDTO>> rtn = permitActMgtService.list();
        return rtn;
    }
}
