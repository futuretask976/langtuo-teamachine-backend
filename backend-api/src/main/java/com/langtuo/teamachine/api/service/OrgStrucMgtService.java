package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.OrgStrucDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.OrgStrucPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface OrgStrucMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<OrgStrucDTO> get(String tenantCode, String orgName);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<OrgStrucDTO> listByDepth(String tenantCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<OrgStrucDTO>> list(String tenantCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<OrgStrucDTO>> search(String tenantCode, String contactPerson, int pageNum, int pageSize);

    /**
     *
     * @param orgStrucPutRequest
     * @return
     */
    LangTuoResult<Void> put(OrgStrucPutRequest orgStrucPutRequest);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String orgName);
}
