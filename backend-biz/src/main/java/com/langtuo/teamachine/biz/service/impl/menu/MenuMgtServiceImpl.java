package com.langtuo.teamachine.biz.service.impl.menu;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.service.aync.AsyncDispatcher;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menu.MenuDTO;
import com.langtuo.teamachine.api.model.menu.MenuDispatchDTO;
import com.langtuo.teamachine.api.model.menu.MenuSeriesRelDTO;
import com.langtuo.teamachine.api.request.menu.MenuDispatchPutRequest;
import com.langtuo.teamachine.api.request.menu.MenuPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.menu.MenuMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.MessageUtils;
import com.langtuo.teamachine.dao.accessor.menu.MenuAccessor;
import com.langtuo.teamachine.dao.accessor.menu.MenuDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.menu.MenuSeriesRelAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchPO;
import com.langtuo.teamachine.dao.po.menu.MenuPO;
import com.langtuo.teamachine.dao.po.menu.MenuSeriesRelPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
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
public class MenuMgtServiceImpl implements MenuMgtService {
    @Resource
    private MenuAccessor menuAccessor;

    @Resource
    private MenuSeriesRelAccessor menuSeriesRelAccessor;

    @Resource
    private MenuDispatchAccessor menuDispatchAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;

    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<List<MenuDTO>> list(String tenantCode) {
        TeaMachineResult<List<MenuDTO>> teaMachineResult;
        try {
            List<MenuPO> list = menuAccessor.selectList(tenantCode);
            List<MenuDTO> dtoList = convertToMenuDTO(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<MenuDTO>> listByShopCode(String tenantCode, String shopCode) {
        TeaMachineResult<List<MenuDTO>> teaMachineResult;
        try {
            ShopPO shopPO = shopAccessor.selectOneByShopCode(tenantCode, shopCode);
            if (shopPO == null) {
                teaMachineResult = TeaMachineResult.success();
            }

            List<MenuDispatchPO> menuDispatchPOList = menuDispatchAccessor.selectListByShopGroupCode(
                    tenantCode, shopPO.getShopGroupCode());
            if (CollectionUtils.isEmpty(menuDispatchPOList)) {
                teaMachineResult = TeaMachineResult.success();
            }

            List<String> menuCodeList = menuDispatchPOList.stream()
                    .map(MenuDispatchPO::getMenuCode)
                    .collect(Collectors.toList());
            List<MenuPO> cleanRulePOList = menuAccessor.selectListByMenuCode(tenantCode,
                    menuCodeList);
            List<MenuDTO> menuDTOList = convertToMenuDTO(cleanRulePOList);
            teaMachineResult = TeaMachineResult.success(menuDTOList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> triggerDispatchByShopCode(String tenantCode, String shopCode, String machineCode) {
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(BizConsts.JSON_KEY_BIZ_CODE, BizConsts.BIZ_CODE_MENU_LIST_REQUESTED);
        jsonPayload.put(BizConsts.JSON_KEY_TENANT_CODE, tenantCode);
        jsonPayload.put(BizConsts.JSON_KEY_SHOP_CODE, shopCode);
        jsonPayload.put(BizConsts.JSON_KEY_MACHINE_CODE, machineCode);
        asyncDispatcher.dispatch(jsonPayload);

        return TeaMachineResult.success();
    }

    @Override
    public TeaMachineResult<PageDTO<MenuDTO>> search(String tenantName, String seriesCode, String seriesName,
            int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<MenuDTO>> teaMachineResult;
        try {
            PageInfo<MenuPO> pageInfo = menuAccessor.search(tenantName, seriesCode, seriesName,
                    pageNum, pageSize);
            List<MenuDTO> dtoList = convertToMenuDTO(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<MenuDTO> getByCode(String tenantCode, String seriesCode) {
        TeaMachineResult<MenuDTO> teaMachineResult;
        try {
            MenuPO toppingTypePO = menuAccessor.selectOneByMenuCode(tenantCode, seriesCode);
            MenuDTO seriesDTO = convert(toppingTypePO);
            teaMachineResult = TeaMachineResult.success(seriesDTO);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<MenuDTO> getByName(String tenantCode, String seriesName) {
        TeaMachineResult<MenuDTO> teaMachineResult;
        try {
            MenuPO toppingTypePO = menuAccessor.selectOneByMenuName(tenantCode, seriesName);
            MenuDTO tenantDTO = convert(toppingTypePO);
            teaMachineResult = TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(MenuPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        MenuPO seriesPO = convertMenuPO(request);
        List<MenuSeriesRelPO> menuSeriesRelPOList = convertToMenuSeriesRelPO(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            MenuPO exist = menuAccessor.selectOneByMenuCode(seriesPO.getTenantCode(),
                    seriesPO.getMenuCode());
            if (exist != null) {
                int updated = menuAccessor.update(seriesPO);
            } else {
                int inserted = menuAccessor.insert(seriesPO);
            }

            int deleted4SeriesTeaRel = menuSeriesRelAccessor.deleteBySeriesCode(seriesPO.getTenantCode(), seriesPO.getMenuCode());
            if (!CollectionUtils.isEmpty(menuSeriesRelPOList)) {
                menuSeriesRelPOList.forEach(seriesTeaRelPO -> {
                    menuSeriesRelAccessor.insert(seriesTeaRelPO);
                });
            }

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String menuCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted4Series = menuAccessor.deleteByMenuCode(tenantCode, menuCode);
            int deleted4SeriesTeaRel = menuSeriesRelAccessor.deleteBySeriesCode(tenantCode, menuCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> putDispatch(MenuDispatchPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        List<MenuDispatchPO> poList = convert(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = menuDispatchAccessor.deleteByMenuCode(request.getTenantCode(),
                    request.getMenuCode());
            poList.forEach(po -> {
                menuDispatchAccessor.insert(po);
            });

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("putDispatch error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }

        // 异步发送消息准备配置信息分发
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(BizConsts.JSON_KEY_BIZ_CODE, BizConsts.BIZ_CODE_MENU_UPDATED);
        jsonPayload.put(BizConsts.JSON_KEY_TENANT_CODE, request.getTenantCode());
        jsonPayload.put(BizConsts.JSON_KEY_MENU_CODE, request.getMenuCode());
        asyncDispatcher.dispatch(jsonPayload);

        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<MenuDispatchDTO> getDispatchByMenuCode(String tenantCode, String menuCode) {
        TeaMachineResult<MenuDispatchDTO> teaMachineResult;
        try {
            MenuDispatchDTO dto = new MenuDispatchDTO();
            dto.setMenuCode(menuCode);

            List<MenuDispatchPO> poList = menuDispatchAccessor.selectListByMenuCode(tenantCode, menuCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("listDispatchByMenuCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    private List<MenuDTO> convertToMenuDTO(List<MenuPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<MenuDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private MenuDTO convert(MenuPO po) {
        if (po == null) {
            return null;
        }

        MenuDTO dto = new MenuDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setMenuCode(po.getMenuCode());
        dto.setMenuName(po.getMenuName());
        dto.setValidFrom(po.getValidFrom());

        List<MenuSeriesRelPO> seriesTeaRelPOList = menuSeriesRelAccessor.selectListBySeriesCode(
                po.getTenantCode(), po.getMenuCode());
        dto.setMenuSeriesRelList(convert(seriesTeaRelPOList));
        return dto;
    }

    private MenuPO convertMenuPO(MenuPutRequest request) {
        if (request == null) {
            return null;
        }

        MenuPO po = new MenuPO();
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(request.getExtraInfo());
        po.setMenuCode(request.getMenuCode());
        po.setMenuName(request.getMenuName());
        po.setValidFrom(request.getValidFrom());
        return po;
    }

    private List<MenuSeriesRelDTO> convert(List<MenuSeriesRelPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream().map(po -> {
            MenuSeriesRelDTO dto = new MenuSeriesRelDTO();
            dto.setSeriesCode(po.getSeriesCode());
            dto.setMenuCode(po.getMenuCode());
            return dto;
        }).collect(Collectors.toList());
    }

    private List<MenuSeriesRelPO> convertToMenuSeriesRelPO(MenuPutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getMenuSeriesRelList())) {
            return null;
        }

        return request.getMenuSeriesRelList().stream()
                .map(menuSeriesRelPutRequest -> {
                    MenuSeriesRelPO po = new MenuSeriesRelPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setSeriesCode(menuSeriesRelPutRequest.getSeriesCode());
                    po.setMenuCode(menuSeriesRelPutRequest.getMenuCode());
                    return po;
                }).collect(Collectors.toList());
    }

    private List<MenuDispatchPO> convert(MenuDispatchPutRequest request) {
        String tenantCode = request.getTenantCode();
        String menuCode = request.getMenuCode();

        return request.getShopGroupCodeList().stream()
                .map(shopGroupCode -> {
                    MenuDispatchPO po = new MenuDispatchPO();
                    po.setTenantCode(tenantCode);
                    po.setMenuCode(menuCode);
                    po.setShopGroupCode(shopGroupCode);
                    return po;
                }).collect(Collectors.toList());
    }
}
