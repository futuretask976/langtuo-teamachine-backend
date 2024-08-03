package com.langtuo.teamachine.biz.service.impl.security;

import com.langtuo.teamachine.api.model.security.OSSTokenDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.security.OSSService;
import com.langtuo.teamachine.dao.oss.OSSUtils;
import com.langtuo.teamachine.dao.po.security.OSSTokenPO;
import org.springframework.stereotype.Component;

@Component
public class OSSServiceImpl implements OSSService {
    @Override
    public LangTuoResult<OSSTokenDTO> getOSSToken() {
        OSSTokenPO ossTokenPO = OSSUtils.getSTS();
        return LangTuoResult.success(ossTokenPO);
    }
}
