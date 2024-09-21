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
import com.langtuo.teamachine.dao.accessor.menu.MenuSeriesRelAccessor;
import com.langtuo.teamachine.dao.mapper.menu.MenuDispatchHistoryMapper;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchPO;
import com.langtuo.teamachine.dao.po.menu.MenuPO;
import com.langtuo.teamachine.dao.po.menu.MenuSeriesRelPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.DateUtils;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
    private MenuDispatchHistoryMapper menuDispatchHistoryMapper;

    @Resource
    private AsyncDispatcher asyncDispatcher;

    @Override
    public TeaMachineResult<List<MenuDTO>> list(String tenantCode) {
        try {
            List<MenuPO> list = menuAccessor.list(tenantCode);
            List<MenuDTO> dtoList = convertToMenuDTO(list);
            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("menuMgtService|list|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
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
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<MenuDTO> getByCode(String tenantCode, String seriesCode) {
        try {
            MenuPO toppingTypePO = menuAccessor.getByMenuCode(tenantCode, seriesCode);
            MenuDTO seriesDTO = convertToMenuDTO(toppingTypePO);
            return TeaMachineResult.success(seriesDTO);
        } catch (Exception e) {
            log.error("menuMgtService|getByCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(MenuPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        MenuPO po = convertMenuPO(request);
        List<MenuSeriesRelPO> seriesRelPOList = convertToMenuSeriesRelPO(request);
        if (request.isPutNew()) {
            return putNew(po, seriesRelPOList);
        } else {
            return putUpdate(po, seriesRelPOList);
        }
    }

    private TeaMachineResult<Void> putNew(MenuPO po, List<MenuSeriesRelPO> seriesRelPOlist) {
        try {
            MenuPO exist = menuAccessor.getByMenuCode(po.getTenantCode(), po.getMenuCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = menuAccessor.insert(po);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("menuMgtService|putNewMenu|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }

            int deleted4SeriesRel = menuSeriesRelAccessor.deleteByMenuCode(po.getTenantCode(), po.getMenuCode());
            for (MenuSeriesRelPO seriesRelPO : seriesRelPOlist) {
                int inserted4SeriesRel = menuSeriesRelAccessor.insert(seriesRelPO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4SeriesRel) {
                    log.error("menuMgtService|putNewSeriesRel|error|" + inserted4SeriesRel);
                    return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }

            deleteMenuDispatchlistHistory(po.getTenantCode(), po.getMenuCode());

            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("menuMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(MenuPO po, List<MenuSeriesRelPO> seriesRelPOlist) {
        try {
            MenuPO exist = menuAccessor.getByMenuCode(po.getTenantCode(), po.getMenuCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = menuAccessor.update(po);
            if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
                log.error("menuMgtService|putUpdateMenu|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }

            int deleted4SeriesRel = menuSeriesRelAccessor.deleteByMenuCode(po.getTenantCode(), po.getMenuCode());
            for (MenuSeriesRelPO seriesRelPO : seriesRelPOlist) {
                int inserted4SeriesRel = menuSeriesRelAccessor.insert(seriesRelPO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4SeriesRel) {
                    log.error("menuMgtService|putUpdateSeriesRel|error|" + inserted4SeriesRel);
                    return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }

            deleteMenuDispatchlistHistory(po.getTenantCode(), po.getMenuCode());

            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("menuMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String menuCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int deleted4Series = menuAccessor.deleteByMenuCode(tenantCode, menuCode);
            int deleted4SeriesTeaRel = menuSeriesRelAccessor.deleteByMenuCode(tenantCode, menuCode);
            int deleted4Dispatch = menuDispatchAccessor.deleteByMenuCode(tenantCode, menuCode);

            deleteMenuDispatchlistHistory(tenantCode, menuCode);

            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("menuMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> putDispatch(MenuDispatchPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }
        List<MenuDispatchPO> poList = convertToMenuDispatchPO(request);

        try {
            MenuPO menuPO = menuAccessor.getByMenuCode(request.getTenantCode(), request.getMenuCode());
            if (menuPO == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int deleted = menuDispatchAccessor.deleteByMenuCode(request.getTenantCode(), request.getMenuCode());
            for (MenuDispatchPO po : poList) {
                menuDispatchAccessor.insert(po);
            }

            // 异步发送消息准备配置信息分发
            JSONObject jsonPayload = new JSONObject();
            jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_MENU_DISPATCH_REQUESTED);
            jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, request.getTenantCode());
            jsonPayload.put(CommonConsts.JSON_KEY_MENU_CODE, request.getMenuCode());
            jsonPayload.put(CommonConsts.JSON_KEY_MENU_GMTMODIFIED_YMDHMS,
                    DateUtils.transformYMDHMS(menuPO.getGmtModified()));
            asyncDispatcher.dispatch(jsonPayload);

            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("menuMgtService|putDispatch|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<MenuDispatchDTO> getDispatchByMenuCode(String tenantCode, String menuCode) {
        try {
            MenuDispatchDTO dto = new MenuDispatchDTO();
            dto.setMenuCode(menuCode);

            List<MenuDispatchPO> poList = menuDispatchAccessor.listByMenuCode(tenantCode, menuCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("menuMgtService|getDispatchByMenuCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    private void deleteMenuDispatchlistHistory(String tenantCode, String menuCode) {
        List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByMenuCode(tenantCode, menuCode);
        if (CollectionUtils.isEmpty(shopGroupCodeList)) {
            return;
        }

        List<String> fileNameList = Lists.newArrayList();
        for (String shopGroupCode : shopGroupCodeList) {
            fileNameList.add(BizUtils.getMenuListFileName(shopGroupCode));
        }
        int deleted = menuDispatchHistoryMapper.deleteByFileNameList(tenantCode, CommonConsts.MENU_DISPATCH_LIST_TRUE,
                fileNameList);
    }
}
