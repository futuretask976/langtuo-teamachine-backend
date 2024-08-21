package com.langtuo.teamachine.api.result;

import com.langtuo.teamachine.api.constant.ErrorEnum;
import lombok.Data;

import java.util.List;

@Data
public class TeaMachineResult<T> {
    private boolean success;

    private String errorCode;

    private String errorMsg;

    private T model;

    public static <U> TeaMachineResult success(U model) {
        TeaMachineResult<U> teaMachineResult = new TeaMachineResult<>();
        teaMachineResult.setSuccess(true);
        teaMachineResult.setModel(model);
        return teaMachineResult;
    }

    public static <U> TeaMachineResult success() {
        TeaMachineResult<U> teaMachineResult = new TeaMachineResult<>();
        teaMachineResult.setSuccess(true);
        return teaMachineResult;
    }

    public static <U> TeaMachineResult error(ErrorEnum errorEnum) {
        TeaMachineResult<U> teaMachineResult = new TeaMachineResult<>();
        teaMachineResult.setSuccess(false);
        teaMachineResult.setErrorCode(errorEnum.getErrorCode());
        teaMachineResult.setErrorMsg(errorEnum.getErrorMsg());
        return teaMachineResult;
    }

    public static <T> T getModel(TeaMachineResult<T> result) {
        if (result == null || !result.isSuccess() || result.getModel() == null) {
            return null;
        }
        return result.getModel();
    }

    public static <T> List<T> getListModel(TeaMachineResult<List<T>> result) {
        if (result == null || !result.isSuccess() || result.getModel() == null) {
            return null;
        }
        List<T> list = result.getModel();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }
}
