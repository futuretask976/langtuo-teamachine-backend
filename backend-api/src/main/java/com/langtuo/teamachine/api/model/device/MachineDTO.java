package com.langtuo.teamachine.api.model.device;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MachineDTO {
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
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 机器编码
     */
    private String machineCode;

    /**
     * 机器名称
     */
    private String machineName;

    /**
     * 屏幕编码
     */
    private String screenCode;

    /**
     * 电控板编码
     */
    private String elecBoardCode;

    /**
     *　店铺编码
     */
    private String shopCode;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺组编码
     */
    private String shopGroupCode;

    /**
     * 店铺组名称
     */
    private String shopGroupName;

    /**
     * 型号编码
     */
    private String modelCode;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 机器状态，0：禁用，1：启用
     */
    private int state;

    /**
     * 有效期
     */
    private Date validUntil;

    /**
     * 保修期
     */
    private Date maintainUntil;
}
