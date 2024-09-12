package com.langtuo.teamachine.api.service.device;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.device.AndroidAppDTO;
import com.langtuo.teamachine.api.request.device.AndroidAppPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

import java.util.List;

public interface AndroidAppMgtService {
    /**
     *
     * @param version
     * @return
     */
    TeaMachineResult<AndroidAppDTO> get(String version);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<AndroidAppDTO>> search(String version, int pageNum, int pageSize);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(AndroidAppPutRequest request);

    /**
     *
     * @param version
     * @return
     */
    TeaMachineResult<Void> delete(String version);

    /**
     *
     * @param version
     * @return
     */
    TeaMachineResult<Void> dispatch(String version);
}
