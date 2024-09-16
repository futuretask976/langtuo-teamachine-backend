package com.langtuo.teamachine.biz.service.user;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.user.RoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.RolePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.user.RoleMgtService;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.user.PermitActAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleActRelAccessor;
import com.langtuo.teamachine.dao.po.user.RoleActRelPO;
import com.langtuo.teamachine.dao.po.user.RolePO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RoleMgtServiceImpl implements RoleMgtService {
    /**
     * 租户超级管理员角色的roleCode硬编码
     */
    private static final String TENANT_SUPER_ADMIN_ROLE_CODE = "role_tenant_super_admin";

    @Resource
    private RoleAccessor roleAccessor;

    @Resource
    private RoleActRelAccessor roleActRelAccessor;

    @Resource
    private PermitActAccessor permitActAccessor;

    @Resource
    private AdminAccessor adminAccessor;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<RoleDTO> getByCode(String tenantCode, String roleCode) {
        RolePO rolePO = roleAccessor.selectOneByRoleCode(tenantCode, roleCode);
        RoleDTO roleDTO = convert(rolePO);
        return TeaMachineResult.success(roleDTO);
    }

    @Override
    public TeaMachineResult<RoleDTO> getByName(String tenantCode, String roleName) {
        RolePO rolePO = roleAccessor.selectOneByRoleName(tenantCode, roleName);
        RoleDTO roleDTO = convert(rolePO);
        return TeaMachineResult.success(roleDTO);
    }

    @Override
    public TeaMachineResult<PageDTO<RoleDTO>> search(String tenantCode, String roleName, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<RoleDTO>> teaMachineResult;
        try {
            PageInfo<RolePO> pageInfo = roleAccessor.search(tenantCode, roleName, pageNum, pageSize);
            List<RoleDTO> dtoList = convert(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("roleMgtService|search|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<RoleDTO>> page(String tenantCode, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        TeaMachineResult<PageDTO<RoleDTO>> teaMachineResult;
        try {
            PageInfo<RolePO> pageInfo = roleAccessor.selectList(tenantCode, pageNum, pageSize);
            List<RoleDTO> dtoList = convert(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("roleMgtService|page|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<RoleDTO>> list(String tenantCode) {
        TeaMachineResult<List<RoleDTO>> teaMachineResult;
        try {
            List<RolePO> list = roleAccessor.selectList(tenantCode);

            String adminLoginName = getAdminName();
            if (!"SYS_SUPER_ADMIN".equals(adminLoginName)) {
                list = list.stream().filter(rolePO -> {
                    if ("SYS_SUPER_ROLE".equals(rolePO.getRoleCode())) {
                        return false;
                    } else {
                        return true;
                    }
                }).collect(Collectors.toList());
            }

            List<RoleDTO> dtoList = convert(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(RolePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        RolePO rolePO = convert(request);
        List<RoleActRelPO> roleActRelPOList = convertRoleActRel(request);
        if (request.isNewPut()) {
            return putNew(rolePO, roleActRelPOList);
        } else {
            return putUpdate(rolePO, roleActRelPOList);
        }
    }

    private TeaMachineResult<Void> putNew(RolePO po, List<RoleActRelPO> actRelPOList) {
        try {
            RolePO exist = roleAccessor.selectOneByRoleCode(po.getTenantCode(), po.getRoleCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = roleAccessor.insert(po);
            if (CommonConsts.NUM_ONE != inserted) {
                log.error("roleMgtService|putNewRole|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted4RoleActRel = roleActRelAccessor.deleteByRoleCode(po.getTenantCode(), po.getRoleCode());
            for (RoleActRelPO actRelPO : actRelPOList) {
                int inserted4actRel = roleActRelAccessor.insert(actRelPO);
                if (CommonConsts.NUM_ONE != inserted) {
                    log.error("roleMgtService|putNewActRel|error|" + inserted4actRel);
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("roleMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(RolePO po, List<RoleActRelPO> actRelPOList) {
        try {
            RolePO exist = roleAccessor.selectOneByRoleCode(po.getTenantCode(), po.getRoleCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            if (TENANT_SUPER_ADMIN_ROLE_CODE.equals(po.getRoleCode())) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_CANNOT_MODIFY_TENANT_SUPER_ADMIN_ROLE));
            }

            int inserted = roleAccessor.insert(po);
            if (CommonConsts.NUM_ONE != inserted) {
                log.error("roleMgtService|putUpdateRole|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted4RoleActRel = roleActRelAccessor.deleteByRoleCode(po.getTenantCode(), po.getRoleCode());
            for (RoleActRelPO actRelPO : actRelPOList) {
                int inserted4actRel = roleActRelAccessor.insert(actRelPO);
                if (CommonConsts.NUM_ONE != inserted) {
                    log.error("roleMgtService|putUpdateActRel|error|" + inserted4actRel);
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("roleMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String roleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(roleCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        int adminCount = adminAccessor.countByRoleCode(tenantCode, roleCode);
        if (adminCount > 0) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                    ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
        }

        if (TENANT_SUPER_ADMIN_ROLE_CODE.equals(roleCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_CANNOT_MODIFY_TENANT_SUPER_ADMIN_ROLE));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = roleAccessor.deleteByRoleCode(tenantCode, roleCode);
            int deleted4Rel = roleActRelAccessor.deleteByRoleCode(tenantCode, roleCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("roleMgtService|delete|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    private List<RoleDTO> convert(List<RolePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<RoleDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private RoleDTO convert(RolePO po) {
        if (po == null) {
            return null;
        }

        RoleDTO dto = new RoleDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setRoleCode(po.getRoleCode());
        dto.setRoleName(po.getRoleName());
        dto.setSysReserved(po.getSysReserved());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());

        List<RoleActRelPO> roleActRelPOList = roleActRelAccessor.selectListByRoleCode(
                po.getTenantCode(), po.getRoleCode());
        if (!CollectionUtils.isEmpty(roleActRelPOList)) {
            dto.setPermitActCodeList(roleActRelPOList.stream()
                    .map(item -> item.getPermitActCode())
                    .collect(Collectors.toList()));
        }

        int adminCount = adminAccessor.countByRoleCode(po.getTenantCode(), po.getRoleCode());
        dto.setAdminCount(adminCount);

        return dto;
    }

    private RolePO convert(RolePutRequest request) {
        if (request == null) {
            return null;
        }

        RolePO po = new RolePO();
        po.setRoleCode(request.getRoleCode());
        po.setRoleName(request.getRoleName());
        po.setSysReserved(request.getSysReserved());
        po.setComment(request.getComment());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }

    private List<RoleActRelPO> convertRoleActRel(RolePutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getPermitActCodeList())) {
            return null;
        }

        return request.getPermitActCodeList().stream().map(item -> {
            RoleActRelPO po = new RoleActRelPO();
            po.setRoleCode(request.getRoleCode());
            po.setTenantCode(request.getTenantCode());
            po.setPermitActCode(item);
            return po;
        }).collect(Collectors.toList());
    }

    private String getAdminName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalArgumentException("couldn't find login session");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String adminLoginName = userDetails.getUsername();
        if (StringUtils.isBlank(adminLoginName)) {
            throw new IllegalArgumentException("couldn't find login session");
        }

        return adminLoginName;
    }
}
