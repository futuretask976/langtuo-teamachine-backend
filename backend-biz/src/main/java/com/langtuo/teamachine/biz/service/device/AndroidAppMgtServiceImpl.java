package com.langtuo.teamachine.biz.service.device;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.device.AndroidAppDTO;
import com.langtuo.teamachine.api.model.device.AndroidAppDispatchDTO;
import com.langtuo.teamachine.api.request.device.AndroidAppDispatchPutRequest;
import com.langtuo.teamachine.api.request.device.AndroidAppPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.AndroidAppMgtService;
import com.langtuo.teamachine.biz.aync.AsyncDispatcher;
import com.langtuo.teamachine.dao.accessor.device.AndroidAppAccessor;
import com.langtuo.teamachine.dao.accessor.device.AndroidAppDispatchAccessor;
import com.langtuo.teamachine.dao.po.device.AndroidAppDispatchPO;
import com.langtuo.teamachine.dao.po.device.AndroidAppPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AndroidAppMgtServiceImpl implements AndroidAppMgtService {
    @Resource
    private AndroidAppAccessor androidAppAccessor;

    @Resource
    private AndroidAppDispatchAccessor androidAppDispatchAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;

    @Override
    public TeaMachineResult<AndroidAppDTO> get(String version) {
        TeaMachineResult<AndroidAppDTO> teaMachineResult;
        try {
            AndroidAppPO po = androidAppAccessor.getByVersion(version);
            teaMachineResult = TeaMachineResult.success(convertToAndroidAppDTO(po));
        } catch (Exception e) {
            log.error("androidAppMgtService|get|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<AndroidAppDTO>> search(String version, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<AndroidAppDTO>> teaMachineResult;
        try {
            PageInfo<AndroidAppPO> pageInfo = androidAppAccessor.search(version, pageNum, pageSize);
            List<AndroidAppDTO> dtoList = convertToAndroidAppDTO(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("androidAppMgtService|search|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(AndroidAppPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        AndroidAppPO po = convertToAndroidAppPO(request);
        if (request.isNewPut()) {
            return putNew(po);
        } else {
            return putUpdate(po);
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String version) {
        if (StringUtils.isBlank(version)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = androidAppAccessor.deleteByVersion(version);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("androidAppMgtService|delete|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> putDispatch(AndroidAppDispatchPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }
        List<AndroidAppDispatchPO> poList = convert(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = androidAppDispatchAccessor.deleteByVersion(request.getTenantCode(),
                    request.getVersion());
            poList.forEach(po -> {
                androidAppDispatchAccessor.insert(po);
            });
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("androidAppMgtService|putDispatch|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }

        // 异步发送消息准备配置信息分发
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_ANDROID_APP_DISPATCHED);
        jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, request.getTenantCode());
        jsonPayload.put(CommonConsts.JSON_KEY_VERSION, request.getVersion());
        asyncDispatcher.dispatch(jsonPayload);

        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<AndroidAppDispatchDTO> getDispatchByVersion(String tenantCode, String version) {
        TeaMachineResult<AndroidAppDispatchDTO> teaMachineResult;
        try {
            AndroidAppDispatchDTO dto = new AndroidAppDispatchDTO();
            dto.setVersion(version);

            List<AndroidAppDispatchPO> poList = androidAppDispatchAccessor.listByVersion(tenantCode,
                    version);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("androidAppMgtService|getDispatchByVersion|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    private TeaMachineResult<Void> putNew(AndroidAppPO po) {
        TeaMachineResult<Void> teaMachineResult;
        try {
            AndroidAppPO exist = androidAppAccessor.getByVersion(po.getVersion());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = androidAppAccessor.insert(po);
            if (inserted != CommonConsts.NUM_ONE) {
                log.error("androidAppMgtService|putNew|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("androidAppMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(AndroidAppPO po) {
        try {
            AndroidAppPO exist = androidAppAccessor.getByVersion(po.getVersion());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = androidAppAccessor.update(po);
            if (updated != CommonConsts.NUM_ONE) {
                log.error("androidAppMgtService|putUpdate|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("androidAppMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    private List<AndroidAppDTO> convertToAndroidAppDTO(List<AndroidAppPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream()
                .map(this::convertToAndroidAppDTO)
                .collect(Collectors.toList());
    }

    private AndroidAppDTO convertToAndroidAppDTO(AndroidAppPO po) {
        if (po == null) {
            return null;
        }

        AndroidAppDTO dto = new AndroidAppDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setVersion(po.getVersion());
        dto.setOssPath(po.getOssPath());
        dto.setComment(po.getComment());
        return dto;
    }

    private AndroidAppPO convertToAndroidAppPO(AndroidAppPutRequest request) {
        if (request == null) {
            return null;
        }

        AndroidAppPO po = new AndroidAppPO();
        po.setExtraInfo(request.getExtraInfo());
        po.setVersion(request.getVersion());
        po.setOssPath(request.getOssPath());
        po.setComment(request.getComment());
        return po;
    }

    private List<AndroidAppDispatchPO> convert(AndroidAppDispatchPutRequest request) {
        String tenantCode = request.getTenantCode();
        String version = request.getVersion();

        return request.getShopGroupCodeList().stream()
                .map(shopGroupCode -> {
                    AndroidAppDispatchPO po = new AndroidAppDispatchPO();
                    po.setTenantCode(tenantCode);
                    po.setVersion(version);
                    po.setShopGroupCode(shopGroupCode);
                    return po;
                }).collect(Collectors.toList());
    }
}
