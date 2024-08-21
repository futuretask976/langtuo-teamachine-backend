package com.langtuo.teamachine.api.service.menu;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menu.SeriesDTO;
import com.langtuo.teamachine.api.request.menu.SeriesPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

import java.util.List;

public interface SeriesMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<SeriesDTO> getByCode(String tenantCode, String seriesCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<SeriesDTO> getByName(String tenantCode, String seriesName);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<SeriesDTO>> search(String tenantCode, String seriesCode, String seriesName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<SeriesDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(SeriesPutRequest request);

    /**
     *
     * @param tenantCode
     * @param seriesCode
     * @return
     */
    TeaMachineResult<Void> delete(String tenantCode, String seriesCode);
}
