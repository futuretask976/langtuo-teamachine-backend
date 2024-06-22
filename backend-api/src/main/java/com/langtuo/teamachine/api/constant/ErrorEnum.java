package com.langtuo.teamachine.api.constant;

public enum ErrorEnum {
    // 业务逻辑错误
    BIZ_ERR_ONLY_TEST("BIZ_ERR_ONLY_TEST", "测试返回")
    , BIZ_ERR_ILLEGAL_ARGUMENT("BIZ_ERR_ILLEGAL_ARGUMENT", "输入参数错误")

    // 数据库错误
    , DB_ERR_QUERY_FAIL("DB_ERR_QUERY_FAIL", "数据查询失败")
    , DB_ERR_INSERT_FAIL("DB_ERR_INSERT_FAIL", "数据保存失败")
    ;


    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 错误消息
     */
    private String errorMsg;

    ErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
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
}
