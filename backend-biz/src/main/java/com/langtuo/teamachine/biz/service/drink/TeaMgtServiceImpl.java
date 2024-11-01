package com.langtuo.teamachine.biz.service.drink;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.TeaDTO;
import com.langtuo.teamachine.api.request.drink.TeaPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import com.langtuo.teamachine.biz.excel.ExcelHandlerFactory;
import com.langtuo.teamachine.dao.accessor.drink.*;
import com.langtuo.teamachine.dao.accessor.menu.MenuDispatchCacheAccessor;
import com.langtuo.teamachine.dao.accessor.menu.SeriesTeaRelAccessor;
import com.langtuo.teamachine.dao.po.drink.*;
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
import java.util.List;

import static com.langtuo.teamachine.biz.convert.drink.TeaMgtConvertor.*;

@Component
@Slf4j
public class TeaMgtServiceImpl implements TeaMgtService {
    @Resource
    private TeaAccessor teaAccessor;

    @Resource
    private TeaUnitAccessor teaUnitAccessor;

    @Resource
    private SpecItemRuleAccessor specItemRuleAccessor;

    @Resource
    private ToppingAdjustRuleAccessor toppingAdjustRuleAccessor;

    @Resource
    private ToppingBaseRuleAccessor toppingBaseRuleAccessor;

    @Resource
    private SeriesTeaRelAccessor seriesTeaRelAccessor;

    @Resource
    private MenuDispatchCacheAccessor menuDispatchCacheAccessor;

