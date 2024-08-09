package com.langtuo.teamachine.dao.accessor.user;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.langtuo.teamachine.dao.mapper.user.OrgMapper;
import com.langtuo.teamachine.dao.po.user.OrgPO;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
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

    private List<OrgPO> listOrgPO(String tenantCode) {
        if (orgNodeMapByTenant.get(tenantCode) == null) {
            initOrgNodeMapByTenant(tenantCode);
        }

        Map<String, OrgNode> orgNodeMap = orgNodeMapByTenant.get(tenantCode);
        List<OrgPO> list = Lists.newArrayList();
        for (Map.Entry<String, OrgNode> entry : orgNodeMap.entrySet()) {
            list.add(convert(entry.getValue()));
        }
        return list;
    }

    public OrgPO selectOne(String tenantCode, String orgName) {
        if (orgNodeMapByTenant.get(tenantCode) == null) {
            initOrgNodeMapByTenant(tenantCode);
        }

        Map<String, OrgNode> orgNodeMap = orgNodeMapByTenant.get(tenantCode);
        OrgNode orgNode = orgNodeMap.get(orgName);
        return convert(orgNode);
    }

    public List<OrgPO> selectList(String tenantCode) {
        return listOrgPO(tenantCode);
    }

    public List<OrgPO> selectListByDepth(String tenantCode) {
        return listOrgPO(tenantCode);
    }

    public PageInfo<OrgPO> search(String tenantCode, String orgName, int pageNum, int pageSize) {
        List<OrgPO> poList = listOrgPO(tenantCode);
        if (CollectionUtils.isEmpty(poList)) {
            PageInfo<OrgPO> pageInfo = new PageInfo(null);
            pageInfo.setTotal(0);
            return pageInfo;
        }

        poList = poList.stream()
                .filter(po -> StringUtils.isBlank(orgName) || orgName.equals(po.getOrgName()))
                .collect(Collectors.toList());
        int startIdx = (pageNum - 1) * pageSize;
        int endIdx = pageNum * pageSize > poList.size() ? poList.size() : pageNum * pageSize;

        List<OrgPO> list = Lists.newArrayList();
        for (int i = startIdx; i < endIdx; i++) {
            list.add(poList.get(i));
        }

        PageInfo<OrgPO> pageInfo = new PageInfo(list);
        pageInfo.setTotal(poList.size());
        return pageInfo;
    }

    public int insert(OrgPO po) {
        int inserted = mapper.insert(po);
        if (inserted == 1) {
            orgNodeMapByTenant.remove(po.getTenantCode());
        }
        return inserted;
    }

    public int update(OrgPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            orgNodeMapByTenant.remove(po.getTenantCode());
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

    @Data
    class OrgNode {
        /**
         * 数据表id
         */
        private long id;

        /**
         * 数据表记录插入时间
         */
        private Date gmtCreated;

        /**
         * 数据表记录最近修改时间
         */
        private Date gmtModified;

        /**
         * 租户编码
         */
        private String tenantCode;

        /**
         * 组织名称
         */
        private String orgName;

        /**
         * 父节点名称
         */
        private String parentOrgName;

        /**
         * 父节点
         */
        private OrgNode parent;

        /**
         * 子节点列表
         */
        private List<OrgNode> children;
    }
}
