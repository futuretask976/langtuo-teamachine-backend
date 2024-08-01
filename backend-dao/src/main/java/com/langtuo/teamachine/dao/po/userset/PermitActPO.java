package com.langtuo.teamachine.dao.po.userset;

import lombok.Data;

import java.util.Date;

@Data
public class PermitActPO {
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
