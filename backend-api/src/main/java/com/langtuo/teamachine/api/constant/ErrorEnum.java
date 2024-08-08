package com.langtuo.teamachine.api.constant;

public enum ErrorEnum {
    // 测试
    TEST_ERR_ONLY_TEST("TEST_ERR_ONLY_TEST", "测试返回"),

    // 登录错误
    LOGIN_ERR_UNAUTHENTICATED("LOGIN_ERR_UNAUTHENTICATED", "登录错误"),
    LOGIN_ERR_UNAUTHORIZED("LOGIN_ERR_UNAUTHORIZED", "授权错误"),

    // 业务逻辑错误
    BIZ_ERR_ILLEGAL_ARGUMENT("BIZ_ERR_ILLEGAL_ARGUMENT", "输入参数错误"),
    BIZ_ERR_TRY_DELETE_USING_ROLE("BIZ_ERR_TRY_DELETE_USING_ROLE", "尝试删除正在使用的角色"),

    // 数据库错误
    DB_ERR_QUERY_FAIL("DB_ERR_QUERY_FAIL", "数据查询失败"),
    DB_ERR_INSERT_FAIL("DB_ERR_INSERT_FAIL", "数据保存失败"),
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
