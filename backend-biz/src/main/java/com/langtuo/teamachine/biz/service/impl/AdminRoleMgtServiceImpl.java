package com.langtuo.teamachine.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.AdminRoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.AdminRolePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.AdminRoleMgtService;
import com.langtuo.teamachine.dao.accessor.AdminRoleAccessor;
import com.langtuo.teamachine.dao.accessor.AdminRoleActRelAccessor;
import com.langtuo.teamachine.dao.po.AdminRoleActRelPO;
import com.langtuo.teamachine.dao.po.AdminRolePO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminRoleMgtServiceImpl implements AdminRoleMgtService {
    @Resource
    private AdminRoleAccessor adminRoleAccessor;

    @Resource
    private AdminRoleActRelAccessor adminRoleActRelAccessor;

    @Override
    public LangTuoResult<AdminRoleDTO> get(String tenantCode, String roleCode) {
        AdminRolePO adminRolePO = adminRoleAccessor.selectOne(tenantCode, roleCode);
        List<AdminRoleActRelPO> adminRoleActRelPOList = adminRoleActRelAccessor.selectList(tenantCode, roleCode);

        AdminRoleDTO adminRoleDTO = convert(adminRolePO);
        if (!CollectionUtils.isEmpty(adminRoleActRelPOList)) {
            adminRoleDTO.setPermitActCodeList(adminRoleActRelPOList.stream().map(item -> {
                return item.getPermitActCode();
            }).collect(Collectors.toList()));
        }

        return LangTuoResult.success(adminRoleDTO);
    }

    @Override
    public LangTuoResult<PageDTO<AdminRoleDTO>> search(String tenantCode, String roleName, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<AdminRoleDTO>> langTuoResult = null;
        try {
            PageInfo<AdminRolePO> pageInfo = adminRoleAccessor.search(tenantCode, roleName, pageNum, pageSize);
            List<AdminRoleDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());
            System.out.printf("$$$$$ AdminRoleMgtServiceImpl#search dtoList=%s\n", dtoList.size());

            dtoList.stream().forEach(item -> {
                List<AdminRoleActRelPO> adminRoleActRelPOList = adminRoleActRelAccessor.selectList(tenantCode,
                        item.getRoleCode());
                if (!CollectionUtils.isEmpty(adminRoleActRelPOList)) {
                    item.setPermitActCodeList(adminRoleActRelPOList.stream().map(ele -> {
                        return ele.getPermitActCode();
                    }).collect(Collectors.toList()));
                }
            });
            System.out.printf("$$$$$ AdminRoleMgtServiceImpl#search begin to convert\n");

            PageDTO<AdminRoleDTO> pageDTO = new PageDTO<>();
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
    public LangTuoResult<PageDTO<AdminRoleDTO>> list(String tenantCode, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<AdminRoleDTO>> langTuoResult = null;
        try {
            PageInfo<AdminRolePO> pageInfo = adminRoleAccessor.selectList(tenantCode, pageNum, pageSize);
            List<AdminRoleDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            dtoList.stream().forEach(item -> {
                List<AdminRoleActRelPO> adminRoleActRelPOList = adminRoleActRelAccessor.selectList(tenantCode,
                        item.getRoleCode());
                if (!CollectionUtils.isEmpty(adminRoleActRelPOList)) {
                    item.setPermitActCodeList(adminRoleActRelPOList.stream().map(ele -> {
                        return ele.getPermitActCode();
                    }).collect(Collectors.toList()));
                }
            });

            PageDTO<AdminRoleDTO> pageDTO = new PageDTO<>();
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
    public LangTuoResult<Void> put(AdminRolePutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        AdminRolePO adminRolePO = convert(request);
        List<AdminRoleActRelPO> adminRoleActRelPOList = convertRoleActRel(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            AdminRolePO exist = adminRoleAccessor.selectOne(request.getTenantCode(), request.getRoleCode());
            System.out.printf("$$$$$ AdminRoleMgtServiceImpl#put exist=%s\n", exist);
            if (exist != null) {
                int updated = adminRoleAccessor.update(adminRolePO);
                System.out.printf("$$$$$ AdminRoleMgtServiceImpl#put updated=%s\n", updated);
            } else {
                int inserted = adminRoleAccessor.insert(adminRolePO);
                System.out.printf("$$$$$ AdminRoleMgtServiceImpl#put inserted=%s\n", inserted);
            }

            int deleted4RoleActRel = adminRoleActRelAccessor.delete(request.getTenantCode(), request.getRoleCode());
            System.out.printf("$$$$$ AdminRoleMgtServiceImpl#put deleted4RoleActRel=%s\n", deleted4RoleActRel);
            adminRoleActRelPOList.stream().forEach(item -> {
                int inserted4RoleActRel = adminRoleActRelAccessor.insert(item);
                System.out.printf("$$$$$ AdminRoleMgtServiceImpl#put inserted4RoleActRel=%s\n", inserted4RoleActRel);
            });
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String roleCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(roleCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = adminRoleAccessor.delete(tenantCode, roleCode);
            System.out.printf("$$$$$ AdminRoleMgtServiceImpl#put deleted=%s\n", deleted);
            int deleted4Rel = adminRoleActRelAccessor.delete(tenantCode, roleCode);
            System.out.printf("$$$$$ MachineModelMgtServiceImpl#put deleted4Rel=%s\n", deleted4Rel);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private AdminRoleDTO convert(AdminRolePO po) {
        if (po == null) {
            return null;
        }

        AdminRoleDTO dto = new AdminRoleDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setRoleCode(po.getRoleCode());
        dto.setRoleName(po.getRoleName());
        dto.setComment(po.getComment());
        dto.setTenantCode(po.getTenantCode());
        dto.setExtraInfo(po.getExtraInfo());

        return dto;
    }

    private AdminRolePO convert(AdminRolePutRequest request) {
        if (request == null) {
            return null;
        }

        AdminRolePO po = new AdminRolePO();
        po.setRoleCode(request.getRoleCode());
        po.setRoleName(request.getRoleName());
        po.setComment(request.getComment());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());

        return po;
    }

    private List<AdminRoleActRelPO> convertRoleActRel(AdminRolePutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getPermitActCodeList())) {
            return null;
        }

        return request.getPermitActCodeList().stream().map(item -> {
            AdminRoleActRelPO po = new AdminRoleActRelPO();
            po.setRoleCode(request.getRoleCode());
            po.setTenantCode(request.getTenantCode());
            po.setPermitActCode(item);
            return po;
        }).collect(Collectors.toList());
    }
}
