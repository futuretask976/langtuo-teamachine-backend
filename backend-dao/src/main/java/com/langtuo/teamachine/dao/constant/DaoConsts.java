package com.langtuo.teamachine.dao.constant;

public class DaoConsts {
    /**
     * DB操作插入一条记录成功
     */
    public static int INSERTED_ONE_ROW = 1;

    /**
     * DB操作删除一条记录成功
     */
    public static int UPDATED_ONE_ROW = 1;

    /**
     * DB操作删除一条记录成功
     */
    public static int DELETED_ONE_ROW = 1;

    /**
     * DB操作删除记录失败
     */
    public static int DELETED_ZERO_ROW = 0;

    /**
     * 系统超级管理员登录名
     */
    public static final String ADMIN_SYS_SUPER_LOGIN_NAME = "sys_super_admin";

    /**
     * 系统超级管理员登录密码
     * 经过md5加密后的密码，原始密码是SYS_SUPER_ADMIN
     */
    public static final String ADMIN_SYS_SUPER_PASSWORD = "5505b50f5f0ec77b27a0ea270b21e7f0";

    /**
     * 系统超级管理员角色
     */
    public static final String ROLE_SYS_SUPER = "SYS_SUPER_ROLE";

    /**
     *  系统保留角色
     */
    public static final int ROLE_SYS_RESERVED = 1;

    /**
     * 组织架构中最高层次的名称
     */
    public static final String ORG_NAME_TOP = "总公司";

    public static final String PERMIT_ACT_CODE_TENANT = "tenant_mgt";
}
