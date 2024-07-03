package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.MachineDeployDTO;
import com.langtuo.teamachine.api.model.MachineModelDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.MachineDeployPutRequest;
import com.langtuo.teamachine.api.request.MachineModelPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

public interface MachineDeployMgtService {
    /**
     *
     * @param tenantCode
     * @param deployCode
     * @return
     */
    LangTuoResult<MachineDeployDTO> get(String tenantCode, String deployCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<MachineDeployDTO>> search(String tenantCode, String deployCode, String shopName, String state,
            int pageNum, int pageSize);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<MachineDeployDTO>> list();

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(MachineDeployPutRequest request);

    /**
     *
     * @param tenantCode
     * @param deployCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String deployCode);
}
