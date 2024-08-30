package com.langtuo.teamachine.biz.service.constant;

import com.google.common.collect.Lists;

import java.util.List;

public class BizConsts {
    /**
     * 最小页码
     */
    public static final int MIN_PAGE_NUM = 1;

    /**
     * 最小每页展示数
     */
    public static final int MIN_PAGE_SIZE = 1;

    /**
     * 数字 0
     */
    public static final int NUM_ZERO = 0;

    /**
     * 数字 1
     */
    public static final int NUM_ONE = 1;

    /**
     * 横杠
     */
    public static final String STR_HORIZONTAL_BAR = "-";

    /**
     *
     */
    public static final int DB_SELECT_RESULT_EMPTY = 0;



    /**
     * 茶品导出 - sheet名称
     */
    public static final String SHEET_NAME_4_TEA_EXPORT = "茶品信息导出";

    /**
     * sheet索引
     */
    public static final int SHEET_NUM_4_DATA = 0;

    /**
     * 标题行索引
     */
    public static final int ROW_NUM_4_TITLE = 0;

    /**
     * 数据行索引
     */
    public static final int ROW_START_NUM_4_DATA = 1;

    /**
     * 茶品导出 - 列标题
     */
    public static final String TITLE_TEA_CODE = "茶品编码";
    public static final int TITLE_TEA_CODE_INDEX = 0;
    public static final String TITLE_TEA_NAME = "茶品名称";
    public static final int TITLE_TEA_NAME_INDEX = 1;
    public static final String TITLE_OUTER_TEA_CODE = "外部茶品编码";
    public static final int TITLE_OUTER_TEA_CODE_INDEX = 2;
    public static final String TITLE_STATE = "状态";
    public static final int TITLE_STATE_INDEX = 3;
    public static final String TITLE_TEA_TYPE_CODE = "茶品类型编码";
    public static final int TITLE_TEA_TYPE_CODE_INDEX = 4;
    public static final String TITLE_IMG_LINK = "图片链接";
    public static final int TITLE_IMG_LINK_INDEX = 5;
    public static final String TITLE_TEA_UNIT_NAME = "规格单位名称";
    public static final int TITLE_TEA_UNIT_NAME_INDEX = 6;
    public static final String TITLE_STEP_INDEX = "步骤序号";
    public static final int TITLE_STEP_INDEX_INDEX = 7;
    public static final String TITLE_TOPPING_CODE = "物料编码";
    public static final int TITLE_TOPPING_CODE_INDEX = 8;
    public static final String TITLE_TOPPING_NAME = "物料名称";
    public static final int TITLE_TOPPING_NAME_INDEX = 9;
    public static final String TITLE_ACTUAL_AMOUNT = "实际用量";
    public static final int TITLE_ACTUAL_AMOUNT_INDEX = 10;

    /**
     * 茶品导出 - 标题行
     */
    public static List<String> TITLE_LIST_4_TEA_EXPORT = Lists.newArrayList(
            TITLE_TEA_CODE,
            TITLE_TEA_NAME,
            TITLE_OUTER_TEA_CODE,
            TITLE_STATE,
            TITLE_TEA_TYPE_CODE,
            TITLE_IMG_LINK,
            TITLE_TEA_UNIT_NAME,
            TITLE_STEP_INDEX,
            TITLE_TOPPING_CODE,
            TITLE_TOPPING_NAME,
            TITLE_ACTUAL_AMOUNT);

    /**
     * 茶品 - 起始行num
     */
    public static final int ROW_START_NUM_4_TEA = 1;

    /**
     * 茶品 - 基础信息部分起始列num
     */
    public static final int COL_START_NUM_4_TEA_INFO = 0;

    /**
     * 茶品 - 规格部分起始列num
     */
    public static final int COL_START_NUM_4_TEA_UNIT = 6;

    /**
     * 茶品 - 调整规则部分起始列num
     */
    public static final int COL_START_NUM_4_ADJUST_RULE = 7;

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
            "创建时间",
            "修改时间",
            "商户编码",
            "商户名称",
            "部署编码",
            "机器编码",
            "型号编码",
            "店铺编码",
            "店铺名称",
            "状态");

    /**
     * 部署导出 - 起始行num
     */
    public static final int ROW_START_NUM_4_DEPLOY = 1;

    /**
     * 部署导出 - 起始列num
     */
    public static final int COL_START_NUM_4_DEPLOY = 0;
}
