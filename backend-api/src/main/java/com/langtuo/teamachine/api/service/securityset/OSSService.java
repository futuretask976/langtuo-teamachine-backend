package com.langtuo.teamachine.api.service.securityset;

import com.langtuo.teamachine.api.model.securityset.OSSTokenDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;

public interface OSSService {
    /**
     *
     * @return
     */
    LangTuoResult<OSSTokenDTO> getOSSToken();
}
