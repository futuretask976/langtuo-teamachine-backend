package com.gx.sp3.demo.api.service.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineDTO;
import com.gx.sp3.demo.api.result.GxResult;

import java.util.List;

public interface MachineService {
    /**
     *
     * @return
     */
    GxResult<List<MachineDTO>> list();

    /**
     *
     * @param machineCode
     * @return
     */
    GxResult<MachineDTO> get(String machineCode);
}
