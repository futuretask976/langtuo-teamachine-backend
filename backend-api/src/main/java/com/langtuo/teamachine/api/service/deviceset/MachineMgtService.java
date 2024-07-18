package com.langtuo.teamachine.api.service.deviceset;

import com.langtuo.teamachine.api.model.deviceset.MachineDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.deviceset.MachineActivatePutRequest;
import com.langtuo.teamachine.api.request.deviceset.MachineUpdatePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface MachineMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<MachineDTO> get(String tenantCode, String machineCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<MachineDTO>> search(String tenantCode, String screenCode, String elecBoardCode,
            String modelCode, String shopName, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<MachineDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> activate(MachineActivatePutRequest request);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> update(MachineUpdatePutRequest request);

    /**
     *
     * @param tenantCode
     * @param machineCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String machineCode);
}
