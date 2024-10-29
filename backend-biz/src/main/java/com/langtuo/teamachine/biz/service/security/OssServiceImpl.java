package com.langtuo.teamachine.biz.service.security;

import com.langtuo.teamachine.api.model.security.OssTokenDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.security.OssService;
import com.langtuo.teamachine.dao.oss.OssUtils;
import com.langtuo.teamachine.dao.po.security.OssToken;
import org.springframework.stereotype.Component;

@Component
public class OssServiceImpl implements OssService {
    @Override
    public TeaMachineResult<OssTokenDTO> getOssToken() {
        OssToken po = OssUtils.getSTS();

        OssTokenDTO dto = new OssTokenDTO();
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
