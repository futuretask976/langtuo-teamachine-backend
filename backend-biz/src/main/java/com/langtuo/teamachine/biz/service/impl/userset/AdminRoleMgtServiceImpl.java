package com.langtuo.teamachine.biz.service.impl.userset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.userset.RoleDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.userset.RolePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.userset.AdminRoleMgtService;
import com.langtuo.teamachine.dao.accessor.userset.AdminRoleAccessor;
import com.langtuo.teamachine.dao.accessor.userset.AdminRoleActRelAccessor;
import com.langtuo.teamachine.dao.po.userset.AdminRoleActRelPO;
import com.langtuo.teamachine.dao.po.userset.RolePO;
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
    public LangTuoResult<RoleDTO> get(String tenantCode, String roleCode) {
        RolePO rolePO = adminRoleAccessor.selectOne(tenantCode, roleCode);
        List<AdminRoleActRelPO> adminRoleActRelPOList = adminRoleActRelAccessor.selectList(tenantCode, roleCode);

        RoleDTO roleDTO = convert(rolePO);
        if (!CollectionUtils.isEmpty(adminRoleActRelPOList)) {
            roleDTO.setPermitActCodeList(adminRoleActRelPOList.stream().map(item -> {
                return item.getPermitActCode();
            }).collect(Collectors.toList()));
        }

        return LangTuoResult.success(roleDTO);
    }

    @Override
    public LangTuoResult<PageDTO<RoleDTO>> search(String tenantCode, String roleName, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<RoleDTO>> langTuoResult = null;
        try {
            PageInfo<RolePO> pageInfo = adminRoleAccessor.search(tenantCode, roleName, pageNum, pageSize);
            List<RoleDTO> dtoList = pageInfo.getList().stream()
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

            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
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
            PageInfo<RolePO> pageInfo = adminRoleAccessor.selectList(tenantCode, pageNum, pageSize);
            List<RoleDTO> dtoList = pageInfo.getList().stream()
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

            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<RoleDTO>> list(String tenantCode) {
        LangTuoResult<List<RoleDTO>> langTuoResult = null;
        try {
            List<RolePO> list = adminRoleAccessor.selectList(tenantCode);
            List<RoleDTO> dtoList = list.stream()
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

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(RolePutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        RolePO rolePO = convert(request);
        List<AdminRoleActRelPO> adminRoleActRelPOList = convertRoleActRel(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            RolePO exist = adminRoleAccessor.selectOne(request.getTenantCode(), request.getRoleCode());
            System.out.printf("$$$$$ AdminRoleMgtServiceImpl#put exist=%s\n", exist);
            if (exist != null) {
                int updated = adminRoleAccessor.update(rolePO);
                System.out.printf("$$$$$ AdminRoleMgtServiceImpl#put updated=%s\n", updated);
            } else {
                int inserted = adminRoleAccessor.insert(rolePO);
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

    private RoleDTO convert(RolePO po) {
        if (po == null) {
            return null;
        }

        RoleDTO dto = new RoleDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setRoleCode(po.getRoleCode());
        dto.setRoleName(po.getRoleName());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());

        return dto;
    }

    private RolePO convert(RolePutRequest request) {
        if (request == null) {
            return null;
        }

        RolePO po = new RolePO();
        po.setRoleCode(request.getRoleCode());
        po.setRoleName(request.getRoleName());
        po.setComment(request.getComment());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());

        return po;
    }

    private List<AdminRoleActRelPO> convertRoleActRel(RolePutRequest request) {
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
