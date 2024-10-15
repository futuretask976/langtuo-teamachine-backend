package com.langtuo.teamachine.biz.service.menu;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menu.MenuDTO;
import com.langtuo.teamachine.api.model.menu.MenuDispatchDTO;
import com.langtuo.teamachine.api.request.menu.MenuDispatchPutRequest;
import com.langtuo.teamachine.api.request.menu.MenuPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.menu.MenuMgtService;
import com.langtuo.teamachine.biz.aync.AsyncDispatcher;
import com.langtuo.teamachine.biz.util.BizUtils;
import com.langtuo.teamachine.dao.accessor.menu.MenuAccessor;
import com.langtuo.teamachine.dao.accessor.menu.MenuDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.menu.MenuDispatchCacheAccessor;
import com.langtuo.teamachine.dao.accessor.menu.MenuSeriesRelAccessor;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchPO;
import com.langtuo.teamachine.dao.po.menu.MenuPO;
import com.langtuo.teamachine.dao.po.menu.MenuSeriesRelPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.DateUtils;
import com.langtuo.teamachine.internal.util.LocaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.biz.convert.menu.MenuMgtConvertor.*;

@Component
@Slf4j
public class MenuMgtServiceImpl implements MenuMgtService {
    @Resource
    private MenuAccessor menuAccessor;

    @Resource
    private MenuSeriesRelAccessor menuSeriesRelAccessor;

    @Resource
    private MenuDispatchAccessor menuDispatchAccessor;

