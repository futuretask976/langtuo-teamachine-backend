package com.langtuo.teamachine.api.service.record;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.OrderActRecordDTO;
import com.langtuo.teamachine.api.request.record.InvalidActRecordPutRequest;
import com.langtuo.teamachine.api.request.record.OrderActRecordPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;

import java.util.List;

public interface OrderActRecordMgtService {
    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    TeaMachineResult<OrderActRecordDTO> get(String tenantCode, String idempotentMark);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<OrderActRecordDTO>> search(String tenantCode, List<String> shopGroupCodeList,
            List<String> shopCodeList, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    TeaMachineResult<Void> delete(String tenantCode, String idempotentMark);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(OrderActRecordPutRequest request);
}
