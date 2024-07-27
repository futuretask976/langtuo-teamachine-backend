package com.langtuo.teamachine.api.service.deviceset;

import com.langtuo.teamachine.api.model.deviceset.ModelDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.deviceset.ModelPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

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
    LangTuoResult<PageDTO<ModelDTO>> list();

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
