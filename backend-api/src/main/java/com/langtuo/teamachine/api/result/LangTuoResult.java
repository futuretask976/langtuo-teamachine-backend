package com.langtuo.teamachine.api.result;

import com.langtuo.teamachine.api.constant.ErrorEnum;
import lombok.Data;

@Data
public class LangTuoResult<T> {
    private boolean success;

    private String errorCode;

    private String errorMsg;

    private T model;

    public static <U> LangTuoResult success(U model) {
        LangTuoResult<U> langTuoResult = new LangTuoResult<>();
        langTuoResult.setSuccess(true);
        langTuoResult.setModel(model);
        return langTuoResult;
    }

    public static <U> LangTuoResult success() {
        LangTuoResult<U> langTuoResult = new LangTuoResult<>();
        langTuoResult.setSuccess(true);
        return langTuoResult;
    }

    public static <U> LangTuoResult error(ErrorEnum errorEnum) {
        LangTuoResult<U> langTuoResult = new LangTuoResult<>();
        langTuoResult.setSuccess(false);
        langTuoResult.setErrorCode(errorEnum.getErrorCode());
        langTuoResult.setErrorMsg(errorEnum.getErrorMsg());
        return langTuoResult;
    }
}
