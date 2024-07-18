package com.langtuo.teamachine.api.service.userset;

import com.langtuo.teamachine.api.model.userset.OrgStrucDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.userset.OrgStrucPutRequest;
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
     * @param request
     * @return
     */
    LangTuoResult<Void> put(OrgStrucPutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String orgName);
}
