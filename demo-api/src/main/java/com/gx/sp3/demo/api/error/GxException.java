package com.gx.sp3.demo.api.error;

/**
 * 未来可以用于多语言
 */
public class GxException {
    private String message;

    private GxException() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static GxException of(Exception e) {
        GxException gxException = new GxException();
        gxException.setMessage(e.getMessage());
        return gxException;
    }

    public static GxException of(String message) {
        GxException gxException = new GxException();
        gxException.setMessage(message);
        return gxException;
    }
}
