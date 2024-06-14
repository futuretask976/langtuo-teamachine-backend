package com.gx.sp3.demo.dao.pojo.langtuo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MachineTeaToppingPojo {
    /**
     *
     */
    private long id;

    /**
     *
     */
    private Date gmtCreated;

    /**
     *
     */
    private Date gmtModified;

    /**
     *
     */
    private String machineCode;

    /**
     *
     */
    private String teaCode;

    /**
     *
     */
    private String toppingCode;

    /**
     *
     */
    private Map<String, String> extraInfoMap;
}
