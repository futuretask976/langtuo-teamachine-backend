package com.langtuo.teamachine.api.service.recordset;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.recordset.CleanActRecordDTO;
import com.langtuo.teamachine.api.request.recordset.CleanActRecordPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;

import java.util.List;

public interface CleanActRecordMgtService {
    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    LangTuoResult<CleanActRecordDTO> get(String tenantCode, String idempotentMark);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<CleanActRecordDTO>> search(String tenantCode, String shopGroupCode, String shopCode,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<CleanActRecordDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(CleanActRecordPutRequest request);

    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String idempotentMark);
}
