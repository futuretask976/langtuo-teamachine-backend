package com.gx.sp3.demo.biz.service.impl.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineDTO;
import com.gx.sp3.demo.api.result.GxResult;
import com.gx.sp3.demo.api.service.langtuo.MachineService;
import com.gx.sp3.demo.dao.accessor.langtuo.MachineMapperAccessor;
import com.gx.sp3.demo.dao.pojo.langtuo.MachinePojo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineServiceImpl implements MachineService {
    @Resource
    private MachineMapperAccessor accessor;

    @Override
    public GxResult<List<MachineDTO>> list() {
        List<MachinePojo> list = accessor.list();

        GxResult<List<MachineDTO>> gxResult = GxResult.success(list.stream()
                .map(item -> convert(item))
                .collect(Collectors.toList()));
        return gxResult;
    }

    @Override
    public GxResult<MachineDTO> get(String machineCode) {
        GxResult<MachineDTO> gxResult = GxResult.success(
                convert(accessor.get(machineCode)));
        return gxResult;
    }

    private MachineDTO convert(MachinePojo pojo) {
        if (pojo == null) {
            return null;
        }

        MachineDTO dto = new MachineDTO();
        dto.setId(pojo.getId());
        dto.setGmtCreated(pojo.getGmtCreated());
        dto.setGmtModified(pojo.getGmtModified());
        dto.setMachineCode(pojo.getMachineCode());
        dto.setMachineName(pojo.getMachineName());
        dto.setExtraInfoMap(pojo.getExtraInfoMap());
        return dto;
    }
}
