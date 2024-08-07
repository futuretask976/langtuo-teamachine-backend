package com.langtuo.teamachine.mqtt.worker.dispatch;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.langtuo.teamachine.dao.oss.OSSUtils;
import com.langtuo.teamachine.mqtt.MQTTService;
import com.langtuo.teamachine.mqtt.config.MQTTConfig;
import com.langtuo.teamachine.mqtt.util.IOUtils;
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

import static com.langtuo.teamachine.api.result.LangTuoResult.getListModel;
import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

@Slf4j
public class CleanRuleDispatchWorker implements Runnable {
    /**
     * 收到的消息中的key关键字
     */
    private static final String PAYLOAD_KEY_TENANT_CODE = "tenantCode";
    private static final String PAYLOAD_KEY_MENU_CODE = "menuCode";

    /**
     * 发送的消息中的key关键字
     */
    private static final String MSG_KEY_MD5_AS_HEX = "md5AsHex";
    private static final String MSG_KEY_OSS_PATH = "ossPath";


    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 菜单编码
     */
    private String menuCode;

    public CleanRuleDispatchWorker(String payload) {
        JSONObject payloadJSON = JSONObject.parseObject(payload);
        this.tenantCode = payloadJSON.getString(PAYLOAD_KEY_TENANT_CODE);
        this.menuCode = payloadJSON.getString(PAYLOAD_KEY_MENU_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(menuCode)) {
            throw new IllegalArgumentException("tenantCode or menuCode is blank");
        }
    }

    @Override
    public void run() {
        String dispatchCont = getDispatchCont();
        if (StringUtils.isBlank(dispatchCont)) {
            log.info("dispatch content error, stop worker");
            return;
        }
        File outputFile = new File("dispatch/output.json");
        boolean wrote = IOUtils.writeStrToFile(dispatchCont, outputFile);
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

        // 准备发送
        List<String> machineCodeList = getMachineCodeList();
        if (CollectionUtils.isEmpty(machineCodeList)) {
            log.info("machine code list is empty, stop worker");
        }

        MQTTService mqttService = getMQTTService();
        JSONObject payloadJSON = new JSONObject();
        payloadJSON.put(MSG_KEY_MD5_AS_HEX, md5AsHex);
        payloadJSON.put(MSG_KEY_OSS_PATH, ossPath);
        machineCodeList.stream().forEach(machineCode -> {
            mqttService.sendMsgByTopic(MQTTConfig.TOPIC_DISPATCH_MENU, payloadJSON.toJSONString());
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

    private String getDispatchCont() {
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
        JSONObject menuJSON = (JSONObject) JSON.toJSON(menuDTO);
        menuJSON.remove("menuSeriesRelList");
        menuJSON.put("seriesList", new JSONArray());
        seriesList.stream().forEach(seriesDTO -> {
            JSONObject seriesJSON = (JSONObject) JSON.toJSON(seriesDTO);
            seriesJSON.remove("seriesTeaRelList");
            seriesJSON.put("teaList", new JSONArray());
            teaList.stream().forEach(teaDTO -> {
                seriesJSON.getJSONArray("teaList").add(JSON.toJSON(teaDTO));
            });
            menuJSON.getJSONArray("seriesList").add(seriesJSON);
        });

        return menuJSON.toJSONString();
    }

    private List<String> getMachineCodeList() {
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
