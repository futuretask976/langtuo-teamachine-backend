package com.langtuo.teamachine.api.model.drinkset;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TeaUnitDTO {
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
     * 茶品单位编码
     */
    private String teaUnitCode;

    /**
     * 茶品单位名称
     */
    private String teaUnitName;

    /**
     *
     */
    private List<SpecItemRuleDTO> specItemRuleList;

    /**
     *
     */
    private List<ToppingAdjustRuleDTO> toppingAdjustRuleList;
}