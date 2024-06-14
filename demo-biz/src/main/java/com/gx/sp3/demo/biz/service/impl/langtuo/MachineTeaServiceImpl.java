package com.gx.sp3.demo.biz.service.impl.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineTeaDTO;
import com.gx.sp3.demo.api.result.GxResult;
import com.gx.sp3.demo.api.service.langtuo.MachineTeaService;
import com.gx.sp3.demo.dao.accessor.langtuo.MachineTeaMapperAccessor;
import com.gx.sp3.demo.dao.pojo.langtuo.MachineTeaPojo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineTeaServiceImpl implements MachineTeaService {
    @Resource
    private MachineTeaMapperAccessor accessor;

    @Override
    public GxResult<List<MachineTeaDTO>> list() {
        List<MachineTeaPojo> list = accessor.list();

        GxResult<List<MachineTeaDTO>> gxResult = GxResult.success(list.stream()
                .map(item -> convert(item))
                .collect(Collectors.toList()));
        return gxResult;
    }

    @Override
    public GxResult<MachineTeaDTO> get(String machineCode, String teaCode) {
        GxResult<MachineTeaDTO> gxResult = GxResult.success(
                convert(accessor.get(machineCode, teaCode)));
        return gxResult;
    }

    private MachineTeaDTO convert(MachineTeaPojo pojo) {
        if (pojo == null) {
            return null;
        }

        MachineTeaDTO dto = new MachineTeaDTO();
        dto.setId(pojo.getId());
        dto.setGmtCreated(pojo.getGmtCreated());
        dto.setGmtModified(pojo.getGmtModified());
        dto.setMachineCode(pojo.getMachineCode());
        dto.setTeaCode(pojo.getTeaCode());
        dto.setTeaName(pojo.getTeaName());
        dto.setTeaImgLink(pojo.getTeaImgLink());
        dto.setExtraInfoMap(pojo.getExtraInfoMap());
        return dto;
    }
}
