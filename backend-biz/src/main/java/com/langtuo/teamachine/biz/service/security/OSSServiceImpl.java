package com.langtuo.teamachine.biz.service.security;

import com.langtuo.teamachine.api.model.security.OSSTokenDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.security.OSSService;
import com.langtuo.teamachine.dao.oss.OSSUtils;
import com.langtuo.teamachine.dao.po.security.OSSTokenPO;
import org.springframework.stereotype.Component;

@Component
public class OSSServiceImpl implements OSSService {
    @Override
    public TeaMachineResult<OSSTokenDTO> getOSSToken() {
        OSSTokenPO po = OSSUtils.getSTS();

        OSSTokenDTO dto = new OSSTokenDTO();
        dto.setRegion(po.getRegion());
        dto.setBucketName(po.getBucketName());
        dto.setAccessKeyId(po.getAccessKeyId());
        dto.setAccessKeySecret(po.getAccessKeySecret());
        dto.setSecurityToken(po.getSecurityToken());
        dto.setRequestId(po.getRequestId());
        dto.setExpiration(po.getExpiration());
        return TeaMachineResult.success(dto);
    }
}
