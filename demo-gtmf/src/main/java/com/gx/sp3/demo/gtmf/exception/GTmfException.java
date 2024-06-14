package com.gx.sp3.demo.gtmf.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author miya
 */
public class GTmfException extends RuntimeException {
    public enum ErrorCode {
        ILLEGAL_ARGUMENT_ERROR,
        UNKNOWN_ERROR;
    }

    @Getter
    @Setter
    private ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;

    @Getter
    @Setter
    private String errorParameter = "";

    public GTmfException() {
    }

    public GTmfException(String message) {
        super(message);
    }

    public GTmfException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public GTmfException(ErrorCode errorCode, String errorParameter) {
        this.errorCode = errorCode;
        this.errorParameter = errorParameter;
    }
}
