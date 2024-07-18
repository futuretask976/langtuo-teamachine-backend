package com.langtuo.teamachine.api.service.deviceset;

import com.langtuo.teamachine.api.model.deviceset.MachineDeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.deviceset.MachineDeployPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

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
    LangTuoResult<PageDTO<MachineDeployDTO>> search(String tenantCode, String deployCode, String machineCode,
            String shopName, Integer state, int pageNum, int pageSize);

    /**
     *
     * @return
     */
    LangTuoResult<List<MachineDeployDTO>> list(String tenantCode);

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

    /**
     *
     * @return
     */
    LangTuoResult<String> genRandomStr();
}
