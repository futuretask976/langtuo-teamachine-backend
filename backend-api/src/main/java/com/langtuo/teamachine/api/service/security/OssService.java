package com.langtuo.teamachine.api.service.security;

import com.langtuo.teamachine.api.model.security.OssTokenDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;

public interface OssService {
    /**
     *
     * @return
     */
    TeaMachineResult<OssTokenDTO> getOssToken();
}
