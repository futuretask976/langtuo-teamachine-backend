package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.MachineModelDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.MachineModelPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

public interface MachineModelMgtService {
    /**
     *
     * @param modelCode
     * @return
     */
    LangTuoResult<MachineModelDTO> get(String modelCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<MachineModelDTO>> search(String modelCode, int pageNum, int pageSize);

    /**
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    LangTuoResult<PageDTO<MachineModelDTO>> list(int pageNum, int pageSize);

    /**
     *
     * @param machineModelPutRequest
     * @return
     */
    LangTuoResult<Void> put(MachineModelPutRequest machineModelPutRequest);

    /**
     *
     * @param modelCode
     * @return
     */
    LangTuoResult<Void> delete(String modelCode);
}
