package com.langtuo.teamachine.api.service.userset;

import com.langtuo.teamachine.api.model.userset.OrgDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.userset.OrgPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface OrgMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<OrgDTO> get(String tenantCode, String orgName);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<OrgDTO> listByDepth(String tenantCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<OrgDTO>> list(String tenantCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<OrgDTO>> search(String tenantCode, String contactPerson, int pageNum, int pageSize);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(OrgPutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String orgName);
}
