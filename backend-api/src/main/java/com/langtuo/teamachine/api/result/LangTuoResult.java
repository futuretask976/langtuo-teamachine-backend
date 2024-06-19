package com.langtuo.teamachine.api.result;

import com.langtuo.teamachine.api.constant.ErrorEnum;

public class LangTuoResult<T> {
    private boolean success;

    private String errorCode;

    private String errorMsg;

    private T model;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

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
