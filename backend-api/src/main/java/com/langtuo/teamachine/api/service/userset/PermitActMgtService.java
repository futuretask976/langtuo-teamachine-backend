package com.langtuo.teamachine.api.service.userset;

import com.langtuo.teamachine.api.model.userset.PermitActGroupDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface PermitActMgtService {
    /**
     *
     * @return
     */
    LangTuoResult<List<PermitActGroupDTO>> list();
}
