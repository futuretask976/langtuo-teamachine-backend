package com.langtuo.teamachine.biz.service.impl.securityset;

import com.langtuo.teamachine.api.model.securityset.OSSTokenDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.securityset.OSSService;
import com.langtuo.teamachine.dao.oss.OSSUtils;
import com.langtuo.teamachine.dao.po.securityset.OSSTokenPO;
import org.springframework.stereotype.Component;

@Component
public class OSSServiceImpl implements OSSService {
    @Override
    public LangTuoResult<OSSTokenDTO> getOSSToken() {
        OSSTokenPO ossTokenPO = OSSUtils.getSTS();
        return LangTuoResult.success(ossTokenPO);
    }
}
