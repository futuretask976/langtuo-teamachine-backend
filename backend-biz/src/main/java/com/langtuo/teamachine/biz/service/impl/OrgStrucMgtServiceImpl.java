package com.langtuo.teamachine.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.OrgStrucDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.OrgStrucPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.OrgStrucMgtService;
import com.langtuo.teamachine.dao.accessor.OrgStrucAccessor;
import com.langtuo.teamachine.dao.po.OrgStrucPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrgStrucMgtServiceImpl implements OrgStrucMgtService {
    @Resource
    private OrgStrucAccessor orgStrucAccessor;

    @Override
    public LangTuoResult<OrgStrucDTO> listByDepth(String tenantCode) {
        LangTuoResult<OrgStrucDTO> langTuoResult = null;
        try {
            List<OrgStrucPO> poList = orgStrucAccessor.selectList(tenantCode);
            List<OrgStrucDTO> dtoList = poList.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            dtoList.forEach(item -> {
                List<OrgStrucDTO> children = findOrgStrucPOByParent(item.getOrgName(), dtoList);
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
    public LangTuoResult<List<OrgStrucDTO>> list(String tenantCode) {
        LangTuoResult<List<OrgStrucDTO>> langTuoResult = null;
        try {
            List<OrgStrucPO> poList = orgStrucAccessor.selectList(tenantCode);
            List<OrgStrucDTO> dtoList = poList.stream()
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
    public LangTuoResult<PageDTO<OrgStrucDTO>> search(String tenantCode, String orgName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<OrgStrucDTO>> langTuoResult = null;
        try {
            PageInfo<OrgStrucPO> pageInfo = orgStrucAccessor.search(tenantCode, orgName, pageNum, pageSize);
            List<OrgStrucDTO> dtoList = pageInfo.getList().stream()
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
    public LangTuoResult<OrgStrucDTO> get(String tenantCode, String orgName) {
        LangTuoResult<OrgStrucDTO> langTuoResult = null;
        try {
            OrgStrucPO orgStrucPO = orgStrucAccessor.selectOne(tenantCode, orgName);
            OrgStrucDTO orgStrucDTO = convert(orgStrucPO);

            langTuoResult = LangTuoResult.success(orgStrucDTO);
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(OrgStrucPutRequest request) {
        if (request == null
                || StringUtils.isBlank(request.getTenantCode())
                || StringUtils.isBlank(request.getOrgName())
                || StringUtils.isBlank(request.getParentOrgName())) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        String tenantCode = request.getTenantCode();
        String orgName = request.getOrgName();
        OrgStrucPO orgStrucPO = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            OrgStrucPO exist = orgStrucAccessor.selectOne(tenantCode, orgName);
            System.out.printf("$$$$$ OrgStrucMgtServiceImpl#put exist=%s\n", exist);
            if (exist != null) {
                int updated = orgStrucAccessor.update(orgStrucPO);
                System.out.printf("$$$$$ OrgStrucMgtServiceImpl#put updated=%s\n", updated);
            } else {
                int inserted = orgStrucAccessor.insert(orgStrucPO);
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

    private OrgStrucDTO convert(OrgStrucPO po) {
        if (po == null) {
            return null;
        }

        OrgStrucDTO dto = new OrgStrucDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setOrgName(po.getOrgName());
        dto.setParentOrgName(po.getParentOrgName());
        return dto;
    }

    private OrgStrucPO convert(OrgStrucPutRequest request) {
        if (request == null) {
            return null;
        }

        OrgStrucPO po = new OrgStrucPO();
        po.setTenantCode(request.getTenantCode());
        po.setOrgName(request.getOrgName());
        po.setParentOrgName(request.getParentOrgName());
        return po;
    }

    private List<OrgStrucDTO> findOrgStrucPOByParent(String parentOrgName, List<OrgStrucDTO> orgStrucDTOList) {
        if (StringUtils.isBlank(parentOrgName) || CollectionUtils.isEmpty(orgStrucDTOList)) {
            return null;
        }

        return orgStrucDTOList.stream()
                .filter(item -> item.getParentOrgName() != null && item.getParentOrgName().equals(parentOrgName))
                .collect(Collectors.toList());
    }

    private OrgStrucDTO findTopOrgStrucPO(List<OrgStrucDTO> orgStrucDTOList) {
        if (CollectionUtils.isEmpty(orgStrucDTOList)) {
            return null;
        }

        for (OrgStrucDTO item : orgStrucDTOList) {
            if("总公司".equals(item.getOrgName())) {
                return item;
            }
        }
        return null;
    }
}
