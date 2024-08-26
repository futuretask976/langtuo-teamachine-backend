package com.langtuo.teamachine.biz.service.impl.device;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.device.DeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.device.DeployPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.DeployMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.DeployUtils;
import com.langtuo.teamachine.dao.accessor.device.DeployAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.user.TenantAccessor;
import com.langtuo.teamachine.dao.po.device.DeployPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import com.langtuo.teamachine.dao.po.user.TenantPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DeployMgtServiceImpl implements DeployMgtService {
    @Resource
    private DeployAccessor deployAccessor;

    @Resource
    private TenantAccessor tenantAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Override
    public TeaMachineResult<PageDTO<DeployDTO>> search(String tenantCode, String deployCode, String machineCode,
            String shopName, Integer state, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<DeployDTO>> teaMachineResult;
        try {
            PageInfo<DeployPO> pageInfo = deployAccessor.search(tenantCode, deployCode, machineCode,
                    shopName, state, pageNum, pageSize);
            List<DeployDTO> dtoList = convertToDeployDTO(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<DeployDTO> getByDeployCode(String tenantCode, String deployCode) {
        TeaMachineResult<DeployDTO> teaMachineResult;
        try {
            DeployDTO dto = convertToDeployDTO(deployAccessor.selectOneByDeployCode(tenantCode, deployCode));
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("get error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<DeployDTO> getByMachineCode(String tenantCode, String machineCode) {
        TeaMachineResult<DeployDTO> teaMachineResult;
        try {
            DeployDTO dto = convertToDeployDTO(deployAccessor.selectOneByMachineCode(tenantCode, machineCode));
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("get error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(DeployPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        DeployPO deployPO = convertToDeployDTO(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            DeployPO exist = deployAccessor.selectOneByDeployCode(deployPO.getTenantCode(), deployPO.getDeployCode());
            if (exist != null) {
                int updated = deployAccessor.update(deployPO);
            } else {
                int inserted = deployAccessor.insert(deployPO);
            }
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String deployCode) {
        if (StringUtils.isEmpty(tenantCode) || StringUtils.isBlank(deployCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = deployAccessor.deleteByDeployCode(tenantCode, deployCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<String> generateDeployCode() {
        TeaMachineResult<String> teaMachineResult;
        try {
            String deployCode = DeployUtils.genRandomStr(20);
            teaMachineResult = TeaMachineResult.success(deployCode);
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<String> generateMachineCode() {
        TeaMachineResult<String> teaMachineResult;
        try {
            long machineCodeSeqVal = deployAccessor.selectNextSeqVal4MachineCode();
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

            teaMachineResult = TeaMachineResult.success(machineCode);
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    public TeaMachineResult<XSSFWorkbook> exportByExcel(String tenantCode) {
        // 创建一个新的工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建一个工作表
        Sheet sheet = workbook.createSheet(BizConsts.SHEET_NAME_4_DEPLOY_EXPORT);
        // 创建标题行（0基索引）
        Row row = sheet.createRow(BizConsts.ROW_NUM_4_TITLE);
        // 创建单元格并设置值
        for (int i = 0; i < BizConsts.TITLE_LIST_4_DEPLOY_EXPORT.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(BizConsts.TITLE_LIST_4_DEPLOY_EXPORT.get(i));
        }

        List<DeployPO> deployPOList = deployAccessor.selectList(tenantCode);
        int lineIndex = BizConsts.ROW_START_NUM_4_DEPLOY;
        for (DeployPO deployPO : deployPOList) {
            Row dataRow = sheet.createRow(lineIndex++);
            int columnIndex = BizConsts.COL_START_NUM_4_DEPLOY;

            // 添加创建日期
            Cell cell4GmtCreated = dataRow.createCell(columnIndex++);
            cell4GmtCreated.setCellValue(transform(deployPO.getGmtCreated()));
            // 添加修改日期
            Cell cell4GmtModified = dataRow.createCell(columnIndex++);
            cell4GmtModified.setCellValue(transform(deployPO.getGmtModified()));
            // 添加商户编码
            Cell cell4TenantCode = dataRow.createCell(columnIndex++);
            cell4TenantCode.setCellValue(deployPO.getTenantCode());
            // 添加商户名称
            Cell cell4TenantName = dataRow.createCell(columnIndex++);
            TenantPO tenantPO = tenantAccessor.selectOneByTenantCode(tenantCode);
            if (tenantPO != null) {
                cell4TenantName.setCellValue(tenantPO.getTenantName());
            }
            // 添加部署编码
            Cell cell4DeployCode = dataRow.createCell(columnIndex++);
            cell4DeployCode.setCellValue(deployPO.getDeployCode());
            // 添加机器编码
            Cell cell4MachineCode = dataRow.createCell(columnIndex++);
            cell4MachineCode.setCellValue(deployPO.getMachineCode());
            // 添加型号编码
            Cell cell4ModelCode = dataRow.createCell(columnIndex++);
            cell4ModelCode.setCellValue(deployPO.getModelCode());
            // 添加店铺编码
            Cell cell4ShopCode = dataRow.createCell(columnIndex++);
            cell4ShopCode.setCellValue(deployPO.getShopCode());
            // 添加店铺名称
            Cell cell4ShopName = dataRow.createCell(columnIndex++);
            ShopPO shopPO = shopAccessor.selectOneByShopCode(tenantCode, deployPO.getShopCode());
            if (shopPO != null) {
                cell4ShopName.setCellValue(shopPO.getShopName());
            }
            // 添加部署状态
            Cell cell4State = dataRow.createCell(columnIndex++);
            cell4State.setCellValue(deployPO.getState() == BizConsts.DEPLOY_STATE_DISABLED ?
                    BizConsts.DEPLOY_STATE_DISABLED_LABEL : BizConsts.DEPLOY_STATE_ENABLED_LABEL);
        }
        return TeaMachineResult.success(workbook);
    }

    private DeployPO convertToDeployDTO(DeployPutRequest request) {
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

    private List<DeployDTO> convertToDeployDTO(List<DeployPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<DeployDTO> list = poList.stream()
                .map(po -> convertToDeployDTO(po))
                .collect(Collectors.toList());
        return list;
    }

    private DeployDTO convertToDeployDTO(DeployPO po) {
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

        ShopPO shopPO = shopAccessor.selectOneByShopCode(po.getTenantCode(), po.getShopCode());
        if (shopPO != null) {
            dto.setShopCode(shopPO.getShopCode());
            dto.setShopName(shopPO.getShopName());
        }
        return dto;
    }

    public static String transform(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(date);
    }
}
