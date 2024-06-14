package com.gx.sp3.demo.gtmf.monitor;

import lombok.Data;

@Data
public class EagleEye {
    public static String getTraceId() {
        return "eagleEyeTraceId";
    }
}
