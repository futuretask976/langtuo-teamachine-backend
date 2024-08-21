package com.langtuo.teamachine.api.service.record;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.InvalidActRecordDTO;
import com.langtuo.teamachine.api.request.record.InvalidActRecordPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

import java.util.List;

public interface InvalidActRecordMgtService {
    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    TeaMachineResult<InvalidActRecordDTO> get(String tenantCode, String idempotentMark);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<InvalidActRecordDTO>> search(String tenantCode, List<String> shopGroupCodeList,
            List<String> shopCodeList, int pageNum, int pageSize);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(InvalidActRecordPutRequest request);

    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    TeaMachineResult<Void> delete(String tenantCode, String idempotentMark);
}
