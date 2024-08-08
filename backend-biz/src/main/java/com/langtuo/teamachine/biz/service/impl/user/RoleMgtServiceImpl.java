package com.langtuo.teamachine.biz.service.impl.user;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.user.RoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.RolePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.user.RoleMgtService;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleAccessor;
import com.langtuo.teamachine.dao.accessor.user.RoleActRelAccessor;
import com.langtuo.teamachine.dao.accessor.user.PermitActAccessor;
import com.langtuo.teamachine.dao.po.user.PermitActGroupPO;
import com.langtuo.teamachine.dao.po.user.PermitActPO;
import com.langtuo.teamachine.dao.po.user.RoleActRelPO;
import com.langtuo.teamachine.dao.po.user.RolePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    public LangTuoResult<RoleDTO> get(String tenantCode, String roleCode) {
        // 超级管理角色特殊逻辑
        RoleDTO superRole = getSysSuperRole(roleCode);
        if (superRole != null) {
            return LangTuoResult.success(superRole);
        }

        RolePO rolePO = roleAccessor.selectOne(tenantCode, roleCode);
        RoleDTO roleDTO = convert(rolePO);
        return LangTuoResult.success(roleDTO);
    }

    @Override
    public LangTuoResult<PageDTO<RoleDTO>> search(String tenantCode, String roleName, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<RoleDTO>> langTuoResult = null;
        try {
            PageInfo<RolePO> pageInfo = roleAccessor.search(tenantCode, roleName, pageNum, pageSize);
            List<RoleDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<RoleDTO>> page(String tenantCode, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<RoleDTO>> langTuoResult = null;
        try {
            PageInfo<RolePO> pageInfo = roleAccessor.selectList(tenantCode, pageNum, pageSize);
            List<RoleDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("page error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<RoleDTO>> list(String tenantCode) {
        LangTuoResult<List<RoleDTO>> langTuoResult = null;
        try {
            List<RolePO> list = roleAccessor.selectList(tenantCode);
            List<RoleDTO> dtoList = convert(list);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(RolePutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        RolePO rolePO = convert(request);
        List<RoleActRelPO> roleActRelPOList = convertRoleActRel(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            RolePO exist = roleAccessor.selectOne(request.getTenantCode(), request.getRoleCode());
            if (exist != null) {
                int updated = roleAccessor.update(rolePO);
            } else {
                int inserted = roleAccessor.insert(rolePO);
            }

            int deleted4RoleActRel = roleActRelAccessor.delete(request.getTenantCode(), request.getRoleCode());
            roleActRelPOList.stream().forEach(item -> {
                int inserted4RoleActRel = roleActRelAccessor.insert(item);
            });
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String roleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(roleCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        int adminCount = adminAccessor.countByRoleCode(tenantCode, roleCode);
        if (adminCount > 0) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_TRY_DELETE_USING_ROLE);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = roleAccessor.delete(tenantCode, roleCode);
            int deleted4Rel = roleActRelAccessor.delete(tenantCode, roleCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
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

        List<RoleActRelPO> roleActRelPOList = roleActRelAccessor.selectList(
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

    /**
     * 此方法返回系统超级角色，不存储在数据库（因为如果存储数据库，必须指定tenant，而系统超级角色不归属任何teanant）
     * @return
     */
    public RoleDTO getSysSuperRole(String roleCode) {
        if (!"SYS_SUPER_ROLE".equals(roleCode)) {
            return null;
        }

        RoleDTO dto = new RoleDTO();
        dto.setRoleCode("SYS_SUPER_ROLE");
        dto.setRoleName("SYS_SUPER_ROLE");
        dto.setSysReserved(1);

        List<String> permitActCodeList = Lists.newArrayList();
        List<PermitActGroupPO> permitActGroupList = permitActAccessor.selectPermitActGroupList();
        for (PermitActGroupPO po : permitActGroupList) {
            List<PermitActPO> permitActList = permitActAccessor.selectPermitActList(po.getPermitActGroupCode());
            for (PermitActPO permitAct : permitActList) {
                permitActCodeList.add(permitAct.getPermitActCode());
            }
        }
        // 这个权限点通过代码增加无法在页面授予
        permitActCodeList.add("tenant_mgt");
        dto.setPermitActCodeList(permitActCodeList);

        return dto;
    }
}
