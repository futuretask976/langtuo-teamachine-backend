package com.langtuo.teamachine.web.constant;

public class WebConsts {
    /**
     * 导出文件使用，指定disposition的name
     */
    public static final String HTTP_HEADER_DISPOSITION_NAME = "attachment";

    /**
     * 导出文件使用，指定disposition的fileName
     */
    public static final String HTTP_HEADER_DISPOSITION_FILE_NAME_4_DEPLOY_EXPORT = "deploy_export.xlsx";

    /**
     * 导出文件使用，指定disposition的fileName
     */
    public static final String HTTP_HEADER_DISPOSITION_FILE_NAME_4_TEA_EXPORT = "tea_export.xlsx";

    /**
     * HTTP Request 头部字段
     */
    public static final String REQ_HEADER_MACHINE_CODE = "Machine-Code";
    public static final String REQ_HEADER_TENANT_CODE = "Tenant-Code";
    public static final String REQ_HEADER_DEPLOY_CODE = "Deploy-Code";

    /**
     * HTTP Response 头部字段
     */
    public static final String RESP_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String RESP_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String RESP_HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String RESP_HEADER_VAL_ALL = "*";
    public static final String RESP_HEADER_VAL_TRUE = "true";
    public static final String RESP_HEADER_VAL_NO_CACHE = "no-cache";
    public static final String RESP_HEADER_VAL_ENCODING_UTF8 = "UTF-8";
    public static final String RESP_HEADER_VAL_CONT_TYPE_APPLICATIONJSON = "application/json";

    /**
     * Cookie 相关字段
     */
    public static final String COOKIE_NAME_JSESSION_ID = "JSESSIONID";
    public static final int COOKIE_MAX_AGE = 0;
    public static final String COOKIE_PATH = "/";

    /**
     * Session 相关字段
     */
    public static final String SESSION_ATTR_USER = "user";
    public static final String SESSION_ATTR_USERNAME = "username";
    public static final String SESSION_ATTR_AUTHORITIES = "authorities";

    /**
     * JSON 字段相关
     */
    public static final String JSON_KEY_LOGIN_SUCCESS = "loginSuccess";
    public static final String JSON_KEY_ACCESS_SUCCESS = "accessSuccess";
    public static final String JSON_VAL_TRUE = "true";
    public static final String JSON_VAL_FALSE = "false";
}
