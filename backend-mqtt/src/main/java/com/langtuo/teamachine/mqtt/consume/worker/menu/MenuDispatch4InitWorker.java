package com.langtuo.teamachine.mqtt.consume.worker.menu;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.drink.TeaDTO;
import com.langtuo.teamachine.api.model.menu.MenuDTO;
import com.langtuo.teamachine.api.model.menu.SeriesDTO;
import com.langtuo.teamachine.api.model.menu.SeriesTeaRelDTO;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import com.langtuo.teamachine.api.service.menu.MenuMgtService;
import com.langtuo.teamachine.api.service.menu.SeriesMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.dao.oss.OSSUtils;
import com.langtuo.teamachine.mqtt.MqttService;
import com.langtuo.teamachine.mqtt.constant.MqttConsts;
import com.langtuo.teamachine.mqtt.util.MqttUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getModel;

@Slf4j
public class MenuDispatch4InitWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 机器编码
     */
    private String machineCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    public MenuDispatch4InitWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_TENANT_CODE);
        this.shopCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_SHOP_CODE);
        this.machineCode = jsonPayload.getString(MqttConsts.RECEIVE_KEY_MACHINE_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode) || StringUtils.isBlank(shopCode)) {
            throw new IllegalArgumentException("tenantCode or menuCode is blank");
        }
    }

    @Override
    public void run() {
        JSONArray dispatchCont = getDispatchCont();
        if (dispatchCont == null) {
            log.info("dispatch content error, stop worker");
            return;
        }
        File outputFile = new File("dispatch4Init/output.json");
        boolean wrote = MqttUtils.writeStrToFile(dispatchCont.toJSONString(), outputFile);
        if (!wrote) {
            log.info("write file error, stop worker");
            return;
        }
        String ossPath = uploadOSS(outputFile);
        if (StringUtils.isBlank(ossPath)) {
            log.info("upload oss error, stop worker");
            return;
        }
        String md5AsHex = calcMD5Hex(outputFile);
        if (StringUtils.isBlank(md5AsHex)) {
            log.info("calc md5 as hex error, stop worker");
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(MqttConsts.SEND_KEY_BIZ_CODE, MqttConsts.BIZ_CODE_DISPATCH_MENU_INIT_LIST);
        jsonMsg.put(MqttConsts.SEND_KEY_MD5_AS_HEX, md5AsHex);
        jsonMsg.put(MqttConsts.SEND_KEY_OSS_PATH, ossPath);
        log.info("$$$$$ MenuDispatch4InitWorker jsonMsg=" + jsonMsg.toJSONString());

        // 准备发送
        MqttService mqttService = getMQTTService();
        mqttService.sendP2PMsgByTenant(tenantCode, machineCode, jsonMsg.toJSONString());
    }

    private MenuMgtService getMenuMgtService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MenuMgtService menuMgtService = appContext.getBean(MenuMgtService.class);
        return menuMgtService;
    }

    private MqttService getMQTTService() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MqttService mqttService = appContext.getBean(MqttService.class);
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

    private JSONArray getDispatchCont() {
        MenuMgtService menuMgtService = getMenuMgtService();
        List<MenuDTO> menuDTOList = getModel(menuMgtService.listByShopCode(tenantCode, shopCode));
        if (CollectionUtils.isEmpty(menuDTOList)) {
            log.info("list menu error, stop worker");
            return null;
        }

        JSONArray arr = new JSONArray();
        menuDTOList.forEach(menuDTO -> {
            JSONObject menuDispatchCont = getDispatchCont4Menu(menuDTO.getMenuCode());
            arr.add(menuDispatchCont);
        });
        return arr;
    }

    private JSONObject getDispatchCont4Menu(String menuCode) {
        MenuMgtService menuMgtService = getMenuMgtService();
        MenuDTO menuDTO = getModel(menuMgtService.getByCode(tenantCode, menuCode));
        if (menuDTO == null) {
            log.info("list menu error, stop worker");
            return null;
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
            return null;
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
        if (CollectionUtils.isEmpty(teaCodeList)) {
            log.info("tea code list is empty, stop worker");
            return null;
        }

        TeaMgtService teaMgtService = getTeaMgtService();
        List<TeaDTO> teaList = teaCodeList.stream()
                .map(teaCode -> {
                    TeaDTO teaDTO = getModel(teaMgtService.getByCode(tenantCode, teaCode));
                    return teaDTO;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 拼接需要输出的内容
        JSONObject jsonMenu = (JSONObject) JSON.toJSON(menuDTO);
        jsonMenu.remove("menuSeriesRelList");
        jsonMenu.put("seriesList", new JSONArray());
        seriesList.stream().forEach(seriesDTO -> {
            JSONObject seriesJSON = (JSONObject) JSON.toJSON(seriesDTO);
            seriesJSON.remove("seriesTeaRelList");
            seriesJSON.put("teaList", new JSONArray());
            teaList.stream().forEach(teaDTO -> {
                seriesJSON.getJSONArray("teaList").add(JSON.toJSON(teaDTO));
            });
            jsonMenu.getJSONArray("seriesList").add(seriesJSON);
        });

        return jsonMenu;
    }

    private String uploadOSS(File file) {
        String ossPath = null;
        try {
            ossPath = OSSUtils.uploadFile(file);
        } catch (FileNotFoundException e) {
            log.info("upload oss error, stop worker");
        }
        return ossPath;
    }

    private String calcMD5Hex(File file) {
        String md5AsHex = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            md5AsHex = DigestUtils.md5DigestAsHex(fileInputStream);
        } catch (IOException e) {
            log.info("calc md5 error, stop worker");
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.info("close file input stream error, stop worker");
                }
            }
        }
        return md5AsHex;
    }
}
