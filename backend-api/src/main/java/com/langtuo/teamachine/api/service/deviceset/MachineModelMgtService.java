package com.langtuo.teamachine.api.service.deviceset;

import com.langtuo.teamachine.api.model.deviceset.MachineModelDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.deviceset.MachineModelPutRequest;
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
     * @return
     */
    LangTuoResult<PageDTO<MachineModelDTO>> list();

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(MachineModelPutRequest request);

    /**
     *
     * @param modelCode
     * @return
     */
    LangTuoResult<Void> delete(String modelCode);
}
