package com.langtuo.teamachine.web.controller.securityset;

import com.langtuo.teamachine.api.model.menuset.SeriesDTO;
import com.langtuo.teamachine.api.model.securityset.OSSTokenDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.securityset.OSSService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/securityset/oss")
public class OSSController {
    @Resource
    private OSSService ossService;

    /**
     * url: http://localhost:8080/teamachine/securityset/oss/token/get
     * @return
     */
    @GetMapping(value = "/token/get")
    public LangTuoResult<OSSTokenDTO> getOSSToken() {
        LangTuoResult<OSSTokenDTO> rtn = ossService.getOSSToken();
        return rtn;
    }
}
