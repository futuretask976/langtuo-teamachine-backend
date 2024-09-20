package com.langtuo.teamachine.biz.service.device;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.device.DeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
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
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

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

    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<PageDTO<DeployDTO>> search(String tenantCode, String deployCode, String machineCode,
            String shopCode, Integer state, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<DeployPO> pageInfo = deployAccessor.search(tenantCode, deployCode, machineCode,
                    shopCode, state, pageNum, pageSize);
            List<DeployDTO> dtoList = convertToDeployPO(pageInfo.getList());
            return TeaMachineResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("deployMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<DeployDTO> getByCode(String tenantCode, String deployCode) {
        try {
            DeployDTO dto = convertToDeployPO(deployAccessor.getByDeployCode(tenantCode, deployCode));
            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("deployMgtService|getByCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<DeployDTO> getByMachineCode(String tenantCode, String machineCode) {
        try {
            DeployDTO dto = convertToDeployPO(deployAccessor.getByMachineCode(tenantCode, machineCode));
            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("deployMgtService|getByMachineCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(DeployPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        DeployPO po = convertToDeployPO(request);
        if (request.isPutNew()) {
            return putNew(po);
        } else {
            return putUpdate(po);
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String deployCode) {
        if (StringUtils.isEmpty(tenantCode) || StringUtils.isBlank(deployCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int deleted = deployAccessor.deleteByDeployCode(tenantCode, deployCode);
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("deployMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<String> generateDeployCode() {
        try {
            String deployCode = BizUtils.genRandomStr(6);
            return TeaMachineResult.success(deployCode);
        } catch (Exception e) {
            log.error("deployMgtService|generateDeployCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
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
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putNew(DeployPO po) {
        try {
            DeployPO exist = deployAccessor.getByDeployCode(po.getTenantCode(), po.getDeployCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = deployAccessor.insert(po);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("deployMgtService|putNew|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("deployMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(DeployPO po) {
        try {
            DeployPO exist = deployAccessor.getByDeployCode(po.getTenantCode(), po.getDeployCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = deployAccessor.update(po);
            if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
                log.error("deployMgtService|putUpdate|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("deployMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    public TeaMachineResult<XSSFWorkbook> exportByExcel(String tenantCode) {
        XSSFWorkbook xssfWorkbook = excelHandlerFactory.getExcelHandler(tenantCode)
                .getDeployHandler()
                .export(tenantCode);
        return TeaMachineResult.success(xssfWorkbook);
    }
}
