package com.langtuo.teamachine.api.service.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.TeaDTO;
import com.langtuo.teamachine.api.request.drink.TeaPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface TeaMgtService {
    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<TeaDTO> getByCode(String tenantCode, String teaCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<TeaDTO> getByName(String tenantCode, String teaName);

    /**
     *
     * @return
     */
    TeaMachineResult<PageDTO<TeaDTO>> search(String tenantCode, String teaCode, String teaName,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<List<TeaDTO>> list(String tenantCode);

    /**
     *
     * @param request
     * @return
     */
    TeaMachineResult<Void> put(TeaPutRequest request);

    /**
     *
     * @param tenantCode
     * @param teaCode
     * @return
     */
    TeaMachineResult<Void> delete(String tenantCode, String teaCode);

    /**
     *
     * @param tenantCode
     * @param teaTypeCode
     * @return
     */
    TeaMachineResult<Integer> countByTeaTypeCode(String tenantCode, String teaTypeCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    TeaMachineResult<XSSFWorkbook> exportByExcel(String tenantCode);
}
