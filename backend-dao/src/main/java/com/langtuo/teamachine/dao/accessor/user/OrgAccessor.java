package com.langtuo.teamachine.dao.accessor.user;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.langtuo.teamachine.dao.mapper.user.OrgMapper;
import com.langtuo.teamachine.dao.node.user.OrgNode;
import com.langtuo.teamachine.dao.po.user.OrgPO;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrgAccessor {
    @Resource
    private OrgMapper mapper;

    /**
     * 基于商户的组织架构缓存
     */
    private Map<String, Map<String, OrgNode>> orgNodeMapByTenant = Maps.newHashMap();

    public OrgNode findTopOrgNode(String tenantCode) {
        if (orgNodeMapByTenant.get(tenantCode) == null) {
            initOrgNodeMapByTenant(tenantCode);
        }

        Map<String, OrgNode> orgNodeMap = orgNodeMapByTenant.get(tenantCode);
        for (Map.Entry<String, OrgNode> entry : orgNodeMap.entrySet()) {
            OrgNode orgNode = entry.getValue();
            if (orgNode.getParent() == null) {
                return orgNode;
            }
        }
        return null;
    }

    public OrgNode selectOne(String tenantCode, String orgName) {
        if (orgNodeMapByTenant.get(tenantCode) == null) {
            initOrgNodeMapByTenant(tenantCode);
        }

        Map<String, OrgNode> orgNodeMap = orgNodeMapByTenant.get(tenantCode);
        OrgNode orgNode = orgNodeMap.get(orgName);
        return orgNode;
    }

    public List<OrgNode> selectList(String tenantCode) {
        return listOrgNode(tenantCode);
    }

    public PageInfo<OrgNode> search(String tenantCode, String orgName, int pageNum, int pageSize) {
        List<OrgNode> nodeList = listOrgNode(tenantCode);
        if (CollectionUtils.isEmpty(nodeList)) {
            PageInfo<OrgNode> pageInfo = new PageInfo(null);
            pageInfo.setTotal(0);
            return pageInfo;
        }

        nodeList = nodeList.stream()
                .filter(po -> StringUtils.isBlank(orgName) || orgName.equals(po.getOrgName()))
                .collect(Collectors.toList());
        int startIdx = (pageNum - 1) * pageSize;
        int endIdx = pageNum * pageSize > nodeList.size() ? nodeList.size() : pageNum * pageSize;

        List<OrgNode> list = Lists.newArrayList();
        for (int i = startIdx; i < endIdx; i++) {
            list.add(nodeList.get(i));
        }

        PageInfo<OrgNode> pageInfo = new PageInfo(list);
        pageInfo.setTotal(nodeList.size());
        return pageInfo;
    }

    public int insert(OrgNode node) {
        int inserted = mapper.insert(convert(node));
        if (inserted == 1) {
            orgNodeMapByTenant.remove(node.getTenantCode());
        }
        return inserted;
    }

    public int update(OrgNode node) {
        int updated = mapper.update(convert(node));
        if (updated == 1) {
            orgNodeMapByTenant.remove(node.getTenantCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String orgName) {
        int deleted = mapper.delete(tenantCode, orgName);
        if (deleted == 1) {
            orgNodeMapByTenant.remove(tenantCode);
        }
        return deleted;
    }

    private OrgPO convert(OrgNode node) {
        OrgPO po = new OrgPO();
        po.setId(node.getId());
        po.setGmtCreated(node.getGmtCreated());
        po.setGmtModified(node.getGmtModified());
        po.setTenantCode(node.getTenantCode());
        po.setOrgName(node.getOrgName());
        po.setParentOrgName(node.getParentOrgName());
        return po;
    }

    private OrgNode convert(OrgPO po) {
        OrgNode node = new OrgNode();
        node.setId(po.getId());
        node.setGmtCreated(po.getGmtCreated());
        node.setGmtModified(po.getGmtModified());
        node.setTenantCode(po.getTenantCode());
        node.setOrgName(po.getOrgName());
        node.setParentOrgName(po.getParentOrgName());
        return node;
    }

    private void initOrgNodeMapByTenant(String tenantCode) {
        if (orgNodeMapByTenant.get(tenantCode) != null) {
            return;
        }

        List<OrgPO> list = mapper.selectList(tenantCode);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        Map<String, OrgNode> orgNodeMap = Maps.newHashMap();
        for (OrgPO po : list) {
            orgNodeMap.put(po.getOrgName(), convert(po));
        }
        for (Map.Entry<String, OrgNode> entry : orgNodeMap.entrySet()) {
            OrgNode orgNode = entry.getValue();
            String parentOrgName = orgNode.getParentOrgName();
            if (StringUtils.isBlank(parentOrgName)) {
                continue;
            }
            OrgNode parentOrgNode = orgNodeMap.get(parentOrgName);
            orgNode.setParent(parentOrgNode);
            if (parentOrgNode.getChildren() == null) {
                parentOrgNode.setChildren(Lists.newArrayList());
            }
            parentOrgNode.getChildren().add(orgNode);
        }
        orgNodeMapByTenant.put(tenantCode, orgNodeMap);
    }

    private List<OrgNode> listOrgNode(String tenantCode) {
        if (orgNodeMapByTenant.get(tenantCode) == null) {
            initOrgNodeMapByTenant(tenantCode);
        }

        List<OrgNode> list = Lists.newArrayList();
        convertOrgNodeToList(findTopOrgNode(tenantCode), list);
        return list;
    }

    private void convertOrgNodeToList(OrgNode orgNode, List<OrgNode> orgNodeList) {
        if (orgNode == null) {
            return;
        }
        if (orgNodeList == null) {
            orgNodeList = Lists.newArrayList();
        }
        orgNodeList.add(orgNode);

        if (!CollectionUtils.isEmpty(orgNode.getChildren())) {
            for (OrgNode child : orgNode.getChildren()) {
                convertOrgNodeToList(child, orgNodeList);
            }
        }
    }
}
