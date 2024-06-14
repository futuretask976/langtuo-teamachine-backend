package com.gx.sp3.demo.biz.service.impl.langtuo;

import com.gx.sp3.demo.api.model.langtuo.MachineToppingDTO;
import com.gx.sp3.demo.api.result.GxResult;
import com.gx.sp3.demo.api.service.langtuo.MachineToppingService;
import com.gx.sp3.demo.dao.accessor.langtuo.MachineToppingMapperAccessor;
import com.gx.sp3.demo.dao.pojo.langtuo.MachineToppingPojo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineToppingServiceImpl implements MachineToppingService {
    @Resource
    private MachineToppingMapperAccessor accessor;

    @Override
    public GxResult<List<MachineToppingDTO>> list() {
        List<MachineToppingPojo> list = accessor.list();

        GxResult<List<MachineToppingDTO>> gxResult = GxResult.success(list.stream()
                .map(item -> convert(item))
                .collect(Collectors.toList()));
        return gxResult;
    }

    @Override
    public GxResult<MachineToppingDTO> get(String machineCode, String toppingCode) {
        GxResult<MachineToppingDTO> gxResult = GxResult.success(
                convert(accessor.get(machineCode, toppingCode)));
        return gxResult;
    }

    private MachineToppingDTO convert(MachineToppingPojo pojo) {
        if (pojo == null) {
            return null;
        }

        MachineToppingDTO dto = new MachineToppingDTO();
        dto.setId(pojo.getId());
        dto.setGmtCreated(pojo.getGmtCreated());
        dto.setGmtModified(pojo.getGmtModified());
        dto.setMachineCode(pojo.getMachineCode());
        dto.setToppingCode(pojo.getToppingCode());
        dto.setToppingName(pojo.getToppingName());
        dto.setToppingImgLink(pojo.getToppingImgLink());
        dto.setExtraInfoMap(pojo.getExtraInfoMap());
        return dto;
    }
}
