package com.langtuo.teamachine.dao.po;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MachineDeployPO {
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
     * 部署编码
     */
    private String deployCode;

    /**
     * 型号编码
     */
    private String modelCode;

    /**
     * 机器编码
     */
    private String machineCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;
}
