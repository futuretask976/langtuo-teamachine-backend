package com.langtuo.teamachine.biz.service.impl.userset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.userset.OrgDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.userset.OrgPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.userset.OrgMgtService;
import com.langtuo.teamachine.dao.accessor.userset.OrgStrucAccessor;
import com.langtuo.teamachine.dao.po.userset.OrgPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrgMgtServiceImpl implements OrgMgtService {
    @Resource
    private OrgStrucAccessor orgStrucAccessor;

    @Override
    public LangTuoResult<OrgDTO> listByDepth(String tenantCode) {
        LangTuoResult<OrgDTO> langTuoResult = null;
        try {
            List<OrgPO> poList = orgStrucAccessor.selectList(tenantCode);
            List<OrgDTO> dtoList = poList.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            dtoList.forEach(item -> {
                List<OrgDTO> children = findOrgStrucPOByParent(item.getOrgName(), dtoList);
                if (!CollectionUtils.isEmpty(children)) {
                    item.setChildOrgNameList(children);
                }
            });

            langTuoResult = LangTuoResult.success(findTopOrgStrucPO(dtoList));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<OrgDTO>> list(String tenantCode) {
        LangTuoResult<List<OrgDTO>> langTuoResult = null;
        try {
            List<OrgPO> poList = orgStrucAccessor.selectList(tenantCode);
            List<OrgDTO> dtoList = poList.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<OrgDTO>> search(String tenantCode, String orgName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<OrgDTO>> langTuoResult = null;
        try {
            PageInfo<OrgPO> pageInfo = orgStrucAccessor.search(tenantCode, orgName, pageNum, pageSize);
            List<OrgDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<OrgDTO> get(String tenantCode, String orgName) {
        LangTuoResult<OrgDTO> langTuoResult = null;
        try {
            OrgPO orgPO = orgStrucAccessor.selectOne(tenantCode, orgName);
            OrgDTO orgDTO = convert(orgPO);

            langTuoResult = LangTuoResult.success(orgDTO);
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(OrgPutRequest request) {
        if (request == null
                || StringUtils.isBlank(request.getTenantCode())
                || StringUtils.isBlank(request.getOrgName())
                || StringUtils.isBlank(request.getParentOrgName())) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        String tenantCode = request.getTenantCode();
        String orgName = request.getOrgName();
        OrgPO orgPO = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            OrgPO exist = orgStrucAccessor.selectOne(tenantCode, orgName);
            System.out.printf("$$$$$ OrgStrucMgtServiceImpl#put exist=%s\n", exist);
            if (exist != null) {
                int updated = orgStrucAccessor.update(orgPO);
                System.out.printf("$$$$$ OrgStrucMgtServiceImpl#put updated=%s\n", updated);
            } else {
                int inserted = orgStrucAccessor.insert(orgPO);
                System.out.printf("$$$$$ OrgStrucMgtServiceImpl#put inserted=%s\n", inserted);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String orgName) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = orgStrucAccessor.delete(tenantCode, orgName);
            System.out.printf("$$$$$ OrgStrucMgtServiceImpl#delete deleted=%s\n", deleted);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private OrgDTO convert(OrgPO po) {
        if (po == null) {
            return null;
        }

        OrgDTO dto = new OrgDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setOrgName(po.getOrgName());
        dto.setParentOrgName(po.getParentOrgName());
        return dto;
    }

    private OrgPO convert(OrgPutRequest request) {
        if (request == null) {
            return null;
        }

        OrgPO po = new OrgPO();
        po.setTenantCode(request.getTenantCode());
        po.setOrgName(request.getOrgName());
        po.setParentOrgName(request.getParentOrgName());
        return po;
    }

    private List<OrgDTO> findOrgStrucPOByParent(String parentOrgName, List<OrgDTO> orgDTOList) {
        if (StringUtils.isBlank(parentOrgName) || CollectionUtils.isEmpty(orgDTOList)) {
            return null;
        }

        return orgDTOList.stream()
                .filter(item -> item.getParentOrgName() != null && item.getParentOrgName().equals(parentOrgName))
                .collect(Collectors.toList());
    }

    private OrgDTO findTopOrgStrucPO(List<OrgDTO> orgDTOList) {
        if (CollectionUtils.isEmpty(orgDTOList)) {
            return null;
        }

        for (OrgDTO item : orgDTOList) {
            if("总公司".equals(item.getOrgName())) {
                return item;
            }
        }
        return null;
    }
}