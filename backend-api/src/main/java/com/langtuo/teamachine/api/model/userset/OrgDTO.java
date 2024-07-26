package com.langtuo.teamachine.api.model.userset;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrgDTO {
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
     * 组织名称
     */
    private String orgName;

    /**
     * 父组织名称
     */
    private String parentOrgName;

    /**
     * 子组织列表
     */
    private List<OrgDTO> childOrgNameList;
}
