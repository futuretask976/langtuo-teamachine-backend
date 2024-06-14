package com.gx.sp3.demo.biz.service.impl.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineTeaOrderDTO;
import com.gx.sp3.demo.api.result.GxResult;
import com.gx.sp3.demo.api.service.langtuo.MachineTeaOrderService;
import com.gx.sp3.demo.dao.accessor.langtuo.MachineTeaOrderMapperAccessor;
import com.gx.sp3.demo.dao.pojo.langtuo.MachineTeaOrderPojo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineTeaOrderServiceImpl implements MachineTeaOrderService {
    @Resource
    private MachineTeaOrderMapperAccessor accessor;

    @Override
    public GxResult<List<MachineTeaOrderDTO>> list() {
        List<MachineTeaOrderPojo> list = accessor.list();

        GxResult<List<MachineTeaOrderDTO>> gxResult = GxResult.success(list.stream()
                .map(item -> convert(item))
                .collect(Collectors.toList()));
        return gxResult;
    }

    @Override
    public GxResult<MachineTeaOrderDTO> get(String orderId) {
        GxResult<MachineTeaOrderDTO> gxResult = GxResult.success(
                convert(accessor.get(orderId)));
        return gxResult;
    }

    private MachineTeaOrderDTO convert(MachineTeaOrderPojo pojo) {
        if (pojo == null) {
            return null;
        }

        MachineTeaOrderDTO dto = new MachineTeaOrderDTO();
        dto.setId(pojo.getId());
        dto.setGmtCreated(pojo.getGmtCreated());
        dto.setGmtModified(pojo.getGmtModified());
        dto.setOrderId(pojo.getOrderId());
        dto.setMachineCode(pojo.getMachineCode());
        dto.setTeaCode(pojo.getTeaCode());
        dto.setExtraInfoMap(pojo.getExtraInfoMap());
        return dto;
    }
}
