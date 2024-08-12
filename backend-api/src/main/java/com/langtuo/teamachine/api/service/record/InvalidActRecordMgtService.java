package com.langtuo.teamachine.api.service.record;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.InvalidActRecordDTO;
import com.langtuo.teamachine.api.request.record.InvalidActRecordPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface InvalidActRecordMgtService {
    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    LangTuoResult<InvalidActRecordDTO> get(String tenantCode, String idempotentMark);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<InvalidActRecordDTO>> search(String tenantCode, String shopGroupCode, String shopCode,
            int pageNum, int pageSize);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(InvalidActRecordPutRequest request);

    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String idempotentMark);
}
