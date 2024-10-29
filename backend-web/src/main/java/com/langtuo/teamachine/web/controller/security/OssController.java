package com.langtuo.teamachine.web.controller.security;

import com.langtuo.teamachine.api.model.security.OssTokenDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.security.OssService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/securityset/oss")
public class OssController {
    @Resource
    private OssService ossService;

    /**
     * url: http://localhost:8080/teamachinebackend/securityset/oss/token/get
     * @return
     */
    @GetMapping(value = "/token/get")
    public TeaMachineResult<OssTokenDTO> getOssToken() {
        TeaMachineResult<OssTokenDTO> rtn = ossService.getOssToken();
        return rtn;
    }
}
