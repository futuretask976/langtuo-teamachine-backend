package com.langtuo.teamachine.api.service.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.TeaTypeDTO;
import com.langtuo.teamachine.api.request.drink.TeaTypePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

import java.util.List;

public interface TeaTypeMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<TeaTypeDTO> getByCode(String tenantCode, String teaTypeCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<TeaTypeDTO> getByName(String tenantCode, String teaTypeName);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<TeaTypeDTO>> search(String tenantCode, String teaTypeCode, String teaTypeName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<TeaTypeDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(TeaTypePutRequest request);

    /**
     *
     * @param tenantCode
     * @param teaTypeCode
     * @return
     */
    TeaMachineResult<Void> delete(String tenantCode, String teaTypeCode);
}
