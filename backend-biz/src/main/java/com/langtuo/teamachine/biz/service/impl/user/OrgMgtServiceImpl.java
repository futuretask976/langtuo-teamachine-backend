package com.langtuo.teamachine.biz.service.impl.user;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.user.OrgDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.user.OrgPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.user.OrgMgtService;
import com.langtuo.teamachine.dao.accessor.user.OrgAccessor;
import com.langtuo.teamachine.dao.node.user.OrgNode;
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
public class OrgMgtServiceImpl implements OrgMgtService {
    /**
     * 组织架构中最高层次的名称
     */
    public static final String ORG_NAME_TOP = "总公司";

    @Resource
    private OrgAccessor orgAccessor;

    @Override
    public LangTuoResult<OrgDTO> getTop(String tenantCode) {
        LangTuoResult<OrgDTO> langTuoResult = null;
        try {
            OrgNode orgNode = orgAccessor.findTopOrgNode(tenantCode);
            langTuoResult = LangTuoResult.success(convert(orgNode));
        } catch (Exception e) {
            log.error("listByDepth error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<OrgDTO>> listByParent(String tenantCode, String orgName) {
        LangTuoResult<List<OrgDTO>> langTuoResult = null;
        try {
            List<OrgNode> orgNodeList = orgAccessor.selectListByParent(tenantCode, orgName);
            langTuoResult = LangTuoResult.success(convert(orgNodeList));
        } catch (Exception e) {
            log.error("listByDepth error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<OrgDTO>> list(String tenantCode) {
        LangTuoResult<List<OrgDTO>> langTuoResult = null;
        try {
            List<OrgNode> nodeList = orgAccessor.selectList(tenantCode);
            List<OrgDTO> dtoList = convert(nodeList);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
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
            PageInfo<OrgNode> pageInfo = orgAccessor.search(tenantCode, orgName, pageNum, pageSize);
            List<OrgDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<OrgDTO> get(String tenantCode, String orgName) {
        LangTuoResult<OrgDTO> langTuoResult = null;
        try {
            OrgNode orgNode = orgAccessor.selectOne(tenantCode, orgName);
            OrgDTO orgDTO = convert(orgNode);
            langTuoResult = LangTuoResult.success(orgDTO);
        } catch (Exception e) {
            log.error("get error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
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
        OrgNode orgNode = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            OrgNode exist = orgAccessor.selectOne(tenantCode, orgName);
            if (exist != null) {
                int updated = orgAccessor.update(orgNode);
            } else {
                int inserted = orgAccessor.insert(orgNode);
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

    private List<OrgDTO> convert(List<OrgNode> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<OrgDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private OrgDTO convert(OrgNode node) {
        if (node == null) {
            return null;
        }

        OrgDTO dto = new OrgDTO();
        dto.setGmtCreated(node.getGmtCreated());
        dto.setGmtModified(node.getGmtModified());
        dto.setOrgName(node.getOrgName());
        dto.setParentOrgName(node.getParentOrgName());

        if (!CollectionUtils.isEmpty(node.getChildren())) {
            List<OrgDTO> children = Lists.newArrayList();
            node.getChildren().forEach(child -> {
                children.add(convert(child));
            });
            dto.setChildren(children);
        }

        return dto;
    }

    private OrgNode convert(OrgPutRequest request) {
        if (request == null) {
            return null;
        }

        OrgNode node = new OrgNode();
        node.setTenantCode(request.getTenantCode());
        node.setOrgName(request.getOrgName());
        node.setParentOrgName(request.getParentOrgName());
        return node;
    }
}
