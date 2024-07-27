package com.langtuo.teamachine.api.service.deviceset;

import com.langtuo.teamachine.api.model.deviceset.DeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.deviceset.DeployPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface DeployMgtService {
    /**
     *
     * @param tenantCode
     * @param deployCode
     * @return
     */
    LangTuoResult<DeployDTO> get(String tenantCode, String deployCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<DeployDTO>> search(String tenantCode, String deployCode, String machineCode,
            String shopName, Integer state, int pageNum, int pageSize);

    /**
     *
     * @return
     */
    LangTuoResult<List<DeployDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(DeployPutRequest request);

    /**
     *
     * @param tenantCode
     * @param deployCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String deployCode);

    /**
     *
     * @return
     */
    LangTuoResult<String> genRandomStr();
}
