package com.langtuo.teamachine.api.service.record;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.OrderActRecordDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface OrderActRecordMgtService {
    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    LangTuoResult<OrderActRecordDTO> get(String tenantCode, String idempotentMark);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<OrderActRecordDTO>> search(String tenantCode, List<String> shopGroupCodeList,
            List<String> shopCodeList, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String idempotentMark);
}
