package com.langtuo.teamachine.api.service.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.TeaDTO;
import com.langtuo.teamachine.api.request.drink.TeaPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface TeaMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<TeaDTO> getByCode(String tenantCode, String teaCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<TeaDTO> getByName(String tenantCode, String teaName);

    /**
     *
     * @return
     */
    LangTuoResult<PageDTO<TeaDTO>> search(String tenantCode, String teaCode, String teaName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<List<TeaDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    LangTuoResult<Void> put(TeaPutRequest request);

    /**
     *
     * @param tenantCode
     * @param teaCode
     * @return
     */
    LangTuoResult<Void> delete(String tenantCode, String teaCode);

    /**
     *
     * @param tenantCode
     * @param teaTypeCode
     * @return
     */
    LangTuoResult<Integer> countByTeaTypeCode(String tenantCode, String teaTypeCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    LangTuoResult<XSSFWorkbook> exportByExcel(String tenantCode);
}
