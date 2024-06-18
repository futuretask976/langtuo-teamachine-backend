package com.langtuo.teamachine.dao.po;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class PermActPO {
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
    private String permActCode;

    /**
     * 租户名称
     */
    private String permActName;

    /**
     * 联系人
     */
    private String permActGroupCode;
}
