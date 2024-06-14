package com.gx.sp3.demo.gtmf.gray;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author miya
 */
@Data
public class ClientVersionConfig {
    /**
     *
     */
    private boolean enable = true;

    /**
     *
     */
    private String versionGreaterEqualThan = "0";

    /**
     *
     */
    private Set<String> versionNotSupport = new HashSet<>();
}
