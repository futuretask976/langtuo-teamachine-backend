package com.langtuo.teamachine.api.model.drink;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ToppingTypeDTO {
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
     * 物料类型编码
     */
    private String toppingTypeCode;

    /**
     * 物料类型名称
     */
    private String toppingTypeName;

    /**
     * 店铺类型，0：禁用，1：启用
     */
    private int state;
}
