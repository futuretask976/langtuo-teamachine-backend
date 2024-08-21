package com.langtuo.teamachine.biz.service.constant;

import com.google.common.collect.Lists;

import java.util.List;

public class BizConsts {
    /**
     * 最小页码
     */
    public static int MIN_PAGE_NUM = 1;

    /**
     * 最小每页展示数
     */
    public static int MIN_PAGE_SIZE = 1;

    /**
     * 组织架构中最高层次的名称
     */
    public static final String ORG_NAME_TOP = "总公司";

    /**
     * 茶品导出 - sheet名称
     */
    public static final String SHEET_NAME_4_TEA_EXPORT = "茶品信息导出";

    /**
     * 标题行索引
     */
    public static final int ROW_NUM_4_TITLE = 0;

    /**
     * 茶品导出 - 标题行
     */
    public static List<String> TITLE_LIST_4_TEA_EXPORT = Lists.newArrayList(
            "茶品编码",
            "茶品名称",
            "外部茶品编码",
            "状态",
            "茶品类型编码",
            "图片链接",
            "步骤序号",
            "物料编码",
            "基础用量",
            "规格名称",
            "步骤序号",
            "物料编码",
            "调整类型",
            "调整模式",
            "调整用量");

    /**
     * 茶品 - 起始行num
     */
    public static final int ROW_START_NUM_4_TEA = 1;

    /**
     * 茶品 - 基础信息部分起始列num
     */
    public static final int COL_START_NUM_4_TEA_INFO = 0;

    /**
     * 茶品 - 基础规则部分起始列num
     */
    public static final int COL_START_NUM_4_BASE_RULE = 6;

    /**
     * 茶品 - 茶品unit部分起始列num
     */
    public static final int COL_START_NUM_4_UNIT = 9;

    /**
     * 茶品 - 调整规则部分起始列num
     */
    public static final int COL_START_NUM_4_ADJUST_RULE = 10;

    /**
     * 部署状态字段通用说明
     */
    public static final int DEPLOY_STATE_DISABLED = 0;
    public static final String DEPLOY_STATE_DISABLED_LABEL = "未部署";
    public static final int DEPLOY_STATE_ENABLED = 1;
    public static final String DEPLOY_STATE_ENABLED_LABEL = "已部署";

    /**
     * 状态字段通用说明
     */
    public static final int STATE_DISABLED = 0;
    public static final String STATE_DISABLED_LABEL = "禁用";
    public static final int STATE_ENABLED = 1;
    public static final String STATE_ENABLED_LABEL = "启用";

    /**
     * 茶品调整规则toppingAdjustType字段说明
     */
    public static final int TOPPING_ADJUST_TYPE_REDUCE = 0;
    public static final String TOPPING_ADJUST_TYPE_REDUCE_LABEL = "减少";
    public static final int TOPPING_ADJUST_TYPE_INCRESE = 1;
    public static final String TOPPING_ADJUST_TYPE_INCRESE_LABEL = "添加";

    /**
     * 茶品调整规则toppingAdjustMode字段说明
     */
    public static final int TOPPING_ADJUST_MODE_FIX = 0;
    public static final String TOPPING_ADJUST_MODE_FIX_LABEL = "固定值";
    public static final int TOPPING_ADJUST_MODE_PER = 1;
    public static final String TOPPING_ADJUST_MODE_PER_LABEL = "百分比";

    /**
     * 部署导出 - sheet名称
     */
    public static final String SHEET_NAME_4_DEPLOY_EXPORT = "部署信息导出";

    /**
     * 部署导出 - 标题行
     */
    public static List<String> TITLE_LIST_4_DEPLOY_EXPORT = Lists.newArrayList(
            "茶品编码",
            "茶品名称",
            "外部茶品编码",
            "状态",
            "茶品类型编码",
            "图片链接",
            "步骤序号",
            "物料编码",
            "基础用量",
            "规格名称",
            "步骤序号",
            "物料编码",
            "调整类型",
            "调整模式",
            "调整用量");

    /**
     * 部署导出 - 起始行num
     */
    public static final int ROW_START_NUM_4_DEPLOY = 1;

    /**
     * 部署导出 - 起始列num
     */
    public static final int COL_START_NUM_4_DEPLOY = 0;
}
