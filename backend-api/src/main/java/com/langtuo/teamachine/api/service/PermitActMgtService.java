package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.PermitActGroupDTO;
import com.langtuo.teamachine.api.model.TenantDTO;
import com.langtuo.teamachine.api.request.TenantPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface PermitActMgtService {
    /**
     *
     * @return
     */
    LangTuoResult<List<PermitActGroupDTO>> list();
}
