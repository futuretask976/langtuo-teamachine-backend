package com.langtuo.teamachine.api.model.menu;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class MenuDTO {
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
     * 系列编码
     */
    private String menuCode;

    /**
     * 系列名称
     */
    private String menuName;

    /**
     * 图片链接
     */
    private String imgLink;

    /**
     *
     */
    private Date validFrom;

    /**
     *
     */
    private List<MenuSeriesRelDTO> menuSeriesRelList;
}
