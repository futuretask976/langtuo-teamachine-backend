package com.langtuo.teamachine.api.model.drink;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class SpecDTO {
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
     * 规格编码
     */
    private String specCode;

    /**
     * 规格名称
     */
    private String specName;

    /**
     * 状态，0：禁用，1：启用
     */
    private int state;

    /**
     *
     */
    private List<SpecItemDTO> specItemList;
}
