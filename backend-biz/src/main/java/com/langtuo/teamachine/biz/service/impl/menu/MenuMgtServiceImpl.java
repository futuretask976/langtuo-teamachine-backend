package com.langtuo.teamachine.biz.service.impl.menu;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menu.MenuDTO;
import com.langtuo.teamachine.api.model.menu.MenuDispatchDTO;
import com.langtuo.teamachine.api.model.menu.MenuSeriesRelDTO;
import com.langtuo.teamachine.api.request.menu.MenuDispatchPutRequest;
import com.langtuo.teamachine.api.request.menu.MenuPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.menu.MenuMgtService;
import com.langtuo.teamachine.dao.accessor.menu.MenuAccessor;
import com.langtuo.teamachine.dao.accessor.menu.MenuDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.menu.MenuSeriesRelAccessor;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchPO;
import com.langtuo.teamachine.dao.po.menu.MenuPO;
import com.langtuo.teamachine.dao.po.menu.MenuSeriesRelPO;
import com.langtuo.teamachine.mqtt.publish.MqttPublisher4Console;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private MqttPublisher4Console mqttPublisher4Console;

    @Override
    public LangTuoResult<List<MenuDTO>> list(String tenantCode) {
        LangTuoResult<List<MenuDTO>> langTuoResult = null;
        try {
            List<MenuPO> list = menuAccessor.selectList(tenantCode);
            List<MenuDTO> dtoList = convertToMenuDTO(list);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<MenuDTO>> search(String tenantName, String seriesCode, String seriesName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<MenuDTO>> langTuoResult = null;
        try {
            PageInfo<MenuPO> pageInfo = menuAccessor.search(tenantName, seriesCode, seriesName,
                    pageNum, pageSize);
            List<MenuDTO> dtoList = convertToMenuDTO(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<MenuDTO> getByCode(String tenantCode, String seriesCode) {
        LangTuoResult<MenuDTO> langTuoResult = null;
        try {
            MenuPO toppingTypePO = menuAccessor.selectOneByCode(tenantCode, seriesCode);
            MenuDTO seriesDTO = convert(toppingTypePO);
            langTuoResult = LangTuoResult.success(seriesDTO);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<MenuDTO> getByName(String tenantCode, String seriesName) {
        LangTuoResult<MenuDTO> langTuoResult = null;
        try {
            MenuPO toppingTypePO = menuAccessor.selectOneByName(tenantCode, seriesName);
            MenuDTO tenantDTO = convert(toppingTypePO);
            langTuoResult = LangTuoResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(MenuPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        MenuPO seriesPO = convertMenuPO(request);
        List<MenuSeriesRelPO> menuSeriesRelPOList = convertToMenuSeriesRelPO(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            MenuPO exist = menuAccessor.selectOneByCode(seriesPO.getTenantCode(),
                    seriesPO.getMenuCode());
            if (exist != null) {
                int updated = menuAccessor.update(seriesPO);
            } else {
                int inserted = menuAccessor.insert(seriesPO);
            }

            int deleted4SeriesTeaRel = menuSeriesRelAccessor.delete(seriesPO.getTenantCode(), seriesPO.getMenuCode());
            if (!CollectionUtils.isEmpty(menuSeriesRelPOList)) {
                menuSeriesRelPOList.forEach(seriesTeaRelPO -> {
                    menuSeriesRelAccessor.insert(seriesTeaRelPO);
                });
            }

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String menuCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted4Series = menuAccessor.delete(tenantCode, menuCode);
            int deleted4SeriesTeaRel = menuSeriesRelAccessor.delete(tenantCode, menuCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> putDispatch(MenuDispatchPutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        List<MenuDispatchPO> poList = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = menuDispatchAccessor.delete(request.getTenantCode(),
                    request.getMenuCode());
            poList.forEach(po -> {
                menuDispatchAccessor.insert(po);
            });

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("putDispatch error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }

        // 异步发送消息准备配置信息分发
        mqttPublisher4Console.send4Menu(
                request.getTenantCode(), request.getMenuCode());

        return langTuoResult;
    }

    @Override
    public LangTuoResult<MenuDispatchDTO> getDispatchByCode(String tenantCode, String menuCode) {
        LangTuoResult<MenuDispatchDTO> langTuoResult = null;
        try {
            MenuDispatchDTO dto = new MenuDispatchDTO();
            dto.setMenuCode(menuCode);

            List<MenuDispatchPO> poList = menuDispatchAccessor.selectList(tenantCode, menuCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("listDispatchByMenuCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
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

        List<MenuSeriesRelPO> seriesTeaRelPOList = menuSeriesRelAccessor.selectList(
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
