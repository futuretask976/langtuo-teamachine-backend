package com.langtuo.teamachine.api.model.userset;

import lombok.Data;

import java.util.Date;

@Data
public class PermitActDTO {
    /**
     * 权限编码
     */
    private String permitActCode;

    /**
     * 权限名称
     */
    private String permitActName;

    /**
     * 权限组编码
     */
    private String permitActGroupCode;
}
