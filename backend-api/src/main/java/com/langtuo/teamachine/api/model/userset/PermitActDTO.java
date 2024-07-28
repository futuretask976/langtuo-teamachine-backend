package com.langtuo.teamachine.api.model.userset;

import lombok.Data;

import java.util.Date;

@Data
public class PermitActDTO {
    /**
     * 租户编码
     */
    private String permitActCode;

    /**
     * 租户名称
     */
    private String permitActName;

    /**
     * 联系人
     */
    private String permitActGroupCode;
}
