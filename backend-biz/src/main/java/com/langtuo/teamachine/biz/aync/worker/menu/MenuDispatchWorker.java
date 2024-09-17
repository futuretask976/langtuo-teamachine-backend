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
import com.langtuo.teamachine.dao.accessor.menu.MenuDispatchAccessor;
import com.langtuo.teamachine.dao.config.OSSConfig;
import com.langtuo.teamachine.dao.oss.OSSUtils;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
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
public class MenuDispatchWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 菜单编码
     */
    private String menuCode;

    public MenuDispatchWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(CommonConsts.JSON_KEY_TENANT_CODE);
        this.menuCode = jsonPayload.getString(CommonConsts.JSON_KEY_MENU_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(menuCode)) {
            log.error("menuDispatch4InitWorker|init|illegalArgument|" + tenantCode + "|" + menuCode);
            throw new IllegalArgumentException("tenantCode or menuCode is blank");
        }
    }

    @Override
    public void run() {
        JSONObject dispatchCont = getDispatchCont();
        if (dispatchCont == null) {
            log.info("menuDispatchWorker|getDispatchCont|error|stopWorker|" + dispatchCont);
            return;
        }
        File outputFile = new File("dispatch/menu_output.json");
        boolean wrote = BizUtils.writeStrToFile(dispatchCont.toJSONString(), outputFile);
        if (!wrote) {
            log.info("menuDispatchWorker|writeStrToFile|failed|stopWorker");
            return;
        }
        String ossPath = uploadOSS(outputFile);
        if (StringUtils.isBlank(ossPath)) {
            log.info("menuDispatchWorker|uploadOSS|failed|stopWorker");
            return;
        }
        String md5AsHex = calcMD5Hex(outputFile);
        if (StringUtils.isBlank(md5AsHex)) {
            log.info("menuDispatchWorker|calcMD5Hex|failed|stopWorker");
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_MENU);
        jsonMsg.put(CommonConsts.JSON_KEY_MD5_AS_HEX, md5AsHex);
        jsonMsg.put(CommonConsts.JSON_KEY_OSS_PATH, ossPath);

        // 准备发送
        List<String> machineCodeList = getMachineCodeList();
        if (CollectionUtils.isEmpty(machineCodeList)) {
            log.info("menuDispatchWorker|getMachineCodeList|empty|stopWorker");
        }

        MqttProducer mqttProducer = SpringServiceUtils.getMqttProducer();
        for (String machineCode : machineCodeList) {
            mqttProducer.sendP2PMsgByTenant(tenantCode, machineCode, jsonMsg.toJSONString());
        }
    }

    private JSONObject getDispatchCont() {
        MenuMgtService menuMgtService = SpringServiceUtils.getMenuMgtService();
        MenuDTO menuDTO = getModel(menuMgtService.getByCode(tenantCode, menuCode));
        if (menuDTO == null) {
            log.info("menuDispatchWorker|getMenu|null|stopWorker");
            return null;
        }

        SeriesMgtService seriesMgtService = SpringServiceUtils.getSeriesMgtService();
        List<SeriesDTO> seriesDTOList = menuDTO.getMenuSeriesRelList().stream()
                .map(menuSeriesRelDTO -> {
                    SeriesDTO seriesDTO = getModel(seriesMgtService.getByCode(
                            tenantCode, menuSeriesRelDTO.getSeriesCode()));
                    return seriesDTO;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(seriesDTOList)) {
            log.info("menuDispatchWorker|getSeriesList|empty|stopWorker");
            return null;
        }
        List<String> teaCodeList = seriesDTOList.stream()
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
            log.info("menuDispatchWorker|getTeaList|empty|stopWorker");
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
        for (SeriesDTO seriesDTO : seriesDTOList) {
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

    private List<String> getMachineCodeList() {
        MenuDispatchAccessor menuDispatchAccessor = SpringAccessorUtils.getMenuDispatchAccessor();
        List<MenuDispatchPO> menuDispatchPOList = menuDispatchAccessor.listByMenuCode(
                tenantCode, menuCode);
        if (CollectionUtils.isEmpty(menuDispatchPOList)) {
            log.info("menuDispatchWorker|getDispatch|null|stopWorker");
            return null;
        }

        List<String> shopCodeList = DaoUtils.getShopCodeListByShopGroupCodeList(tenantCode,
                menuDispatchPOList.stream()
                        .map(MenuDispatchPO::getShopGroupCode)
                        .collect(Collectors.toList()));
        List<String> machineCodeList = DaoUtils.getMachineCodeListByShopCodeList(tenantCode, shopCodeList);
        return machineCodeList;
    }

    private String uploadOSS(File file) {
        String ossPath = null;
        try {
            ossPath = OSSUtils.uploadFile(file, OSSConfig.OSS_MENU_PATH);
        } catch (FileNotFoundException e) {
            log.info("menuDispatchWorker|uploadFileToOSS|fatal|" + e.getMessage());
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
            log.info("menuDispatchWorker|calcMD5Hex|fatal|" + e.getMessage());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.info("menuDispatchWorker|closeFileInputStream|fatal|" + e.getMessage());
                }
            }
        }
        return md5AsHex;
    }
}
