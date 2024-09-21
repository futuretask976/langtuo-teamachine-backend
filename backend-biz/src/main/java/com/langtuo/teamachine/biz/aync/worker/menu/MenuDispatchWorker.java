package com.langtuo.teamachine.biz.aync.worker.menu;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.biz.util.BizUtils;
import com.langtuo.teamachine.biz.util.SpringServiceUtils;
import com.langtuo.teamachine.dao.accessor.menu.MenuDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.menu.MenuDispatchHistoryAccessor;
import com.langtuo.teamachine.dao.config.OSSConfig;
import com.langtuo.teamachine.dao.oss.OSSUtils;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchHistoryPO;
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
import java.util.stream.Collectors;

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

    /**
     * 菜单的修改时间
     */
    private String menuGmtModifiedYMDHMS;

    public MenuDispatchWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(CommonConsts.JSON_KEY_TENANT_CODE);
        this.menuCode = jsonPayload.getString(CommonConsts.JSON_KEY_MENU_CODE);
        this.menuGmtModifiedYMDHMS = jsonPayload.getString(CommonConsts.JSON_KEY_MENU_GMTMODIFIED_YMDHMS);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(menuCode)
                || StringUtils.isBlank(menuGmtModifiedYMDHMS)) {
            log.error("menuDispatchWorker|init|illegalArgument|" + tenantCode + "|" + menuCode);
            throw new IllegalArgumentException("tenantCode or menuCode is blank");
        }
    }

    @Override
    public void run() {
        String fileName = getMenuFileName();
        File tmpFile = new File(CommonConsts.MENU_OUTPUT_PATH + fileName);
        try {
            MenuDispatchHistoryAccessor menuDispatchHistoryAccessor = SpringAccessorUtils.getMenuDispatchHistoryAccessor();
            MenuDispatchHistoryPO existOssPO = menuDispatchHistoryAccessor.getByFileName(tenantCode,
                    CommonConsts.MENU_DISPATCH_LIST_FALSE, fileName);
            if (existOssPO != null) {
                sendToMachine(getSendMsg(existOssPO));
                return;
            }

            if (tmpFile.exists()) {
                log.info("menuDispatchWorker|tmpFileCheck|exist|stopWorker|" + tmpFile.getAbsolutePath());
                return;
            }

            JSONObject dispatchCont = BizUtils.getMenuDispatchCont(tenantCode, menuCode);
            if (dispatchCont == null) {
                return;
            }

            boolean wrote = BizUtils.writeStrToFile(dispatchCont.toJSONString(), tmpFile);
            if (!wrote) {
                log.info("menuDispatchWorker|writeStrToFile|failed|stopWorker|" + tmpFile.getAbsolutePath());
                return;
            }
            String md5AsHex = calcMD5Hex(tmpFile);
            if (StringUtils.isBlank(md5AsHex)) {
                log.info("menuDispatchWorker|calcMD5Hex|failed|stopWorker");
                return;
            }
            String ossPath = uploadOSS(tmpFile);
            if (StringUtils.isBlank(ossPath)) {
                log.info("menuDispatchWorker|uploadOSS|failed|stopWorker");
                return;
            }

            MenuDispatchHistoryPO newOssPO = getNewOssPO(fileName, md5AsHex);
            int inserted = menuDispatchHistoryAccessor.insert(newOssPO);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("menuDispatchWorker|insertOssInfo|error|" + inserted);
            }

            sendToMachine(getSendMsg(newOssPO));
        } catch (Exception e) {
            log.error("menuDispatchWorker|run|fatal|" + e.getMessage(), e);
        } finally {
            tmpFile.delete();
        }
    }

    private List<String> getMachineCodeList() {
        MenuDispatchAccessor menuDispatchAccessor = SpringAccessorUtils.getMenuDispatchAccessor();
        List<MenuDispatchPO> menuDispatchPOList = menuDispatchAccessor.listByMenuCode(
                tenantCode, menuCode);
        if (CollectionUtils.isEmpty(menuDispatchPOList)) {
            log.info("menuDispatchWorker|getDispatch|null|stopWorker");
            return null;
        }

        List<String> shopCodeList = DaoUtils.getShopCodeListByShopGroupCode(tenantCode,
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

    private JSONObject getSendMsg(MenuDispatchHistoryPO po) {
        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_MENU);
        jsonMsg.put(CommonConsts.JSON_KEY_MD5_AS_HEX, po.getMd5());
        jsonMsg.put(CommonConsts.JSON_KEY_OSS_PATH,
                OSSConfig.OSS_MENU_PATH + OSSConfig.OSS_PATH_SEPARATOR + po.getFileName());
        return jsonMsg;
    }

    private void sendToMachine(JSONObject jsonMsg) {
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

    private String getMenuFileName() {
        String fileName = CommonConsts.MENU_OUTPUT_FILENAME_PREFIX + menuCode
                + CommonConsts.HORIZONTAL_BAR + menuGmtModifiedYMDHMS
                + CommonConsts.MENU_OUTPUT_PATH_EXT;
        return fileName;
    }

    private MenuDispatchHistoryPO getNewOssPO(String fileName, String md5AsHex) {
        MenuDispatchHistoryPO newOssPO = new MenuDispatchHistoryPO();
        newOssPO.setTenantCode(tenantCode);
        newOssPO.setInit(CommonConsts.MENU_DISPATCH_LIST_FALSE);
        newOssPO.setFileName(fileName);
        newOssPO.setMd5(md5AsHex);
        return newOssPO;
    }
}
