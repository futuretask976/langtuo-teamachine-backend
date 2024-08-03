package com.langtuo.teamachine.biz.service.impl.user;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.user.AdminDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.AdminPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.user.AdminMgtService;
import com.langtuo.teamachine.api.service.user.RoleMgtService;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleAccessor;
import com.langtuo.teamachine.dao.po.user.AdminPO;
import com.langtuo.teamachine.dao.po.user.RolePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AdminMgtServiceImpl implements AdminMgtService {
    @Resource
    private RoleMgtService roleMgtService;

    @Resource
    private AdminAccessor adminAccessor;

    @Resource
    private RoleAccessor roleAccessor;

    @Override
    public LangTuoResult<AdminDTO> get(String tenantCode, String loginName) {
        // 超级管理员特殊逻辑
        AdminDTO superAdmin = getSysSuperAdmin(tenantCode, loginName);
        if (superAdmin != null) {
            return LangTuoResult.success(superAdmin);
        }

        AdminPO adminPO = adminAccessor.selectOne(tenantCode, loginName);
        AdminDTO adminRoleDTO = convert(adminPO);
        return LangTuoResult.success(adminRoleDTO);
    }

    @Override
    public LangTuoResult<PageDTO<AdminDTO>> search(String tenantCode, String loginName, String roleName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<AdminDTO>> langTuoResult = null;
        try {
            String roleCode = null;
            if (StringUtils.isNotBlank(roleName)) {
                Optional<RolePO> opt = roleAccessor.selectList(tenantCode).stream()
                        .filter(item -> item.getRoleName().equals(roleName))
                        .findFirst();
                if (opt.isPresent()) {
                    roleCode = opt.get().getRoleCode();
                } else {
                    return LangTuoResult.success(new PageDTO<>(null, 0, pageNum, pageSize));
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
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<AdminDTO>> list(String tenantCode) {
        LangTuoResult<List<AdminDTO>> langTuoResult = null;
        try {
            List<AdminPO> list = adminAccessor.selectList(tenantCode);
            List<AdminDTO> dtoList = list.stream()
                    .map(adminPO -> convert(adminPO))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(AdminPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        AdminPO adminPO = convert(request);

        LangTuoResult<Void> langTuoResult = null;
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
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String loginName) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(loginName)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = adminAccessor.delete(tenantCode, loginName);
            langTuoResult = LangTuoResult.success();
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
        dto.setId(adminPO.getId());
        dto.setGmtCreated(adminPO.getGmtCreated());
        dto.setGmtModified(adminPO.getGmtModified());
        dto.setTenantCode(adminPO.getTenantCode());
        dto.setComment(adminPO.getComment());
        dto.setExtraInfo(adminPO.getExtraInfo());
        dto.setLoginName(adminPO.getLoginName());
        dto.setLoginPass(adminPO.getLoginPass());
        dto.setOrgName(adminPO.getOrgName());

        RolePO rolePO = roleAccessor.selectOne(adminPO.getTenantCode(), adminPO.getRoleCode());
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

    /**
     * 此方法返回系统超级管理员，不存储在数据库（因为如果存储数据库，必须指定tenant，而系统超级管理员不归属任何teanant）
     * @return
     */
    public AdminDTO getSysSuperAdmin(String tenantCode, String loginName) {
        if (!"SYS_SUPER_ADMIN".equals(loginName)) {
            return null;
        }

        AdminDTO dto = new AdminDTO();
        dto.setTenantCode(tenantCode);
        dto.setLoginName("SYS_SUPER_ADMIN");
        dto.setLoginPass("SYS_SUPER_ADMIN");
        dto.setOrgName("总公司");
        dto.setRoleCode("SYS_SUPER_ROLE");
        dto.setRoleName("SYS_SUPER_ROLE");
        return dto;
    }
}
