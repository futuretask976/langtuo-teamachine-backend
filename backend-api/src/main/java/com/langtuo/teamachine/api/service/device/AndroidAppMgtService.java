package com.langtuo.teamachine.api.service.device;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.device.AndroidAppDTO;
import com.langtuo.teamachine.api.model.device.AndroidAppDispatchDTO;
import com.langtuo.teamachine.api.model.menu.MenuDispatchDTO;
import com.langtuo.teamachine.api.request.device.AndroidAppDispatchPutRequest;
import com.langtuo.teamachine.api.request.device.AndroidAppPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

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
    TeaMachineResult<Void> delete(String tenantCode, String version);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> putDispatch(AndroidAppDispatchPutRequest request);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<AndroidAppDispatchDTO> getDispatchByVersion(String tenantCode, String version);
}
