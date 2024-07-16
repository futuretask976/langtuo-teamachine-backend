package com.langtuo.teamachine.api.service;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.SeriesDTO;
import com.langtuo.teamachine.api.request.SeriesPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface SeriesMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<SeriesDTO> getByCode(String tenantCode, String seriesCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<SeriesDTO> getByName(String tenantCode, String seriesName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<SeriesDTO>> search(String tenantCode, String seriesCode, String seriesName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<SeriesDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(SeriesPutRequest request);

    /**
     *
     * @param tenantCode
     * @param seriesCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String seriesCode);
}
