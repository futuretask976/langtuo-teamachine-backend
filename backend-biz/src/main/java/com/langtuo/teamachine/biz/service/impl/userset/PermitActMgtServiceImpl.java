package com.langtuo.teamachine.biz.service.impl.userset;

import com.google.common.collect.Maps;
import com.langtuo.teamachine.api.constant.PermitActEnum;
import com.langtuo.teamachine.api.constant.PermitActGroupEnum;
import com.langtuo.teamachine.api.model.userset.PermitActDTO;
import com.langtuo.teamachine.api.model.userset.PermitActGroupDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.userset.PermitActMgtService;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermitActMgtServiceImpl implements PermitActMgtService {
    private static Map<PermitActGroupEnum, List<PermitActEnum>> permitActGroupMap = Maps.newHashMap();
    static {
        for (PermitActEnum permitActEnum : PermitActEnum.values()) {
            String permitActGroupCode = permitActEnum.getPermitActGroupCode();
            PermitActGroupEnum permitActGroupEnum = PermitActGroupEnum.valueOfByCode(permitActGroupCode);
            if (!permitActGroupMap.containsKey(permitActGroupEnum)) {
                permitActGroupMap.put(permitActGroupEnum, com.google.common.collect.Lists.newArrayList());
            }
            permitActGroupMap.get(permitActGroupEnum).add(permitActEnum);
        }
    }

    @Override
    public LangTuoResult<List<PermitActGroupDTO>> list() {
        List<PermitActGroupDTO> resultModel = Lists.newArrayList();
        Set<Map.Entry<PermitActGroupEnum, List<PermitActEnum>>> entrySet = permitActGroupMap.entrySet();
        for (Map.Entry<PermitActGroupEnum, List<PermitActEnum>> entry : entrySet) {
            PermitActGroupEnum permitActGroupEnum = entry.getKey();
            List<PermitActEnum> permitActEnumList = entry.getValue();
            PermitActGroupDTO permitActGroupDTO = convert(permitActGroupEnum);
            List<PermitActDTO> PermitActDTOList = convert(permitActEnumList);
            permitActGroupDTO.setPermitActList(PermitActDTOList);
            resultModel.add(permitActGroupDTO);
        }
        return LangTuoResult.success(resultModel);
    }

    private PermitActGroupDTO convert(PermitActGroupEnum permitActGroupEnum) {
        if (permitActGroupEnum == null) {
            return null;
        }

        PermitActGroupDTO dto = new PermitActGroupDTO();
        dto.setPermitActGroupCode(permitActGroupEnum.getPermitActGroupCode());
        dto.setPermitActGroupName(permitActGroupEnum.getPermitActGroupName());
        return dto;
    }

    private List<PermitActDTO> convert(List<PermitActEnum> permitActEnumList) {
        if (CollectionUtils.isEmpty(permitActEnumList)) {
            return null;
        }

        List<PermitActDTO> list = permitActEnumList.stream()
                .map(permitActEnum -> {
                    PermitActDTO dto = new PermitActDTO();
                    dto.setPermitActCode(permitActEnum.getPermitActCode());
                    dto.setPermitActName(permitActEnum.getPermitActName());
                    dto.setPermitActGroupCode(permitActEnum.getPermitActGroupCode());
                    return dto;
                })
                .collect(Collectors.toList());
        return list;
    }
}
