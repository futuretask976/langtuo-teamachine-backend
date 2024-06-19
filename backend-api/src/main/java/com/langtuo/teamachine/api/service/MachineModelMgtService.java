package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.MachineModelDTO;
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
    LangTuoResult<List<MachineModelDTO>> list();

    /**
     *
     * @param machineModelDTO
     * @return
     */
    LangTuoResult<Void> put(MachineModelDTO machineModelDTO);

    /**
     *
     * @param modelCode
     * @return
     */
    LangTuoResult<Void> delete(String modelCode);
}
