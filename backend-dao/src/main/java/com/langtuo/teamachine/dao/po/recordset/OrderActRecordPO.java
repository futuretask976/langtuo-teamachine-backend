package com.langtuo.teamachine.dao.po.recordset;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class OrderActRecordPO {
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
     * 设备编码
     */
    private String machineCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 订单生成时间，这里用String的原因是为了做索引幂等
     */
    private String orderGmtCreated;

    /**
     * 外部订单id
     */
    private String outerOrderId;

    /**
     * 状态，0：未制作，1：制作中，2：已制作，3：有异常，4：已取消
     */
    private Integer state;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息
     */
    private Map<String,String> extraInfo;
}
