package com.langtuo.teamachine.biz.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.drink.TeaDTO;
import com.langtuo.teamachine.api.model.menu.MenuDTO;
import com.langtuo.teamachine.api.model.menu.SeriesDTO;
import com.langtuo.teamachine.api.model.menu.SeriesTeaRelDTO;
import com.langtuo.teamachine.biz.convert.drink.TeaMgtConvertor;
import com.langtuo.teamachine.biz.convert.menu.MenuMgtConvertor;
import com.langtuo.teamachine.biz.convert.menu.SeriesMgtConvertor;
import com.langtuo.teamachine.dao.accessor.drink.TeaAccessor;
import com.langtuo.teamachine.dao.accessor.menu.MenuAccessor;
import com.langtuo.teamachine.dao.accessor.menu.SeriesAccessor;
import com.langtuo.teamachine.dao.config.OSSConfig;
import com.langtuo.teamachine.dao.oss.OSSUtils;
import com.langtuo.teamachine.dao.po.drink.TeaPO;
import com.langtuo.teamachine.dao.po.menu.MenuPO;
import com.langtuo.teamachine.dao.po.menu.SeriesPO;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
public class BizUtils {
    public static String getMenuFileName(String menuCode, String menuGmtModifiedYMDHMS) {
        String fileName = CommonConsts.MENU_OUTPUT_FILENAME_PREFIX + menuCode
                + CommonConsts.HORIZONTAL_BAR + menuGmtModifiedYMDHMS
                + CommonConsts.MENU_OUTPUT_PATH_EXT;
        return fileName;
    }

    public static String getMenuListFileName(String shopGroupCode) {
        String fileName = CommonConsts.MENU_OUTPUT_FILENAME_PREFIX + shopGroupCode
                + CommonConsts.MENU_OUTPUT_PATH_EXT;
        return fileName;
    }

    public static JSONObject getMenuDispatchCont(String tenantCode, String menuCode) {
        MenuAccessor menuAccessor = SpringAccessorUtils.getMenuAccessor();
        MenuPO menuPO = menuAccessor.getByMenuCode(tenantCode, menuCode);
        MenuDTO menuDTO = MenuMgtConvertor.convertToMenuDTO(menuPO);
        if (menuDTO == null) {
            log.error("BizUtils|getMenu|null|stopWorker");
            return null;
        }

        List<SeriesDTO> seriesDTOList = menuDTO.getMenuSeriesRelList().stream()
                .map(menuSeriesRelDTO -> {
                    SeriesAccessor seriesAccessor = SpringAccessorUtils.getSeriesAccessor();
                    SeriesPO seriesPO = seriesAccessor.getBySeriesCode(tenantCode, menuSeriesRelDTO.getSeriesCode());
                    SeriesDTO seriesDTO = SeriesMgtConvertor.convertToSeriesDTO(seriesPO);
                    return seriesDTO;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(seriesDTOList)) {
            log.error("BizUtils|getSeriesList|empty|stopWorker");
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
            log.error("BizUtils|getTeaList|empty|stopWorker");
            return null;
        }

        List<TeaDTO> teaList = teaCodeList.stream()
                .map(teaCode -> {
                    TeaAccessor teaAccessor = SpringAccessorUtils.getTeaAccessor();
                    TeaPO teaPO = teaAccessor.getByTeaCode(tenantCode, teaCode);
                    TeaDTO teaDTO = TeaMgtConvertor.convertToTeaDTO(teaPO, true);
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
                JSONObject jsonTea = (JSONObject) JSON.toJSON(teaDTO);
                jsonTea.remove("actStepList");
                jsonTea.remove("specRuleList");
                seriesJSON.getJSONArray("teaList").add(jsonTea);
            }
            jsonMenu.getJSONArray("seriesList").add(seriesJSON);
        }
        return jsonMenu;
    }

    /**
     *
     * @param length
     * @return
     */
    public static String genRandomStr(int length) {
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CommonConsts.DEPLOY_CHAR_SET.length());
            char randomChar = CommonConsts.DEPLOY_CHAR_SET.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static boolean writeStrToFile(String outputCont, File outputFile) {
        if (StringUtils.isBlank(outputCont) || outputCont == null) {
            return false;
        }

        if (outputFile.exists()) {
            boolean deleted = outputFile.delete();
            if (!deleted) {
                return false;
            }
        }

        try {
            boolean created = outputFile.createNewFile();
            if (!created) {
                return false;
            }
        } catch (IOException e) {
            log.error("bizUtils|createNewFile|fatal|" + e.getMessage(), e);
            return false;
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(outputFile);
            writer.write(outputCont);
            writer.flush();
            return true;
        } catch (IOException e) {
            log.error("bizUtils|writeFile|fatal|" + e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("bizUtils|closeWriter|fatal|" + e.getMessage(), e);
                }
            }
        }
        return false;
    }

    public static String uploadOSS(File file) {
        String ossPath = null;
        try {
            ossPath = OSSUtils.uploadFile(file, OSSConfig.OSS_MENU_PATH);
        } catch (FileNotFoundException e) {
            log.error("bizUtils|uploadFileToOSS|fatal|" + e.getMessage());
        }
        return ossPath;
    }

    public static String calcMD5Hex(File file) {
        String md5AsHex = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            md5AsHex = DigestUtils.md5DigestAsHex(fileInputStream);
        } catch (IOException e) {
            log.error("bizUtils|calcMD5Hex|fatal|" + e.getMessage());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("bizUtils|closeFileInputStream|fatal|" + e.getMessage());
                }
            }
        }
        return md5AsHex;
    }

    public static int calcRandom(int min, int max) {
        Random random = new Random(System.currentTimeMillis());
        int randomNumber = random.nextInt(max - min + 1) + min;
        return randomNumber;
    }
}
