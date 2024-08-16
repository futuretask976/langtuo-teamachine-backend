package com.langtuo.teamachine.api.service.device;

import com.langtuo.teamachine.api.model.device.DeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.device.DeployPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface DeployMgtService {
    /**
     *
     * @param tenantCode
     * @param deployCode
     * @return
     */
    LangTuoResult<DeployDTO> getByDeployCode(String tenantCode, String deployCode);

    /**
     *
     * @param tenantCode
     * @param machineCode
     * @return
     */
    LangTuoResult<DeployDTO> getByMachineCode(String tenantCode, String machineCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<DeployDTO>> search(String tenantCode, String deployCode, String machineCode,
            String shopName, Integer state, int pageNum, int pageSize);

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
    LangTuoResult<String> generateDeployCode();

    /**
     *
     * @return
     */
    LangTuoResult<String> generateMachineCode();
}
