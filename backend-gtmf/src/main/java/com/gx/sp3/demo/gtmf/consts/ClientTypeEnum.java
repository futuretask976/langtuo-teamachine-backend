package com.gx.sp3.demo.gtmf.consts;

import lombok.Getter;

/**
 * @author miya
 */
public enum ClientTypeEnum {
    UNKNOWN("unknown", "未知来源"),
    TAOBAO_CLIENT("taobaoClient", "手淘"),
    TMALL_CLIENT("tmallClient", "猫客"),
    COMMON_H5_CLIENT("commonH5Client", "普通h5"),
    ALIPAY_H5_CLIENT("alipayH5Client", "支付宝h5"),
    ALIPAY_PROGRAM_CLIENT("alipayProgramClient", "支付宝小程序"),
    ;

    @Getter
    private String code;

    @Getter
    private String desc;

    ClientTypeEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static ClientTypeEnum fromCode(String code){
        if(null == code){
            return UNKNOWN;
        }
        for(ClientTypeEnum typeEnum : ClientTypeEnum.values()){
            if(typeEnum.code.equals(code)){
                return typeEnum;
            }
        }
        return UNKNOWN;
    }
}
