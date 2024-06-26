package com.langtuo.teamachine.biz.service.impl;

import com.google.common.collect.Maps;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PermitActDTO;
import com.langtuo.teamachine.api.model.PermitActGroupDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.PermitActMgtService;
import com.langtuo.teamachine.dao.accessor.PermitActAccessor;
import com.langtuo.teamachine.dao.accessor.PermitActGroupAccessor;
import com.langtuo.teamachine.dao.po.PermitActGroupPO;
import com.langtuo.teamachine.dao.po.PermitActPO;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class PermitActMgtServiceImpl implements PermitActMgtService {
    @Resource
    private PermitActGroupAccessor permitActGroupAccessor;

    @Resource
    private PermitActAccessor permitActAccessor;

    @Override
    public LangTuoResult<List<PermitActGroupDTO>> list() {
        List<PermitActGroupPO> permitActGroupPOList = permitActGroupAccessor.selectList();
        Map<String, PermitActGroupDTO> actGroupCodeMap = Maps.newHashMap();
        permitActGroupPOList.forEach(po -> {
            PermitActGroupDTO dto = convert(po);
            actGroupCodeMap.put(dto.getPermitActGroupCode(), dto);
        });

        List<PermitActPO> permitActPOList = permitActAccessor.selectList();
        permitActPOList.forEach(po -> {
            PermitActDTO dto = convert(po);
            PermitActGroupDTO permitActGroupDTO = actGroupCodeMap.get(po.getPermitActGroupCode());
            if (permitActGroupDTO == null) {
                throw new RuntimeException(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT.getErrorMsg());
            }
            permitActGroupDTO.addPermitActDTO(dto);
        });

        List<PermitActGroupDTO> resultModel = Lists.newArrayList();
        actGroupCodeMap.forEach((key, value) -> {
            resultModel.add(value);
        });
        return LangTuoResult.success(resultModel);
    }

    private PermitActGroupDTO convert(PermitActGroupPO po) {
        if (po == null) {
            return null;
        }

        PermitActGroupDTO dto = new PermitActGroupDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setPermitActGroupCode(po.getPermitActGroupCode());
        dto.setPermitActGroupName(po.getPermitActGroupName());

        return dto;
    }

    private PermitActDTO convert(PermitActPO po) {
        if (po == null) {
            return null;
        }

        PermitActDTO dto = new PermitActDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setPermitActCode(po.getPermitActCode());
        dto.setPermitActName(po.getPermitActName());
        dto.setPermitActGroupCode(po.getPermitActGroupCode());

        return dto;
    }
}
