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
import com.langtuo.teamachine.internal.util.LocaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.biz.convert.device.AndroidAppMgtConvertor.*;

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
    @Transactional(readOnly = true)
    public TeaMachineResult<List<AndroidAppDTO>> listByLimit(int limit) {
        try {
            List<AndroidAppPO> list = androidAppAccessor.listByLimit(limit);
            List<AndroidAppDTO> dtoList = convertToAndroidAppDTO(list);
            dtoList.sort((o1, o2) -> o1.getGmtModified().equals(o2.getGmtModified()) ?
                    0 : o1.getGmtModified().before(o2.getGmtModified()) ? 1 : -1);
            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("|androidAppMgtService|listByLimit|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<AndroidAppDTO> getByVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            AndroidAppPO po = androidAppAccessor.getByVersion(version);
            return TeaMachineResult.success(convertToAndroidAppDTO(po));
        } catch (Exception e) {
            log.error("|androidAppMgtService|get|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<PageDTO<AndroidAppDTO>> search(String version, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<AndroidAppPO> pageInfo = androidAppAccessor.search(version, pageNum, pageSize);
            List<AndroidAppDTO> dtoList = convertToAndroidAppDTO(pageInfo.getList());
            return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("|androidAppMgtService|search|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(AndroidAppPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        AndroidAppPO po = convertToAndroidAppPO(request);
        try {
            if (request.isPutNew()) {
                return doPutNew(po);
            } else {
                return doPutUpdate(po);
            }
        } catch (Exception e) {
            log.error("|androidAppMgtService|put|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String version) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(version)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            return doDelete(tenantCode, version);
        } catch (Exception e) {
            log.error("|androidAppMgtService|delete|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> putDispatch(AndroidAppDispatchPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }
        List<AndroidAppDispatchPO> poList = convertToAndroidAppDispatchPO(request);

        try {
            TeaMachineResult<Void> result = doPutDispatch(request.getTenantCode(), request.getVersion(), poList);

            // 异步发送消息准备配置信息分发
            JSONObject jsonPayload = new JSONObject();
            jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_ANDROID_APP_DISPATCHED);
            jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, request.getTenantCode());
            jsonPayload.put(CommonConsts.JSON_KEY_VERSION, request.getVersion());
            asyncDispatcher.dispatch(jsonPayload);

            return result;
        } catch (Exception e) {
            log.error("|androidAppMgtService|putDispatch|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<AndroidAppDispatchDTO> getDispatchByVersion(String tenantCode, String version) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(version)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            return doGetDispatchByVersion(tenantCode, version);
        } catch (Exception e) {
            log.error("|androidAppMgtService|getDispatchByVersion|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Transactional(readOnly = true)
    private TeaMachineResult<AndroidAppDispatchDTO> doGetDispatchByVersion(String tenantCode, String version) {
        AndroidAppDispatchDTO dto = new AndroidAppDispatchDTO();
        dto.setVersion(version);

        List<AndroidAppDispatchPO> poList = androidAppDispatchAccessor.listByVersion(tenantCode,
                version);
        if (!CollectionUtils.isEmpty(poList)) {
            dto.setShopGroupCodeList(poList.stream()
                    .map(po -> po.getShopGroupCode())
                    .collect(Collectors.toList()));
        }

        return TeaMachineResult.success(dto);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doDelete(String tenantCode, String version) {
        int deleted4App = androidAppAccessor.deleteByVersion(version);
        int deleted4Dispatch = androidAppDispatchAccessor.deleteByVersion(tenantCode, version);
        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutDispatch(String tenantCode, String version,
            List<AndroidAppDispatchPO> poList) {
        int deleted = androidAppDispatchAccessor.deleteByVersion(tenantCode, version);
        for (AndroidAppDispatchPO po : poList) {
            androidAppDispatchAccessor.insert(po);
        }
        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutNew(AndroidAppPO po) {
        AndroidAppPO exist = androidAppAccessor.getByVersion(po.getVersion());
        if (exist != null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
        }

        int inserted = androidAppAccessor.insert(po);
        if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
            log.error("|androidAppMgtService|doPutNew|error|" + inserted);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutUpdate(AndroidAppPO po) {
        AndroidAppPO exist = androidAppAccessor.getByVersion(po.getVersion());
        if (exist == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
        }

        int updated = androidAppAccessor.update(po);
        if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
            log.error("|androidAppMgtService|doPutUpdate|error|" + updated);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
        return TeaMachineResult.success();
    }
}
