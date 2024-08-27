package com.langtuo.teamachine.api.constant;

public enum ErrorEnum {
    // 测试
    TEST_ERR_ONLY_TEST("TEST_ERR_ONLY_TEST", "测试返回"),

    // 登录错误
    LOGIN_ERR_UNAUTHENTICATED("LOGIN_ERR_UNAUTHENTICATED", "登录错误"),
    LOGIN_ERR_UNAUTHORIZED("LOGIN_ERR_UNAUTHORIZED", "授权错误"),

    // 业务逻辑错误
    BIZ_ERR_ILLEGAL_ARGUMENT("BIZ_ERR_ILLEGAL_ARGUMENT", "输入参数错误"),
    BIZ_ERR_CANNOT_DELETE_USING_ROLE("BIZ_ERR_CANNOT_DELETE_USING_ROLE", "不能删除正在使用的角色"),
    BIZ_ERR_CANNOT_DELETE_TENANT_SUPER_ADMIN_ROLE("BIZ_ERR_CANNOT_DELETE_TENANT_SUPER_ADMIN_ROLE", "不能更改/删除租户超级管理员"),
    BIZ_ERR_CANNOT_DELETE_USING_SHOP_GROUP("BIZ_ERR_CANNOT_DELETE_USING_SHOP_GROUP", "不能删除正在使用的店铺组"),
    BIZ_ERR_CANNOT_DELETE_USING_MODEL("BIZ_ERR_CANNOT_DELETE_USING_MODEL", "不能删除正在使用的型号"),
    BIZ_ERR_CANNOT_DELETE_USING_TEA("BIZ_ERR_CANNOT_DELETE_USING_TEA", "不能删除正在使用的茶品"),
    BIZ_ERR_CANNOT_DELETE_USING_SERIES("BIZ_ERR_CANNOT_DELETE_USING_SERIES", "不能删除正在使用的系列"),
    BIZ_ERR_UPLOAD_FILE_IS_EMPTY("BIZ_ERR_UPLOAD_FILE_IS_EMPTY", "上传的文件为空"),
    BIZ_ERR_PARSE_UPLOAD_FILE_ERROR("BIZ_ERR_PARSE_UPLOAD_FILE_ERROR", "解析上传的文件错误"),

    // 数据库错误
    DB_ERR_SELECT_FAIL("DB_ERR_QUERY_FAIL", "数据查询失败"),
    DB_ERR_INSERT_FAIL("DB_ERR_INSERT_FAIL", "数据保存失败"),
    DB_ERR_UPDATE_FAIL("DB_ERR_INSERT_FAIL", "数据更新失败"),
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
