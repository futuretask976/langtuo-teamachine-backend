package com.langtuo.teamachine.api.model;

import com.langtuo.teamachine.api.request.SpecItemRulePutRequest;
import com.langtuo.teamachine.api.request.ToppingAdjustRulePutRequest;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SpecItemRuleDTO {
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
     * 规格项编码
     */
    private String specItemCode;

    /**
     * 规格项名称
     */
    private String specItemName;

    /**
     * 外部规格项编码
     */
    private String outerSpecItemCode;

    /**
     * 是否选中
     */
    private int selected;
}
