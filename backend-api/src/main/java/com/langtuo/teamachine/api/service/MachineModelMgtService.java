package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.MachineModelDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.MachineModelRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

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
     * @param machineModelRequest
     * @return
     */
    LangTuoResult<Void> put(MachineModelRequest machineModelRequest);

    /**
     *
     * @param modelCode
     * @return
     */
    LangTuoResult<Void> delete(String modelCode);
}
