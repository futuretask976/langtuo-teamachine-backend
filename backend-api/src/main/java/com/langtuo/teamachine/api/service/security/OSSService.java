package com.langtuo.teamachine.api.service.security;

import com.langtuo.teamachine.api.model.security.OSSTokenDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;

public interface OSSService {
    /**
     *
     * @return
     */
    TeaMachineResult<OSSTokenDTO> getOSSToken();
}
