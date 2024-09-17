package com.langtuo.teamachine.biz.aync.worker.menu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.drink.TeaDTO;
import com.langtuo.teamachine.api.model.menu.MenuDTO;
import com.langtuo.teamachine.api.model.menu.SeriesDTO;
import com.langtuo.teamachine.api.model.menu.SeriesTeaRelDTO;
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import com.langtuo.teamachine.api.service.menu.MenuMgtService;
import com.langtuo.teamachine.api.service.menu.SeriesMgtService;
import com.langtuo.teamachine.biz.util.BizUtils;
import com.langtuo.teamachine.biz.util.SpringServiceUtils;
import com.langtuo.teamachine.dao.config.OSSConfig;
import com.langtuo.teamachine.dao.oss.OSSUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import com.langtuo.teamachine.mqtt.util.MqttUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        this.tenantCode = jsonPayload.getString(CommonConsts.JSON_KEY_TENANT_CODE);
        this.shopCode = jsonPayload.getString(CommonConsts.JSON_KEY_SHOP_CODE);
        this.machineCode = jsonPayload.getString(CommonConsts.JSON_KEY_MACHINE_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode) || StringUtils.isBlank(shopCode)) {
            log.error("menuDispatch4InitWorker|init|illegalArgument|" + tenantCode + "|" + shopCode + "|" + machineCode);
            throw new IllegalArgumentException("tenantCode or menuCode or machineCode is blank");
        }
    }

    @Override
    public void run() {
        JSONArray dispatchCont = getDispatchCont();
        if (dispatchCont == null) {
            log.info("menuDispatch4InitWorker|getDispatchCont|error|stopWorker|" + dispatchCont);
            return;
        }
        File outputFile = new File("dispatch4Init/output.json");
        boolean wrote = BizUtils.writeStrToFile(dispatchCont.toJSONString(), outputFile);
        if (!wrote) {
            log.info("menuDispatch4InitWorker|writeStrToFile|failed|stopWorker");
            return;
        }
        String ossPath = BizUtils.uploadOSS(outputFile);
        if (StringUtils.isBlank(ossPath)) {
            log.info("menuDispatch4InitWorker|uploadOSS|failed|stopWorker");
            return;
        }
        String md5AsHex = BizUtils.calcMD5Hex(outputFile);
        if (StringUtils.isBlank(md5AsHex)) {
            log.info("menuDispatch4InitWorker|calcMD5Hex|failed|stopWorker");
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_MENU_LIST);
        jsonMsg.put(CommonConsts.JSON_KEY_MD5_AS_HEX, md5AsHex);
        jsonMsg.put(CommonConsts.JSON_KEY_OSS_PATH, ossPath);

        // 准备发送
        MqttProducer mqttProducer = SpringServiceUtils.getMqttProducer();
        mqttProducer.sendP2PMsgByTenant(tenantCode, machineCode, jsonMsg.toJSONString());
    }

    private JSONArray getDispatchCont() {
        MenuMgtService menuMgtService = SpringServiceUtils.getMenuMgtService();
        List<MenuDTO> menuDTOList = getModel(menuMgtService.listByShopCode(tenantCode, shopCode));
        if (CollectionUtils.isEmpty(menuDTOList)) {
            log.info("menuDispatch4InitWorker|getMenu|empty|stopWorker");
            return null;
        }

        JSONArray arr = new JSONArray();
        for (MenuDTO menuDTO : menuDTOList) {
            JSONObject menuDispatchCont = getDispatchCont4Menu(menuDTO.getMenuCode());
            arr.add(menuDispatchCont);
        }
        return arr;
    }

    private JSONObject getDispatchCont4Menu(String menuCode) {
        MenuMgtService menuMgtService = SpringServiceUtils.getMenuMgtService();
        MenuDTO menuDTO = getModel(menuMgtService.getByCode(tenantCode, menuCode));
        if (menuDTO == null) {
            log.info("menuDispatch4InitWorker|getMenu|null|stopWorker");
            return null;
        }

        SeriesMgtService seriesMgtService = SpringServiceUtils.getSeriesMgtService();
        List<SeriesDTO> seriesList = menuDTO.getMenuSeriesRelList().stream()
                .map(menuSeriesRelDTO -> {
                    SeriesDTO seriesDTO = getModel(seriesMgtService.getByCode(
                            tenantCode, menuSeriesRelDTO.getSeriesCode()));
                    return seriesDTO;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(seriesList)) {
            log.info("menuDispatch4InitWorker|getSeriesList|empty|stopWorker");
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
            log.info("menuDispatch4InitWorker|getTeaList|empty|stopWorker");
            return null;
        }

        TeaMgtService teaMgtService = SpringServiceUtils.getTeaMgtService();
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
        for (SeriesDTO seriesDTO : seriesList) {
            JSONObject seriesJSON = (JSONObject) JSON.toJSON(seriesDTO);
            seriesJSON.remove("seriesTeaRelList");
            seriesJSON.put("teaList", new JSONArray());
            for (TeaDTO teaDTO : teaList) {
                seriesJSON.getJSONArray("teaList").add(JSON.toJSON(teaDTO));
            }
            jsonMenu.getJSONArray("seriesList").add(seriesJSON);
        }
        return jsonMenu;
    }
}
