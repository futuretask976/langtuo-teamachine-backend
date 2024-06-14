package com.gx.sp3.demo.api.service.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineToppingDTO;
import com.gx.sp3.demo.api.result.GxResult;

import java.util.List;

public interface MachineToppingService {
    /**
     *
     * @return
     */
    GxResult<List<MachineToppingDTO>> list();

    /**
     *
     * @param machineCode
     * @return
     */
    GxResult<MachineToppingDTO> get(String machineCode, String toppingCode);
}
