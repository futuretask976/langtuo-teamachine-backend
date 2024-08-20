package com.langtuo.teamachine.biz.service.impl.device;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.device.DeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.model.user.TenantDTO;
import com.langtuo.teamachine.api.request.device.DeployPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.device.DeployMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.api.service.user.TenantMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.DeployUtils;
import com.langtuo.teamachine.dao.accessor.device.DeployAccessor;
import com.langtuo.teamachine.dao.po.device.DeployPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.*;

@Component
@Slf4j
public class DeployMgtServiceImpl implements DeployMgtService {
    @Resource
    private DeployAccessor deployAccessor;

    @Resource
    private TenantMgtService tenantMgtService;

    @Resource
    private ShopMgtService shopMgtService;

    @Override
    public LangTuoResult<PageDTO<DeployDTO>> search(String tenantCode, String deployCode, String machineCode,
            String shopName, Integer state, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        LangTuoResult<PageDTO<DeployDTO>> langTuoResult;
        try {
            PageInfo<DeployPO> pageInfo = deployAccessor.search(tenantCode, deployCode, machineCode,
                    shopName, state, pageNum, pageSize);
            List<DeployDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<DeployDTO> getByDeployCode(String tenantCode, String deployCode) {
        LangTuoResult<DeployDTO> langTuoResult;
        try {
            DeployDTO dto = convert(deployAccessor.selectOneByDeployCode(tenantCode, deployCode));
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("get error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<DeployDTO> getByMachineCode(String tenantCode, String machineCode) {
        LangTuoResult<DeployDTO> langTuoResult;
        try {
            DeployDTO dto = convert(deployAccessor.selectOneByMachineCode(tenantCode, machineCode));
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("get error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(DeployPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        DeployPO deployPO = convert(request);

        LangTuoResult<Void> langTuoResult;
        try {
            DeployPO exist = deployAccessor.selectOneByDeployCode(deployPO.getTenantCode(), deployPO.getDeployCode());
            if (exist != null) {
                int updated = deployAccessor.update(deployPO);
            } else {
                int inserted = deployAccessor.insert(deployPO);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String deployCode) {
        if (StringUtils.isEmpty(tenantCode) || StringUtils.isBlank(deployCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult;
        try {
            int deleted = deployAccessor.delete(tenantCode, deployCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<String> generateDeployCode() {
        LangTuoResult<String> langTuoResult;
        try {
            String deployCode = DeployUtils.genRandomStr(20);
            langTuoResult = LangTuoResult.success(deployCode);
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<String> generateMachineCode() {
        LangTuoResult<String> langTuoResult;
        try {
            long machineCodeSeqVal = deployAccessor.getMachineCodeNextSeqVal();
            String machineCode = String.valueOf(machineCodeSeqVal);
            long needSupplyCnt = 6 - machineCode.length();
            if (machineCode.length() < 6) {
                for (int i = 0; i < needSupplyCnt; i++) {
                    machineCode = "0" + machineCode;
                }
            }
            // 获取当前日期
            Calendar calendar = Calendar.getInstance();
            // 创建一个SimpleDateFormat对象，指定目标格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // 格式化日期
            String formattedDate = sdf.format(calendar.getTime());
            machineCode = formattedDate + machineCode;

            langTuoResult = LangTuoResult.success(machineCode);
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    public LangTuoResult<XSSFWorkbook> exportByExcel(String tenantCode) {
        // 创建一个新的工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建一个工作表
        Sheet sheet = workbook.createSheet("部署信息导出");
        // 标题内容
        List<String> titleList = Lists.newArrayList(
                "创建时间",
                "修改时间",
                "商户编码",
                "部署编码",
                "机器编码",
                "型号编码",
                "店铺编码",
                "状态");
        // 创建标题行（0基索引）
        Row row = sheet.createRow(0);
        // 创建单元格并设置值
        for (int i = 0; i < titleList.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titleList.get(i));
        }

        List<DeployPO> deployPOList = deployAccessor.selectList(tenantCode);
        int lineIndex = 1;
        for (DeployPO deployPO : deployPOList) {
            Row dataRow = sheet.createRow(lineIndex++);
            int columnIndex = 0;
            Cell cell;
            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(transform(deployPO.getGmtCreated(), "yyyy-MM-dd hh:mm:ss"));
            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(transform(deployPO.getGmtModified(), "yyyy-MM-dd hh:mm:ss"));
            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(deployPO.getTenantCode());
            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(deployPO.getDeployCode());

            cell = dataRow.createCell(columnIndex++);
            TenantDTO tenantDTO = getModel(tenantMgtService.get(tenantCode));
            if (tenantDTO == null) {
                cell.setCellValue("");
            } else {
                cell.setCellValue(tenantDTO.getTenantName());
            }

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(deployPO.getModelCode());

            cell = dataRow.createCell(columnIndex++);
            ShopDTO shopDTO = getModel(shopMgtService.getByCode(tenantCode, deployPO.getShopCode()));
            if (shopDTO == null) {
                cell.setCellValue("");
            } else {
                cell.setCellValue(shopDTO.getShopName());
            }

            cell = dataRow.createCell(columnIndex++);
            cell.setCellValue(deployPO.getState() == 0 ? "未部署" : "已部署");
        }
        return LangTuoResult.success(workbook);
    }

    public LangTuoResult<String> genRandomStr() {
        String randomStr = DeployUtils.genRandomStr(20);
        return LangTuoResult.success(randomStr);
    }

    private DeployPO convert(DeployPutRequest request) {
        if (request == null) {
            return null;
        }

        DeployPO po = new DeployPO();
        po.setDeployCode(request.getDeployCode());
        po.setModelCode(request.getModelCode());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setState(request.getState());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }

    private List<DeployDTO> convert(List<DeployPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<DeployDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private DeployDTO convert(DeployPO po) {
        if (po == null) {
            return null;
        }

        DeployDTO dto = new DeployDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setDeployCode(po.getDeployCode());
        dto.setMachineCode(po.getMachineCode());
        dto.setModelCode(po.getModelCode());
        dto.setState(po.getState());
        dto.setExtraInfo(po.getExtraInfo());

        ShopDTO shopDTO = getModel(shopMgtService.getByCode(po.getTenantCode(), po.getShopCode()));
        if (shopDTO != null) {
            dto.setShopCode(shopDTO.getShopCode());
            dto.setShopName(shopDTO.getShopName());
        }
        return dto;
    }

    public static boolean removeFile(File root) {
        if (root == null || !root.exists()) {
            return true;
        }

        boolean result = false;
        if (root.isFile()) {
            result = root.delete();
        } else {
            File[] files = root.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                result = removeFile(file);
            }
            if (result) {
                result = root.delete();
            }
        }
        return result;
    }

    public static String transform(Date date, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd hh:mm:ss";
        }

        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
