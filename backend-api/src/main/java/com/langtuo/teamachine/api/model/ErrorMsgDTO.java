package com.langtuo.teamachine.api.model;

import lombok.Data;

@Data
public class ErrorMsgDTO {
    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 错误消息
     */
    private String errorMsg;
}
