package com.langtuo.teamachine.biz.service.user;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.user.RoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.RolePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.RoleMgtService;
import com.langtuo.teamachine.biz.convert.user.RoleMgtConvertor;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.user.PermitActAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleActRelAccessor;
import com.langtuo.teamachine.dao.po.user.AdminPO;
import com.langtuo.teamachine.dao.po.user.RoleActRelPO;
import com.langtuo.teamachine.dao.po.user.RolePO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.LocaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.biz.convert.user.RoleMgtConvertor.*;

@Component
@Slf4j
public class RoleMgtServiceImpl implements RoleMgtService {
    @Resource
    private RoleAccessor roleAccessor;

    @Resource
    private RoleActRelAccessor roleActRelAccessor;

    @Resource
    private PermitActAccessor permitActAccessor;

    @Resource
    private AdminAccessor adminAccessor;

    @Override
    public TeaMachineResult<RoleDTO> getByCode(String tenantCode, String roleCode) {
        RolePO rolePO = roleAccessor.getByRoleCode(tenantCode, roleCode);
        RoleDTO roleDTO = RoleMgtConvertor.convertToRoleDTO(rolePO);
        return TeaMachineResult.success(roleDTO);
    }

    @Override
    public TeaMachineResult<PageDTO<RoleDTO>> search(String tenantCode, String roleName, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<RolePO> pageInfo = roleAccessor.search(tenantCode, roleName, pageNum, pageSize);
            return TeaMachineResult.success(new PageDTO<>(convertToRoleDTO(pageInfo.getList()), pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("roleMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<List<RoleDTO>> list(String tenantCode) {
        TeaMachineResult<List<RoleDTO>> teaMachineResult;
        try {
            List<RolePO> list = roleAccessor.search(tenantCode);
            if (!CollectionUtils.isEmpty(list)) {
                list = list.stream().filter(rolePO -> {
                    if (CommonConsts.ROLE_CODE_SYS_SUPER.equals(rolePO.getRoleCode())) {
                        return false;
                    } else {
                        return true;
                    }
                }).collect(Collectors.toList());
            }

            List<RoleDTO> dtoList = convertToRoleDTO(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("roleMgtService|list|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(RolePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        RolePO rolePO = convertToRolePO(request);
        List<RoleActRelPO> roleActRelPOList = convertRoleActRel(request);
        if (request.isPutNew()) {
            return putNew(rolePO, roleActRelPOList);
        } else {
            return putUpdate(rolePO, roleActRelPOList);
        }
    }

    private TeaMachineResult<Void> putNew(RolePO po, List<RoleActRelPO> actRelPOList) {
        try {
            RolePO exist = roleAccessor.getByRoleCode(po.getTenantCode(), po.getRoleCode());
            if (exist != null) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = roleAccessor.insert(po);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("roleMgtService|putNewRole|error|" + inserted);
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted4RoleActRel = roleActRelAccessor.deleteByRoleCode(po.getTenantCode(), po.getRoleCode());
            for (RoleActRelPO actRelPO : actRelPOList) {
                int inserted4actRel = roleActRelAccessor.insert(actRelPO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                    log.error("roleMgtService|putNewActRel|error|" + inserted4actRel);
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("roleMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(RolePO po, List<RoleActRelPO> actRelPOList) {
        try {
            RolePO exist = roleAccessor.getByRoleCode(po.getTenantCode(), po.getRoleCode());
            if (exist == null) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            if (CommonConsts.ROLE_CODE_TENANT_SUPER.equals(po.getRoleCode())) {
                AdminPO adminPO = DaoUtils.getAdminPOByLoginSession(po.getTenantCode());
                if (!CommonConsts.ROLE_CODE_SYS_SUPER.equals(adminPO.getRoleCode())) {
                    return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(
                            ErrorCodeEnum.BIZ_ERR_CANNOT_MODIFY_TENANT_SUPER_ADMIN_ROLE));
                }
            }

            int updated = roleAccessor.update(po);
            if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
                log.error("roleMgtService|putUpdateRole|error|" + updated);
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted4RoleActRel = roleActRelAccessor.deleteByRoleCode(po.getTenantCode(), po.getRoleCode());
            for (RoleActRelPO actRelPO : actRelPOList) {
                int inserted4actRel = roleActRelAccessor.insert(actRelPO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4actRel) {
                    log.error("roleMgtService|putUpdateActRel|error|" + inserted4actRel);
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("roleMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String roleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(roleCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        int adminCount = adminAccessor.countByRoleCode(tenantCode, roleCode);
        if (adminCount > 0) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(
                    ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
        }

        if (CommonConsts.ROLE_CODE_TENANT_SUPER.equals(roleCode)) {
            if (CommonConsts.ROLE_CODE_TENANT_SUPER.equals(roleCode)) {
                AdminPO adminPO = DaoUtils.getAdminPOByLoginSession(tenantCode);
                if (!CommonConsts.ROLE_CODE_SYS_SUPER.equals(adminPO.getRoleCode())) {
                    return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(
                            ErrorCodeEnum.BIZ_ERR_CANNOT_MODIFY_TENANT_SUPER_ADMIN_ROLE));
                }
            }
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = roleAccessor.deleteByRoleCode(tenantCode, roleCode);
            int deleted4Rel = roleActRelAccessor.deleteByRoleCode(tenantCode, roleCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("roleMgtService|delete|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }


}
