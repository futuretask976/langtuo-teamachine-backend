package com.langtuo.teamachine.api.model.menuset;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class SeriesDTO {
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
    private String seriesCode;

    /**
     * 系列名称
     */
    private String seriesName;

    /**
     * 图片链接
     */
    private String imgLink;

    /**
     *
     */
    private List<SeriesTeaRelDTO> seriesTeaRelList;
}
