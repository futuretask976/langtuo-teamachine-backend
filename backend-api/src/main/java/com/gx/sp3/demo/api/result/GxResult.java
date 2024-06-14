package com.gx.sp3.demo.api.result;

import com.gx.sp3.demo.api.error.GxException;

public class GxResult<T> {
    private boolean success;

    private GxException exception;

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

    public GxException getException() {
        return exception;
    }

    public void setException(GxException exception) {
        this.exception = exception;
    }

    public static <U>GxResult success(U model) {
        GxResult<U> gxResult = new GxResult<>();
        gxResult.setSuccess(true);
        gxResult.setModel(model);
        return gxResult;
    }

    public static <U> GxResult error(Exception e) {
        GxResult<U> gxResult = new GxResult<>();
        gxResult.setSuccess(false);
        gxResult.setException(GxException.of(e));
        return gxResult;
    }

    public static <U> GxResult error(String message) {
        GxResult<U> gxResult = new GxResult<>();
        gxResult.setSuccess(false);
        gxResult.setException(GxException.of(message));
        return gxResult;
    }
}
