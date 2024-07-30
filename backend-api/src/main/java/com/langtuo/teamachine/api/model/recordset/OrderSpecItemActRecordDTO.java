package com.langtuo.teamachine.api.model.recordset;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class OrderSpecItemActRecordDTO {
    /**
     * 规格编码
     */
    private String specCode;

    /**
     * 规格项编码
     */
    private String specItemCode;
}
