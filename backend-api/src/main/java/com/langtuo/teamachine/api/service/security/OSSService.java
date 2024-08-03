package com.langtuo.teamachine.api.service.security;

import com.langtuo.teamachine.api.model.security.OSSTokenDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;

public interface OSSService {
    /**
     *
     * @return
     */
    LangTuoResult<OSSTokenDTO> getOSSToken();
}
