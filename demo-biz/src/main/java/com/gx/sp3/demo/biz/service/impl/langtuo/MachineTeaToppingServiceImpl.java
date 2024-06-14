package com.gx.sp3.demo.biz.service.impl.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineTeaToppingDTO;
import com.gx.sp3.demo.api.result.GxResult;
import com.gx.sp3.demo.api.service.langtuo.MachineTeaToppingService;
import com.gx.sp3.demo.dao.accessor.langtuo.MachineTeaToppingMapperAccessor;
import com.gx.sp3.demo.dao.pojo.langtuo.MachineTeaToppingPojo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineTeaToppingServiceImpl implements MachineTeaToppingService {
    @Resource
    private MachineTeaToppingMapperAccessor accessor;

    @Override
    public GxResult<List<MachineTeaToppingDTO>> list() {
        List<MachineTeaToppingPojo> list = accessor.list();

        GxResult<List<MachineTeaToppingDTO>> gxResult = GxResult.success(list.stream()
                .map(item -> convert(item))
                .collect(Collectors.toList()));
        return gxResult;
    }

    @Override
    public GxResult<MachineTeaToppingDTO> get(String machineCode, String teaCode, String toppingCode) {
        GxResult<MachineTeaToppingDTO> gxResult = GxResult.success(
                convert(accessor.get(machineCode, teaCode, toppingCode)));
        return gxResult;
    }

    private MachineTeaToppingDTO convert(MachineTeaToppingPojo pojo) {
        if (pojo == null) {
            return null;
        }

        MachineTeaToppingDTO dto = new MachineTeaToppingDTO();
        dto.setId(pojo.getId());
        dto.setGmtCreated(pojo.getGmtCreated());
        dto.setGmtModified(pojo.getGmtModified());
        dto.setMachineCode(pojo.getMachineCode());
        dto.setTeaCode(pojo.getTeaCode());
        dto.setToppingCode(pojo.getToppingCode());
        dto.setExtraInfoMap(pojo.getExtraInfoMap());
        return dto;
    }
}
