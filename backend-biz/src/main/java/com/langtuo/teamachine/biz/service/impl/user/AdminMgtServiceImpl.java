package com.langtuo.teamachine.biz.service.impl.user;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.user.AdminDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.user.RoleDTO;
import com.langtuo.teamachine.api.request.user.AdminPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.user.AdminMgtService;
import com.langtuo.teamachine.api.service.user.RoleMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.po.user.AdminPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

@Component
@Slf4j
public class AdminMgtServiceImpl implements AdminMgtService {
    @Resource
    private AdminAccessor adminAccessor;

    @Resource
    private RoleMgtService roleMgtService;

    @Override
    public LangTuoResult<AdminDTO> get(String tenantCode, String loginName) {
        // 超级管理员特殊逻辑
        AdminDTO superAdminDTO = getSysSuperAdmin(tenantCode, loginName);
        if (superAdminDTO != null) {
            return LangTuoResult.success(superAdminDTO);
        }

        AdminPO adminPO = adminAccessor.selectOne(tenantCode, loginName);
        AdminDTO adminRoleDTO = convert(adminPO);
        return LangTuoResult.success(adminRoleDTO);
    }

    @Override
    public LangTuoResult<PageDTO<AdminDTO>> search(String tenantCode, String loginName, String roleName,
            int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        LangTuoResult<PageDTO<AdminDTO>> langTuoResult;
        try {
            String roleCode = null;
            if (StringUtils.isNotBlank(roleName)) {
                RoleDTO roleDTO = getModel(roleMgtService.getByName(tenantCode, roleName));
                if (roleDTO == null) {
                    return LangTuoResult.success(new PageDTO<>(null, 0, pageNum, pageSize));
                } else {
                    roleCode = roleDTO.getRoleCode();
                }
            }

            PageInfo<AdminPO> pageInfo = adminAccessor.search(tenantCode, loginName, roleCode,
                    pageNum, pageSize);
            List<AdminDTO> dtoList = pageInfo.getList().stream()
                    .map(adminPO -> convert(adminPO))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<AdminDTO>> list(String tenantCode) {
        LangTuoResult<List<AdminDTO>> langTuoResult;
        try {
            List<AdminPO> list = adminAccessor.selectList(tenantCode);
            List<AdminDTO> dtoList = list.stream()
                    .map(adminPO -> convert(adminPO))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(AdminPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        AdminPO adminPO = convert(request);

        LangTuoResult<Void> langTuoResult;
        try {
            AdminPO exist = adminAccessor.selectOne(request.getTenantCode(), request.getLoginName());
            if (exist != null) {
                int updated = adminAccessor.update(adminPO);
            } else {
                int inserted = adminAccessor.insert(adminPO);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String loginName) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(loginName)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult;
        try {
            int deleted = adminAccessor.delete(tenantCode, loginName);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Integer> countByRoleCode(String tenantCode, String roleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(roleCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Integer> langTuoResult;
        try {
            int cnt = adminAccessor.countByRoleCode(tenantCode, roleCode);
            langTuoResult = LangTuoResult.success(cnt);
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
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

        RoleDTO roleDTO = getModel(roleMgtService.getByCode(adminPO.getTenantCode(), adminPO.getRoleCode()));
        if (roleDTO != null) {
            dto.setRoleCode(roleDTO.getRoleCode());
            dto.setRoleName(roleDTO.getRoleName());
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

    private AdminDTO getSysSuperAdmin(String tenantCode, String loginName) {
        if (!"SYS_SUPER_ADMIN".equals(loginName)) {
            return null;
        }

        AdminDTO dto = new AdminDTO();
        dto.setTenantCode(tenantCode);
        dto.setLoginName("SYS_SUPER_ADMIN");
        // 经过md5加密后的密码，原始密码是SYS_SUPER_ADMIN
        dto.setLoginPass("5505b50f5f0ec77b27a0ea270b21e7f0");
        dto.setOrgName("总公司");
        dto.setRoleCode("SYS_SUPER_ROLE");
        dto.setRoleName("SYS_SUPER_ROLE");
        return dto;
    }
}
