package com.langtuo.teamachine.api.model.userset;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class RoleDTO {
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
     * 备注
     */
    private String comment;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 关联的权限点列表
     */
    private List<String> permitActCodeList;
}