    @Resource
    private MenuDispatchCacheAccessor menuDispatchCacheAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<List<MenuDTO>> list(String tenantCode) {
        try {
            List<MenuPO> list = menuAccessor.list(tenantCode);
            List<MenuDTO> dtoList = convertToMenuDTO(list);
            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("menuMgtService|list|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<Void> triggerDispatchByShopGroupCode(String tenantCode, String shopGroupCode,
                String machineCode) {
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_MENU_LIST_REQUESTED);
        jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(CommonConsts.JSON_KEY_SHOP_GROUP_CODE, shopGroupCode);
        jsonPayload.put(CommonConsts.JSON_KEY_MACHINE_CODE, machineCode);
        asyncDispatcher.dispatch(jsonPayload);

        return TeaMachineResult.success();
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<PageDTO<MenuDTO>> search(String tenantName, String seriesCode, String seriesName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<MenuPO> pageInfo = menuAccessor.search(tenantName, seriesCode, seriesName,
                    pageNum, pageSize);
            List<MenuDTO> dtoList = convertToMenuDTO(pageInfo.getList());
            return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("menuMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<MenuDTO> getByMenuCode(String tenantCode, String seriesCode) {
        try {
            MenuPO toppingTypePO = menuAccessor.getByMenuCode(tenantCode, seriesCode);
            MenuDTO seriesDTO = convertToMenuDTO(toppingTypePO);
            return TeaMachineResult.success(seriesDTO);
        } catch (Exception e) {
            log.error("menuMgtService|getByCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(MenuPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        MenuPO po = convertMenuPO(request);
        List<MenuSeriesRelPO> seriesRelPOList = convertToMenuSeriesRelPO(request);
        try {
            if (request.isPutNew()) {
                return doPutNew(po, seriesRelPOList);
            } else {
                return doPutUpdate(po, seriesRelPOList);
            }
        } catch (Exception e) {
            log.error("menuMgtService|put|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> deleteByMenuCode(String tenantCode, String menuCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            return doDeleteByMenuCode(tenantCode, menuCode);
        } catch (Exception e) {
            log.error("menuMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> putDispatch(MenuDispatchPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }
        List<MenuDispatchPO> poList = convertToMenuDispatchPO(request);

        try {
            TeaMachineResult<Void> result = doPutDispatch(request.getTenantCode(), request.getMenuCode(), poList);
            return result;
        } catch (Exception e) {
            log.error("menuMgtService|putDispatch|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<MenuDispatchDTO> getDispatchByMenuCode(String tenantCode, String menuCode) {
        try {
            MenuDispatchDTO dto = new MenuDispatchDTO();
            dto.setMenuCode(menuCode);

            List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByLoginSession(tenantCode);
            List<MenuDispatchPO> poList = menuDispatchAccessor.listByMenuCode(tenantCode, menuCode, shopGroupCodeList);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("menuMgtService|getDispatchByMenuCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutNew(MenuPO po, List<MenuSeriesRelPO> seriesRelPOlist) {
        MenuPO exist = menuAccessor.getByMenuCode(po.getTenantCode(), po.getMenuCode());
        if (exist != null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
        }

        int inserted = menuAccessor.insert(po);
        if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
            log.error("menuMgtService|putNewMenu|error|" + inserted);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }

        int deleted4SeriesRel = menuSeriesRelAccessor.deleteByMenuCode(po.getTenantCode(), po.getMenuCode());
        for (MenuSeriesRelPO seriesRelPO : seriesRelPOlist) {
            int inserted4SeriesRel = menuSeriesRelAccessor.insert(seriesRelPO);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4SeriesRel) {
                log.error("menuMgtService|putNewSeriesRel|error|" + inserted4SeriesRel);
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
        }

        deleteMenuDispatchCache(po.getTenantCode());

        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutUpdate(MenuPO po, List<MenuSeriesRelPO> seriesRelPOlist) {
        MenuPO exist = menuAccessor.getByMenuCode(po.getTenantCode(), po.getMenuCode());
        if (exist == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
        }

        int updated = menuAccessor.update(po);
        if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
            log.error("menuMgtService|putUpdateMenu|error|" + updated);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }

        int deleted4SeriesRel = menuSeriesRelAccessor.deleteByMenuCode(po.getTenantCode(), po.getMenuCode());
        for (MenuSeriesRelPO seriesRelPO : seriesRelPOlist) {
            int inserted4SeriesRel = menuSeriesRelAccessor.insert(seriesRelPO);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4SeriesRel) {
                log.error("menuMgtService|putUpdateSeriesRel|error|" + inserted4SeriesRel);
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
        }

        deleteMenuDispatchCache(po.getTenantCode());

        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doDeleteByMenuCode(String tenantCode, String menuCode) {
        int deleted4Series = menuAccessor.deleteByMenuCode(tenantCode, menuCode);
        int deleted4SeriesTeaRel = menuSeriesRelAccessor.deleteByMenuCode(tenantCode, menuCode);

        List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByLoginSession(tenantCode);
        int deleted4Dispatch = menuDispatchAccessor.deleteByMenuCode(tenantCode, menuCode, shopGroupCodeList);

        deleteMenuDispatchCache(tenantCode);

        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutDispatch(String tenantCode, String menuCode, List<MenuDispatchPO> poList) {
        MenuPO menuPO = menuAccessor.getByMenuCode(tenantCode, menuCode);
        if (menuPO == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
        }

        List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByLoginSession(menuPO.getTenantCode());
        int deleted = menuDispatchAccessor.deleteByMenuCode(tenantCode, menuCode, shopGroupCodeList);
        for (MenuDispatchPO po : poList) {
            menuDispatchAccessor.insert(po);
        }

        // 异步发送消息准备配置信息分发
        JSONObject jsonPayload = getAsyncDispatchMsg(tenantCode, menuCode, menuPO.getGmtModified());
        asyncDispatcher.dispatch(jsonPayload);

        return TeaMachineResult.success();
    }

    private void deleteMenuDispatchCache(String tenantCode) {
        List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByLoginSession(tenantCode);
        if (CollectionUtils.isEmpty(shopGroupCodeList)) {
            return;
        }

        List<String> fileNameList = Lists.newArrayList();
        for (String shopGroupCode : shopGroupCodeList) {
            fileNameList.add(BizUtils.getMenuListFileName(shopGroupCode));
        }
        int deleted = menuDispatchCacheAccessor.deleteByFileNameList(tenantCode, CommonConsts.MENU_DISPATCH_LIST_TRUE,
                fileNameList);
        if (deleted == CommonConsts.DB_DELETED_ZERO_ROW) {
            log.error("menuMgtService|deleteMenuDispatchCache|error|" + deleted + "|" + tenantCode);
        }
    }

    private JSONObject getAsyncDispatchMsg(String tenantCode, String menuCode, Date gmtModified) {
        // 异步发送消息准备配置信息分发
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_MENU_DISPATCH_REQUESTED);
        jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(CommonConsts.JSON_KEY_LOGIN_NAME, DaoUtils.getAdminPOByLoginSession(tenantCode).getLoginName());
        jsonPayload.put(CommonConsts.JSON_KEY_MENU_CODE, menuCode);
        jsonPayload.put(CommonConsts.JSON_KEY_MENU_GMTMODIFIED_YMDHMS,
                DateUtils.transformYMDHMS(gmtModified));
        return jsonPayload;
    }
}
