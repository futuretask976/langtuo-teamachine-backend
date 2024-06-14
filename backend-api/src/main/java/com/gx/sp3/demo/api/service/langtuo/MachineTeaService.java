package com.gx.sp3.demo.api.service.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineTeaDTO;
import com.gx.sp3.demo.api.result.GxResult;

import java.util.List;

public interface MachineTeaService {
    /**
     *
     * @return
     */
    GxResult<List<MachineTeaDTO>> list();

    /**
     *
     * @param machineCode
     * @param teaCode
     * @return
     */
    GxResult<MachineTeaDTO> get(String machineCode, String teaCode);
}
