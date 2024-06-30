package com.langtuo.teamachine.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.AdminDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.AdminPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.AdminMgtService;
import com.langtuo.teamachine.dao.accessor.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.AdminRoleAccessor;
import com.langtuo.teamachine.dao.po.AdminPO;
import com.langtuo.teamachine.dao.po.AdminRolePO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminMgtServiceImpl implements AdminMgtService {
    @Resource
    private AdminAccessor adminAccessor;

    @Resource
    private AdminRoleAccessor adminRoleAccessor;

    @Override
    public LangTuoResult<AdminDTO> get(String tenantCode, String loginName) {
        AdminPO adminPO = adminAccessor.selectOne(tenantCode, loginName);
        AdminRolePO adminRolePO = adminRoleAccessor.selectOne(tenantCode, adminPO.getRoleCode());

        AdminDTO adminRoleDTO = convert(adminPO, adminRolePO);
        return LangTuoResult.success(adminRoleDTO);
    }

    @Override
    public LangTuoResult<PageDTO<AdminDTO>> search(String tenantCode, String loginName, String roleName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<AdminDTO>> langTuoResult = null;
        try {
            AdminRolePO adminRolePO = adminRoleAccessor.selectOne(tenantCode, roleName);
            if (adminRolePO == null) {
                return LangTuoResult.success(new PageDTO<>());
            }

            PageInfo<AdminPO> pageInfo = adminAccessor.search(tenantCode, loginName, adminRolePO.getRoleCode(),
                    pageNum, pageSize);
            List<AdminDTO> dtoList = pageInfo.getList().stream()
                    .map(adminPO -> {
                        AdminRolePO rolePO = adminRoleAccessor.selectOne(adminPO.getTenantCode(), adminPO.getRoleCode());
                        return convert(adminPO, rolePO);
                    })
                    .collect(Collectors.toList());
            System.out.printf("$$$$$ AdminMgtServiceImpl#search dtoList=%s\n", dtoList.size());

            PageDTO<AdminDTO> pageDTO = new PageDTO<>();
            pageDTO.setList(dtoList);
            pageDTO.setPageNum(pageNum);
            pageDTO.setPageSize(pageSize);
            pageDTO.setTotal(pageInfo.getTotal());

            langTuoResult = LangTuoResult.success(pageDTO);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<AdminDTO>> list(String tenantCode, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<AdminDTO>> langTuoResult = null;
        try {
            PageInfo<AdminPO> pageInfo = adminAccessor.selectList(tenantCode, pageNum, pageSize);
            List<AdminDTO> dtoList = pageInfo.getList().stream()
                    .map(adminPO -> {
                        AdminRolePO adminRolePO = adminRoleAccessor.selectOne(adminPO.getTenantCode(), adminPO.getRoleCode());
                        return convert(adminPO, adminRolePO);
                    })
                    .collect(Collectors.toList());

            PageDTO<AdminDTO> pageDTO = new PageDTO<>();
            pageDTO.setList(dtoList);
            pageDTO.setPageNum(pageNum);
            pageDTO.setPageSize(pageSize);
            pageDTO.setTotal(pageInfo.getTotal());
            langTuoResult = LangTuoResult.success(pageDTO);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(AdminPutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        AdminPO adminPO = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            AdminPO exist = adminAccessor.selectOne(request.getTenantCode(), request.getLoginName());
            System.out.printf("$$$$$ AdminMgtServiceImpl#put exist=%s\n", exist);
            if (exist != null) {
                int updated = adminAccessor.update(adminPO);
                System.out.printf("$$$$$ AdminMgtServiceImpl#put updated=%s\n", updated);
            } else {
                int inserted = adminAccessor.insert(adminPO);
                System.out.printf("$$$$$ AdminMgtServiceImpl#put inserted=%s\n", inserted);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
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
            System.out.printf("$$$$$ AdminMgtServiceImpl#put deleted=%s\n", deleted);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private AdminDTO convert(AdminPO adminPO, AdminRolePO adminRolePO) {
        if (adminPO == null || adminRolePO == null) {
            return null;
        }

        AdminDTO dto = new AdminDTO();
        dto.setId(adminPO.getId());
        dto.setGmtCreated(adminPO.getGmtCreated());
        dto.setGmtModified(adminPO.getGmtModified());
        dto.setLoginName(adminPO.getLoginName());
        dto.setLoginPass(adminPO.getLoginPass());
        dto.setOrgName(adminPO.getOrgName());
        dto.setComment(adminPO.getComment());
        dto.setTenantCode(adminPO.getTenantCode());
        dto.setExtraInfo(adminPO.getExtraInfo());

        dto.setRoleCode(adminRolePO.getRoleCode());
        dto.setRoleName(adminRolePO.getRoleName());

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
