package com.langtuo.teamachine.biz.service.impl.userset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.userset.OrgDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.userset.OrgPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.userset.OrgMgtService;
import com.langtuo.teamachine.dao.accessor.userset.OrgAccessor;
import com.langtuo.teamachine.dao.po.userset.OrgPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrgMgtServiceImpl implements OrgMgtService {
    @Resource
    private OrgAccessor orgAccessor;

    @Override
    public LangTuoResult<OrgDTO> listByDepth(String tenantCode) {
        LangTuoResult<OrgDTO> langTuoResult = null;
        try {
            List<OrgPO> poList = orgAccessor.selectList(tenantCode);
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
            log.error("listByDepth error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<OrgDTO>> list(String tenantCode) {
        LangTuoResult<List<OrgDTO>> langTuoResult = null;
        try {
            List<OrgPO> poList = orgAccessor.selectList(tenantCode);
            List<OrgDTO> dtoList = convert(poList);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
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
            PageInfo<OrgPO> pageInfo = orgAccessor.search(tenantCode, orgName, pageNum, pageSize);
            List<OrgDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<OrgDTO> get(String tenantCode, String orgName) {
        LangTuoResult<OrgDTO> langTuoResult = null;
        try {
            OrgPO orgPO = orgAccessor.selectOne(tenantCode, orgName);
            OrgDTO orgDTO = convert(orgPO);
            langTuoResult = LangTuoResult.success(orgDTO);
        } catch (Exception e) {
            log.error("get error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(OrgPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        String tenantCode = request.getTenantCode();
        String orgName = request.getOrgName();
        OrgPO orgPO = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            OrgPO exist = orgAccessor.selectOne(tenantCode, orgName);
            if (exist != null) {
                int updated = orgAccessor.update(orgPO);
            } else {
                int inserted = orgAccessor.insert(orgPO);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
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
            int deleted = orgAccessor.delete(tenantCode, orgName);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private List<OrgDTO> convert(List<OrgPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<OrgDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
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
