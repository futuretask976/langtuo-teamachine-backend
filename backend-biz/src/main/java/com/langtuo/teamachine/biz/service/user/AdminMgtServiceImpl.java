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
import com.langtuo.teamachine.dao.po.user.RolePO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AdminMgtServiceImpl implements AdminMgtService {
    @Resource
    private AdminAccessor adminAccessor;

    @Resource
    private RoleAccessor roleAccessor;

    @Override
    public TeaMachineResult<AdminDTO> get(String tenantCode, String loginName) {
        AdminPO adminPO = adminAccessor.getByLoginName(tenantCode, loginName);
        AdminDTO adminRoleDTO = convert(adminPO);
        return TeaMachineResult.success(adminRoleDTO);
    }

    @Override
    public TeaMachineResult<PageDTO<AdminDTO>> search(String tenantCode, String loginName, String roleName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<AdminDTO>> teaMachineResult;
        try {
            String roleCode = null;
            if (StringUtils.isNotBlank(roleName)) {
                RolePO rolePO = roleAccessor.getByRoleName(tenantCode, roleName);
                if (rolePO == null) {
                    return TeaMachineResult.success(new PageDTO<>(null, 0, pageNum, pageSize));
                } else {
                    roleCode = rolePO.getRoleCode();
                }
            }

            PageInfo<AdminPO> pageInfo = adminAccessor.search(tenantCode, loginName, roleCode,
                    pageNum, pageSize);
            List<AdminDTO> dtoList = pageInfo.getList().stream()
                    .map(adminPO -> convert(adminPO))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("adminMgtService|search|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<AdminDTO>> list(String tenantCode) {
        TeaMachineResult<List<AdminDTO>> teaMachineResult;
        try {
            List<AdminPO> list = adminAccessor.list(tenantCode);
            List<AdminDTO> dtoList = list.stream()
                    .map(adminPO -> convert(adminPO))
                    .collect(Collectors.toList());

            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("adminMgtService|list|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(AdminPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        AdminPO po = convert(request);
        if (request.isNewPut()) {
            return putNew(po);
        } else {
            return putUpdate(po);
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String loginName) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(loginName)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = adminAccessor.deleteByLoginName(tenantCode, loginName);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("adminMgtService|delete|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Integer> countByRoleCode(String tenantCode, String roleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(roleCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Integer> teaMachineResult;
        try {
            int cnt = adminAccessor.countByRoleCode(tenantCode, roleCode);
            teaMachineResult = TeaMachineResult.success(cnt);
        } catch (Exception e) {
            log.error("adminMgtService|countByRoleCode|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    private TeaMachineResult<Void> putNew(AdminPO po) {
        TeaMachineResult<Void> teaMachineResult;
        try {
            AdminPO exist = adminAccessor.getByLoginName(po.getTenantCode(), po.getLoginName());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = adminAccessor.insert(po);
            if (inserted != CommonConsts.NUM_ONE) {
                log.error("adminMgtService|putNew|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("adminMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(AdminPO po) {
        try {
            AdminPO exist = adminAccessor.getByLoginName(po.getTenantCode(), po.getLoginName());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            if (CommonConsts.ROLE_CODE_TENANT_SUPER.equals(exist.getRoleCode())) {
                AdminPO loginAdminPO = DaoUtils.getLoginAdminPO(po.getTenantCode());
                if (!CommonConsts.ROLE_CODE_SYS_SUPER.equals(loginAdminPO.getRoleCode())) {
                    return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                            ErrorCodeEnum.BIZ_ERR_CANNOT_MODIFY_TENANT_SUPER_ADMIN));
                }
            }

            int updated = adminAccessor.update(po);
            if (updated != CommonConsts.NUM_ONE) {
                log.error("adminMgtService|putUpdate|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("adminMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    private AdminDTO convert(AdminPO adminPO) {
        if (adminPO == null) {
            return null;
        }

        AdminDTO dto = new AdminDTO();
        dto.setGmtCreated(adminPO.getGmtCreated());
        dto.setGmtModified(adminPO.getGmtModified());
        dto.setTenantCode(adminPO.getTenantCode());
        dto.setComment(adminPO.getComment());
        dto.setExtraInfo(adminPO.getExtraInfo());
        dto.setLoginName(adminPO.getLoginName());
        dto.setLoginPass(adminPO.getLoginPass());
        dto.setOrgName(adminPO.getOrgName());

        RolePO rolePO = roleAccessor.getByRoleCode(adminPO.getTenantCode(), adminPO.getRoleCode());
        if (rolePO != null) {
            dto.setRoleCode(rolePO.getRoleCode());
            dto.setRoleName(rolePO.getRoleName());
        }
        return dto;
    }

    private AdminPO convert(AdminPutRequest request) {
        if (request == null) {
            return null;
        }

        AdminPO po = new AdminPO();
        po.setLoginName(request.getLoginName());
        po.setLoginPass(request.getLoginPass());
        po.setRoleCode(request.getRoleCode());
        po.setOrgName(request.getOrgName());
        po.setComment(request.getComment());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }
}
