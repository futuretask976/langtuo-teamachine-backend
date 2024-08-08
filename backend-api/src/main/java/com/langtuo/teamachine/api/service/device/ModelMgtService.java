package com.langtuo.teamachine.api.service.device;

import com.langtuo.teamachine.api.model.device.ModelDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.device.ModelPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface ModelMgtService {
    /**
     *
     * @param modelCode
     * @return
     */
    LangTuoResult<ModelDTO> get(String modelCode);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<ModelDTO>> search(String modelCode, int pageNum, int pageSize);

    /**
     *
     * @return
     */
    LangTuoResult<List<ModelDTO>> list();

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(ModelPutRequest request);

    /**
     *
     * @param modelCode
     * @return
     */
    LangTuoResult<Void> delete(String modelCode);
}
