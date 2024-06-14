package com.gx.sp3.demo.api.service.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineDTO;
import com.gx.sp3.demo.api.model.langtuo.MachineTeaOrderDTO;
import com.gx.sp3.demo.api.result.GxResult;

import java.util.List;

public interface MachineTeaOrderService {
    /**
     *
     * @return
     */
    GxResult<List<MachineTeaOrderDTO>> list();

    /**
     *
     * @param orderId
     * @return
     */
    GxResult<MachineTeaOrderDTO> get(String orderId);
}
