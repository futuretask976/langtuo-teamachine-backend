package com.langtuo.teamachine.biz.service.impl.device;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.device.AndroidAppDTO;
import com.langtuo.teamachine.api.request.device.AndroidAppPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.AndroidAppMgtService;
import com.langtuo.teamachine.biz.service.aync.AsyncDispatcher;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.biz.service.util.MessageUtils;
import com.langtuo.teamachine.dao.accessor.device.AndroidAppAccessor;
import com.langtuo.teamachine.dao.po.device.AndroidAppPO;
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
    private AsyncDispatcher asyncDispatcher;

    @Override
    public TeaMachineResult<AndroidAppDTO> get(String version) {
        TeaMachineResult<AndroidAppDTO> teaMachineResult;
        try {
            AndroidAppPO po = androidAppAccessor.selectOne(version);
            teaMachineResult = TeaMachineResult.success(convertToAndroidAppDTO(po));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<AndroidAppDTO>> search(String version, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<AndroidAppDTO>> teaMachineResult;
        try {
            PageInfo<AndroidAppPO> pageInfo = androidAppAccessor.search(version, pageNum, pageSize);
            List<AndroidAppDTO> dtoList = convertToAndroidAppDTO(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
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

        TeaMachineResult<Void> teaMachineResult;
        try {
            AndroidAppPO exist = androidAppAccessor.selectOne(po.getVersion());
            if (exist != null) {
                int updated = androidAppAccessor.update(po);
            } else {
                int inserted = androidAppAccessor.insert(po);
            }
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
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
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> dispatch(String version) {
        if (StringUtils.isBlank(version)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        // 异步发送消息准备配置信息分发
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(BizConsts.JSON_KEY_BIZ_CODE, BizConsts.BIZ_CODE_ANDROID_APP_DISPATCHED);
        jsonPayload.put(BizConsts.JSON_KEY_ANDROID_APP_VER, version);
        asyncDispatcher.dispatch(jsonPayload);

        return TeaMachineResult.success();
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
}
