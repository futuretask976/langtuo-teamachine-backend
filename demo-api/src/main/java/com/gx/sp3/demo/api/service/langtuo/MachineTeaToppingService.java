package com.gx.sp3.demo.api.service.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineTeaToppingDTO;
import com.gx.sp3.demo.api.result.GxResult;

import java.util.List;

public interface MachineTeaToppingService {
    /**
     *
     * @return
     */
    GxResult<List<MachineTeaToppingDTO>> list();

    /**
     *
     * @param machineCode
     * @return
     */
    GxResult<MachineTeaToppingDTO> get(String machineCode, String teaCode, String toppingCode);
}
