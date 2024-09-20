package com.langtuo.teamachine.internal.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String transformYMD(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String transformYMDHMS(Date date) {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }
}
