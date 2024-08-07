package com.langtuo.teamachine.mqtt.worker;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.model.drink.TeaDTO;
import com.langtuo.teamachine.api.model.menu.MenuDTO;
import com.langtuo.teamachine.api.model.menu.MenuDispatchDTO;
import com.langtuo.teamachine.api.model.menu.SeriesDTO;
import com.langtuo.teamachine.api.model.menu.SeriesTeaRelDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import com.langtuo.teamachine.api.service.menu.MenuMgtService;
import com.langtuo.teamachine.api.service.menu.SeriesMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.mqtt.MQTTService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.*;

@Slf4j
public class MenuDispatchWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 菜单编码
     */
    private String menuCode;

    public MenuDispatchWorker(String payload) {
        JSONObject payloadJSON = JSONObject.parseObject(payload);
        this.tenantCode = payloadJSON.getString("tenantCode");
        this.menuCode = payloadJSON.getString("menuCode");
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(menuCode)) {
            throw new IllegalArgumentException("tenantCode or menuCode is blank");
        }
    }

    @Override
    public void run() {
        MenuMgtService menuMgtService = getMenuMgtService();
        MenuDTO menuDTO = getModel(menuMgtService.getByCode(tenantCode, menuCode));
        if (menuDTO == null) {
            log.info("list menu error, stop worker");
            return;
        }


        SeriesMgtService seriesMgtService = getSeriesMgtService();
        List<SeriesDTO> seriesList = menuDTO.getMenuSeriesRelList().stream()
                .map(menuSeriesRelDTO -> {
                    SeriesDTO seriesDTO = getModel(seriesMgtService.getByCode(
                            tenantCode, menuSeriesRelDTO.getSeriesCode()));
                    return seriesDTO;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(seriesList)) {
            log.info("series list is empty, stop worker");
            return;
        }
        List<String> teaCodeList = seriesList.stream()
                .map(seriesDTO -> {
                    List<SeriesTeaRelDTO> seriesTeaRelList = seriesDTO.getSeriesTeaRelList();
                    if (CollectionUtils.isEmpty(seriesTeaRelList)) {
                        return null;
                    }
                    return seriesTeaRelList.stream()
                            .map(seriesTeaRelDTO -> seriesTeaRelDTO.getTeaCode())
                            .collect(Collectors.toList());
                })
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        TeaMgtService teaMgtService = getTeaMgtService();
        List<TeaDTO> teaList = teaCodeList.stream()
                .map(teaCode -> {
                    TeaDTO teaDTO = getModel(teaMgtService.getByCode(tenantCode, teaCode));
                    return teaDTO;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("prepare to send menu=" + JSONObject.toJSONString(teaList));


        // 准备发送
        List<String> machineCodeList = getMachineCodeList(menuDTO);
        if (CollectionUtils.isEmpty(machineCodeList)) {
            log.info("machine code list is empty, stop worker");
        }
        log.info("prepare to send machine=" + JSONObject.toJSONString(machineCodeList));

        MQTTService mqttService = getMQTTService();
        machineCodeList.stream().forEach(machineCode -> {
            mqttService.sendMsgByP2P(machineCode, JSONObject.toJSONString(menuDTO));
        });
    }

    private MenuMgtService getMenuMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MenuMgtService menuMgtService = appContext.getBean(MenuMgtService.class);
        return menuMgtService;
    }

    private MQTTService getMQTTService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MQTTService mqttService = appContext.getBean(MQTTService.class);
        return mqttService;
    }

    private ShopMgtService getShopMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        ShopMgtService shopMgtService = appContext.getBean(ShopMgtService.class);
        return shopMgtService;
    }

    private MachineMgtService getMachineMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MachineMgtService machineMgtService = appContext.getBean(MachineMgtService.class);
        return machineMgtService;
    }

    private SeriesMgtService getSeriesMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        SeriesMgtService seriesMgtService = appContext.getBean(SeriesMgtService.class);
        return seriesMgtService;
    }

    private TeaMgtService getTeaMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        TeaMgtService teaMgtService = appContext.getBean(TeaMgtService.class);
        return teaMgtService;
    }

    private List<String> getMachineCodeList(MenuDTO menuDTO) {
        MenuMgtService menuMgtService = getMenuMgtService();
        MenuDispatchDTO menuDispatchDTO = getModel(menuMgtService.listDispatchByMenuCode(tenantCode, menuCode));
        if (menuDispatchDTO == null) {
            log.info("menu dispatch is null");
            return null;
        }

        ShopMgtService shopMgtService = getShopMgtService();
        List<String> shopCodeList = menuDispatchDTO.getShopGroupCodeList().stream()
                .map(shopGroupCode -> {
                    List<ShopDTO> shopList = getListModel(shopMgtService.listByShopGroupCode(
                            tenantCode, shopGroupCode));
                    if (shopList == null) {
                        return null;
                    }

                    return shopList.stream()
                            .map(shop -> shop.getShopCode())
                            .collect(Collectors.toList());
                })
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(shopCodeList)) {
            log.info("shop code list is empty");
            return null;
        }

        MachineMgtService machineMgtService = getMachineMgtService();
        List<String> machineCodeList = shopCodeList.stream()
                .map(shopCode -> {
                    List<MachineDTO> machineList = getListModel(machineMgtService.listByShopCode(
                            tenantCode, shopCode));
                    if (machineList == null) {
                        return null;
                    }

                    return machineList.stream()
                            .map(shop -> shop.getMachineCode())
                            .collect(Collectors.toList());
                })
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return machineCodeList;
    }
}
