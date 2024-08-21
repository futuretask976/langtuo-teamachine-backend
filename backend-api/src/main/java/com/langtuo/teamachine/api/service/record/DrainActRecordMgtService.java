package com.langtuo.teamachine.api.service.record;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.DrainActRecordDTO;
import com.langtuo.teamachine.api.request.record.DrainActRecordPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

import java.util.List;

public interface DrainActRecordMgtService {
    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    TeaMachineResult<DrainActRecordDTO> get(String tenantCode, String idempotentMark);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<DrainActRecordDTO>> search(String tenantCode, List<String> shopGroupCodeList,
            List<String> shopCodeList, int pageNum, int pageSize);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(DrainActRecordPutRequest request);

    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    TeaMachineResult<Void> delete(String tenantCode, String idempotentMark);
}