    @Resource
    private ExcelHandlerFactory excelHandlerFactory;

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<TeaDTO> getByTeaCode(String tenantCode, String teaCode) {
        try {
            TeaPO po = teaAccessor.getByTeaCode(tenantCode, teaCode);
            TeaDTO dto = convertToTeaDTO(po, true);
            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("|teaMgtService|getByCode|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<List<TeaDTO>> list(String tenantCode) {
        try {
            List<TeaPO> teaPOList = teaAccessor.list(tenantCode);
            List<TeaDTO> teaDTOList = convertToTeaDTO(teaPOList, false);
            return TeaMachineResult.success(teaDTOList);
        } catch (Exception e) {
            log.error("|teaMgtService|list|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<PageDTO<TeaDTO>> search(String tenantName, String teaCode, String teaName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<TeaPO> pageInfo = teaAccessor.search(tenantName, teaCode, teaName,
                    pageNum, pageSize);
            List<TeaDTO> dtoList = convertToTeaDTO(pageInfo.getList(), false);
            return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("|teaMgtService|search|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(TeaPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaPO teaPO = convertToTeaPO(request);
        List<ToppingBaseRulePO> toppingBaseRulePOList = convertToToppingBaseRuleDTO(request);
        List<SpecItemRulePO> specItemRulePOList = convertToTeaSpecItemPO(request);
        List<TeaUnitPO> teaUnitPOList = convertToTeaUnitPO(request);
        List<ToppingAdjustRulePO> toppingAdjustRulePOList = convertToToppingAdjustRulePO(request);

        try {
            if (request.isPutImport()) {
                return doPutImport(teaPO, toppingBaseRulePOList, specItemRulePOList, teaUnitPOList,
                        toppingAdjustRulePOList);
            } else if (request.isPutNew()) {
                return doPutNew(teaPO, toppingBaseRulePOList, specItemRulePOList, teaUnitPOList,
                        toppingAdjustRulePOList);
            } else {
                return doPutUpdate(teaPO, toppingBaseRulePOList, specItemRulePOList, teaUnitPOList,
                        toppingAdjustRulePOList);
            }
        } catch (Exception e) {
            log.error("|teaMgtService|put|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutImport(TeaPO teaPO, List<ToppingBaseRulePO> toppingBaseRulePOList,
            List<SpecItemRulePO> specItemRulePOList, List<TeaUnitPO> teaUnitPOList,
            List<ToppingAdjustRulePO> toppingAdjustRulePOList) {
        int deleted4Tea = teaAccessor.deleteByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
        if (CommonConsts.DB_DELETED_ONE_ROW != deleted4Tea) {
            log.error("|teaMgtService|doPutImport|deleteTeaError|" + deleted4Tea);
        }

        int inserted4Tea = teaAccessor.insert(teaPO);
        if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4Tea) {
            log.error("|teaMgtService|doPutImport|insertTeaError|" + inserted4Tea);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }

        update4Tea(teaPO, toppingBaseRulePOList, specItemRulePOList, teaUnitPOList, toppingAdjustRulePOList);

        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutNew(TeaPO teaPO, List<ToppingBaseRulePO> toppingBaseRulePOList,
            List<SpecItemRulePO> specItemRulePOList, List<TeaUnitPO> teaUnitPOList,
            List<ToppingAdjustRulePO> toppingAdjustRulePOList) {
        TeaPO exist = teaAccessor.getByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
        if (exist != null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
        }

        int inserted4Tea = teaAccessor.insert(teaPO);
        if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4Tea) {
            log.error("|teaMgtService|putNewTea|error|" + inserted4Tea);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }

        update4Tea(teaPO, toppingBaseRulePOList, specItemRulePOList, teaUnitPOList, toppingAdjustRulePOList);

        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutUpdate(TeaPO teaPO, List<ToppingBaseRulePO> toppingBaseRulePOList,
            List<SpecItemRulePO> specItemRulePOList, List<TeaUnitPO> teaUnitPOList,
            List<ToppingAdjustRulePO> toppingAdjustRulePOList) {
        TeaPO exist = teaAccessor.getByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
        if (exist == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
        }

        int updated4Tea = teaAccessor.update(teaPO);
        if (CommonConsts.DB_UPDATED_ONE_ROW != updated4Tea) {
            log.error("|teaMgtService|putUpdateTea|error|" + JSONObject.toJSONString(teaPO));
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }

        update4Tea(teaPO, toppingBaseRulePOList, specItemRulePOList, teaUnitPOList, toppingAdjustRulePOList);

        return TeaMachineResult.success();
    }

    private void update4Tea(TeaPO teaPO, List<ToppingBaseRulePO> toppingBaseRulePOList,
            List<SpecItemRulePO> specItemRulePOList, List<TeaUnitPO> teaUnitPOList,
            List<ToppingAdjustRulePO> toppingAdjustRulePOList) {
        int deleted4ToppingBaseRule = toppingBaseRuleAccessor.deleteByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
        for (ToppingBaseRulePO toppingBaseRulePO : toppingBaseRulePOList) {
            int inserted4ToppingBaseRule = toppingBaseRuleAccessor.insert(toppingBaseRulePO);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4ToppingBaseRule) {
                log.error("|teaMgtService|updateToppingBaseRule|error|" +
                        JSONObject.toJSONString(toppingBaseRulePO));
            }
        }

        int deleted4TeaSpecItem = specItemRuleAccessor.deleteByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
        for (SpecItemRulePO specItemRulePO : specItemRulePOList) {
            int inserted4TeaSpecItem = specItemRuleAccessor.insert(specItemRulePO);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4TeaSpecItem) {
                log.error("|teaMgtService|updateTeaSpecItem|error|" + JSONObject.toJSONString(specItemRulePO));
            }
        }

        int deleted4TeaUnit = teaUnitAccessor.deleteByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
        for (TeaUnitPO teaUnitPO : teaUnitPOList) {
            int inserted4TeaUnit = teaUnitAccessor.insert(teaUnitPO);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4TeaUnit) {
                log.error("|teaMgtService|updateTeaUnit|error|" + JSONObject.toJSONString(teaUnitPO));
            }
        }

        int deleted4ToppingAdjustRule = toppingAdjustRuleAccessor.deleteByTeaCode(teaPO.getTenantCode(), teaPO.getTeaCode());
        for (ToppingAdjustRulePO toppingAdjustRulePO : toppingAdjustRulePOList) {
            int inserted4ToppingAdjustRule = toppingAdjustRuleAccessor.insert(toppingAdjustRulePO);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4ToppingAdjustRule) {
                log.error("|teaMgtService|updateToppingAdjustRule|error|" +
                        JSONObject.toJSONString(toppingAdjustRulePO));
            }
        }

        int cleared = menuDispatchCacheAccessor.clear(teaPO.getTenantCode());
    }

    @Override
    public TeaMachineResult<Void> deleteByTeaCode(String tenantCode, String teaCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            return doDeleteByTeaCode(tenantCode, teaCode);
        } catch (Exception e) {
            log.error("|teaMgtService|delete|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doDeleteByTeaCode(String tenantCode, String teaCode) {
        int count = seriesTeaRelAccessor.countByTeaCode(tenantCode, teaCode);
        if (CommonConsts.DB_SELECT_ZERO_ROW != count) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(
                    ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
        }

        int deleted4Tea = teaAccessor.deleteByTeaCode(tenantCode, teaCode);
        int deleted4TeaUnit = teaUnitAccessor.deleteByTeaCode(tenantCode, teaCode);
        int deleted4ToppingBaseRule = toppingBaseRuleAccessor.deleteByTeaCode(tenantCode, teaCode);
        int deleted4ToppingAdjustRule = toppingAdjustRuleAccessor.deleteByTeaCode(tenantCode, teaCode);

        int cleared = menuDispatchCacheAccessor.clear(tenantCode);

        return TeaMachineResult.success();
    }

    @Override
    public TeaMachineResult<XSSFWorkbook> exportByExcel(String tenantCode) {
        XSSFWorkbook xssfWorkbook = excelHandlerFactory.getExcelHandler(tenantCode)
                .getTeaHandler()
                .export(tenantCode);
        return TeaMachineResult.success(xssfWorkbook);
    }

    @Override
    public TeaMachineResult<Void> importByExcel(String tenantCode, XSSFWorkbook workbook) {
        if (StringUtils.isBlank(tenantCode) || workbook == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        List<TeaPutRequest> teaPutRequestList = excelHandlerFactory.getExcelHandler(tenantCode)
                .getTeaHandler()
                .upload(tenantCode, workbook);

        for (TeaPutRequest teaPutRequest : teaPutRequestList) {
            long start4InsertSingle = System.currentTimeMillis();
            TeaMachineResult<Void> result = this.put(teaPutRequest);
            if (!result.isSuccess()) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            long end4InsertSingle = System.currentTimeMillis();
            log.info("|teaMgtServiceImpl|insertSingle|succ|" + (end4InsertSingle - start4InsertSingle) + "|");
        }
        return TeaMachineResult.success();
    }
}
