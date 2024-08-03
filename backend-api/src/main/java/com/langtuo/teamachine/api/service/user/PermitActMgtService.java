package com.langtuo.teamachine.api.service.user;

import com.langtuo.teamachine.api.model.user.PermitActGroupDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface PermitActMgtService {
    /**
     *
     * @return
     */
    LangTuoResult<List<PermitActGroupDTO>> list();
}
