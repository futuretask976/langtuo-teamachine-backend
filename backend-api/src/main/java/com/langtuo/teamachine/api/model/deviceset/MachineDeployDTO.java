package com.langtuo.teamachine.api.model.deviceset;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MachineDeployDTO {
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
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

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
     * 店铺名称
     */
    private String shopName;

    /**
     * 部署状态，0：未部署，1：已部署
     */
    private Integer state;
}
