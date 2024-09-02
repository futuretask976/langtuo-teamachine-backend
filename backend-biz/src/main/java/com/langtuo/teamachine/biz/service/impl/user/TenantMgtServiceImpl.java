package com.langtuo.teamachine.biz.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.service.aync.AsyncDispatcher;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.user.TenantDTO;
import com.langtuo.teamachine.api.request.user.TenantPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.TenantMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.ApiUtils;
import com.langtuo.teamachine.dao.accessor.user.TenantAccessor;
import com.langtuo.teamachine.dao.po.user.TenantPO;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TenantMgtServiceImpl implements TenantMgtService {
    @Resource
    private TenantAccessor tenantAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<List<TenantDTO>> list() {
        TeaMachineResult<List<TenantDTO>> teaMachineResult;
        try {
            List<TenantPO> list = tenantAccessor.selectList();
            List<TenantDTO> dtoList = convert(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<TenantDTO>> search(String tenantName, String contactPerson,
            int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<TenantDTO>> teaMachineResult;
        try {
            PageInfo<TenantPO> pageInfo = tenantAccessor.search(tenantName, contactPerson, pageNum, pageSize);
            List<TenantDTO> dtoList = convert(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<TenantDTO> get(String tenantCode) {
        TeaMachineResult<TenantDTO> teaMachineResult;
        try {
            TenantPO tenantPO = tenantAccessor.selectOneByTenantCode(tenantCode);
            TenantDTO tenantDTO = convert(tenantPO);
            teaMachineResult = TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("get error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(TenantPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        String tenantCode = request.getTenantCode();
        TenantPO tenantPO = convert(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            TenantPO exist = tenantAccessor.selectOneByTenantCode(tenantCode);
            if (exist != null) {
                int updated = tenantAccessor.update(tenantPO);
            } else {
                int inserted = tenantAccessor.insert(tenantPO);
            }
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }

        // 异步发送消息准备添加超级租户管理角色和超级租户管理员
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(BizConsts.SEND_KEY_BIZ_CODE, BizConsts.BIZ_CODE_PREPARE_TENANT);
        jsonPayload.put(BizConsts.SEND_KEY_TENANT_CODE, request.getTenantCode());
        asyncDispatcher.dispatch(jsonPayload);

        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = tenantAccessor.deleteByTenantCode(tenantCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    private List<TenantDTO> convert(List<TenantPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<TenantDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private TenantDTO convert(TenantPO po) {
        if (po == null) {
            return null;
        }

        TenantDTO dto = new TenantDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setTenantName(po.getTenantName());
        dto.setContactPerson(po.getContactPerson());
        dto.setContactPhone(po.getContactPhone());
        dto.setComment(po.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return dto;
    }

    private TenantPO convert(TenantPutRequest request) {
        if (request == null) {
            return null;
        }

        TenantPO po = new TenantPO();
        po.setTenantCode(request.getTenantCode());
        po.setTenantName(request.getTenantName());
        po.setContactPerson(request.getContactPerson());
        po.setContactPhone(request.getContactPhone());
        po.setComment(request.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }
}
