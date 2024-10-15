package com.langtuo.teamachine.biz.service.device;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.device.DeployDTO;
import com.langtuo.teamachine.api.request.device.DeployPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.DeployMgtService;
import com.langtuo.teamachine.biz.excel.ExcelHandlerFactory;
import com.langtuo.teamachine.biz.util.BizUtils;
import com.langtuo.teamachine.dao.accessor.device.DeployAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.user.TenantAccessor;
import com.langtuo.teamachine.dao.po.device.DeployPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.LocaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.langtuo.teamachine.biz.convert.device.DeployMgtConvertor.convertToDeployPO;

@Component
@Slf4j
public class DeployMgtServiceImpl implements DeployMgtService {
    @Resource
    private DeployAccessor deployAccessor;

    @Resource
    private TenantAccessor tenantAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private ExcelHandlerFactory excelHandlerFactory;

    @Override
    public TeaMachineResult<PageDTO<DeployDTO>> search(String tenantCode, String deployCode, String machineCode,
            String shopCode, Integer state, int pageNum, int pageSize) {
        if (StringUtils.isBlank(tenantCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            return doSearch(tenantCode, deployCode, machineCode, shopCode, state, pageNum, pageSize);
        } catch (Exception e) {
            log.error("deployMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<DeployDTO> getByDeployCode(String tenantCode, String deployCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(deployCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            return doGetByDeployCode(tenantCode, deployCode);
        } catch (Exception e) {
            log.error("deployMgtService|getByCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<DeployDTO> getByMachineCode(String tenantCode, String machineCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            return doGetByMachineCode(tenantCode, machineCode);
        } catch (Exception e) {
            log.error("deployMgtService|getByMachineCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(DeployPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        DeployPO po = convertToDeployPO(request);
        try {
            if (request.isPutNew()) {
                return doPutNew(po);
            } else {
                return doPutUpdate(po);
            }
        } catch (Exception e) {
            log.error("deployMgtService|put|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> deleteByDeployCode(String tenantCode, String deployCode) {
        if (StringUtils.isEmpty(tenantCode) || StringUtils.isBlank(deployCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            return doDeleteByDeployCode(tenantCode, deployCode);
        } catch (Exception e) {
            log.error("deployMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<String> generateDeployCode() {
        try {
            String deployCode = BizUtils.genRandomStr(6);
            return TeaMachineResult.success(deployCode);
        } catch (Exception e) {
            log.error("deployMgtService|generateDeployCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<String> generateMachineCode() {
        try {
            long machineCodeSeqVal = deployAccessor.getNextSeqVal4MachineCode();
            String machineCode = String.valueOf(machineCodeSeqVal);
            long needSupplyCnt = CommonConsts.DEPLOY_CODE_LENGTH - machineCode.length();
            if (machineCode.length() < CommonConsts.DEPLOY_CODE_LENGTH) {
                for (int i = 0; i < needSupplyCnt; i++) {
                    machineCode = CommonConsts.DEPLOY_CODE_COVERING_NUM + machineCode;
                }
            }
            // 获取当前日期
            Calendar calendar = Calendar.getInstance();
            // 创建一个SimpleDateFormat对象，指定目标格式
            SimpleDateFormat sdf = new SimpleDateFormat(CommonConsts.DATE_FORMAT_SIMPLE);
            // 格式化日期
            String formattedDate = sdf.format(calendar.getTime());
            machineCode = formattedDate + machineCode;

            return TeaMachineResult.success(machineCode);
        } catch (Exception e) {
            log.error("deployMgtService|generateMachineCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doDeleteByDeployCode(String tenantCode, String deployCode) {
        int deleted = deployAccessor.deleteByDeployCode(tenantCode, deployCode);
        return TeaMachineResult.success();
    }

    @Transactional(readOnly = true)
    private TeaMachineResult<PageDTO<DeployDTO>> doSearch(String tenantCode, String deployCode, String machineCode,
                                                          String shopCode, Integer state, int pageNum, int pageSize) {
        PageInfo<DeployPO> pageInfo = deployAccessor.search(tenantCode, deployCode, machineCode,
                shopCode, state, pageNum, pageSize);

        List<DeployDTO> dtoList = convertToDeployPO(pageInfo.getList());
        return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
    }

    @Transactional(readOnly = true)
    private TeaMachineResult<DeployDTO> doGetByDeployCode(String tenantCode, String deployCode) {
        DeployDTO dto = convertToDeployPO(deployAccessor.getByDeployCode(tenantCode, deployCode));
        return TeaMachineResult.success(dto);
    }

    @Transactional(readOnly = true)
    private TeaMachineResult<DeployDTO> doGetByMachineCode(String tenantCode, String machineCode) {
        DeployDTO dto = convertToDeployPO(deployAccessor.getByMachineCode(tenantCode, machineCode));
        return TeaMachineResult.success(dto);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutNew(DeployPO po) {
        DeployPO exist = deployAccessor.getByDeployCode(po.getTenantCode(), po.getDeployCode());
        if (exist != null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
        }

        int inserted = deployAccessor.insert(po);
        if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
            log.error("deployMgtService|doPutNew|error|" + inserted);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutUpdate(DeployPO po) {
        DeployPO exist = deployAccessor.getByDeployCode(po.getTenantCode(), po.getDeployCode());
        if (exist == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
        }

        int updated = deployAccessor.update(po);
        if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
            log.error("deployMgtService|doPutUpdate|error|" + updated);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
        return TeaMachineResult.success();
    }

    @Override
    public TeaMachineResult<XSSFWorkbook> exportByExcel(String tenantCode) {
        XSSFWorkbook xssfWorkbook = excelHandlerFactory.getExcelHandler(tenantCode)
                .getDeployHandler()
                .export(tenantCode);
        return TeaMachineResult.success(xssfWorkbook);
    }
}
