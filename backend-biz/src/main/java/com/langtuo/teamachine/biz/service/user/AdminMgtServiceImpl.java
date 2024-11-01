package com.langtuo.teamachine.biz.service.user;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.user.AdminDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.AdminPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.AdminMgtService;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleAccessor;
import com.langtuo.teamachine.dao.po.user.AdminPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.LocaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.langtuo.teamachine.biz.convert.user.AdminMgtConvertor.*;

@Component
@Slf4j
public class AdminMgtServiceImpl implements AdminMgtService {
    @Resource
    private AdminAccessor adminAccessor;

    @Resource
    private RoleAccessor roleAccessor;

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<AdminDTO> getByLoginName(String tenantCode, String loginName) {
        AdminPO adminPO = adminAccessor.getByLoginName(tenantCode, loginName);
        return TeaMachineResult.success(convertToAdminDTO(adminPO));
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<PageDTO<AdminDTO>> search(String tenantCode, String loginName, String roleCode,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<AdminDTO>> teaMachineResult;
        try {
            PageInfo<AdminPO> pageInfo = adminAccessor.search(tenantCode, loginName, roleCode,
                    pageNum, pageSize);
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(convertToAdminDTO(pageInfo.getList()),
                    pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("|adminMgtService|search|fatal|" + e.getMessage() + "|", e);
            teaMachineResult = TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<List<AdminDTO>> list(String tenantCode) {
        try {
            List<AdminPO> list = adminAccessor.list(tenantCode);
            return TeaMachineResult.success(convertToAdminDTO(list));
        } catch (Exception e) {
            log.error("|adminMgtService|list|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(AdminPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        AdminPO po = convertToAdminPO(request);
        try {
            if (request.isPutNew()) {
                return doPutNew(po);
            } else {
                return doPutUpdate(po);
            }
        } catch (Exception e) {
            log.error("|adminMgtService|put|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public TeaMachineResult<Void> deleteByLoginName(String tenantCode, String loginName) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(loginName)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = adminAccessor.deleteByLoginName(tenantCode, loginName);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("|adminMgtService|delete|fatal|" + e.getMessage() + "|", e);
            teaMachineResult = TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<Integer> countByRoleCode(String tenantCode, String roleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(roleCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Integer> teaMachineResult;
        try {
            int cnt = adminAccessor.countByRoleCode(tenantCode, roleCode);
            teaMachineResult = TeaMachineResult.success(cnt);
        } catch (Exception e) {
            log.error("|adminMgtService|countByRoleCode|fatal|" + e.getMessage() + "|", e);
            teaMachineResult = TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutNew(AdminPO po) {
        AdminPO exist = adminAccessor.getByLoginName(po.getTenantCode(), po.getLoginName());
        if (exist != null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
        }

        int inserted = adminAccessor.insert(po);
        if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
            log.error("|adminMgtService|putNew|error|" + inserted + "|");
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPutUpdate(AdminPO po) {
        AdminPO exist = adminAccessor.getByLoginName(po.getTenantCode(), po.getLoginName());
        if (exist == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
        }

        if (CommonConsts.ROLE_CODE_TENANT_SUPER.equals(exist.getRoleCode())) {
            AdminPO loginAdminPO = DaoUtils.getAdminPOByLoginSession(po.getTenantCode());
            if (!CommonConsts.ROLE_CODE_SYS_SUPER.equals(loginAdminPO.getRoleCode())) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_MODIFY_TENANT_SUPER_ADMIN));
            }
        }

        int updated = adminAccessor.update(po);
        if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
            log.error("|adminMgtService|putUpdate|error|" + updated + "|");
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
        return TeaMachineResult.success();
    }
}
