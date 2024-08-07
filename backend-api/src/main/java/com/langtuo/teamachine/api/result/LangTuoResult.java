package com.langtuo.teamachine.api.result;

import com.langtuo.teamachine.api.constant.ErrorEnum;
import lombok.Data;

import java.util.List;

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

    public static <T> T getModel(LangTuoResult<T> result) {
        if (result == null || !result.isSuccess() || result.getModel() == null) {
            return null;
        }
        return result.getModel();
    }

    public static <T> List<T> getListModel(LangTuoResult<List<T>> result) {
        if (result == null || result.isSuccess() || result.getModel() == null) {
            return null;
        }
        List<T> list = result.getModel();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }
}
