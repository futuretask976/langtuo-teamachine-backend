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

    /**
     * 部署码生成用 char_set
     */
    public static final String DEPLOY_CHAR_SET = "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 部署码长度
     */
    public static final int DEPLOY_CODE_LENGTH = 6;

    /**
     * 部署码补位用数字
     */
    public static final String DEPLOY_CODE_COVERING_NUM = "0";

    /**
     * 日期格式完整格式
     */
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd hh:mm:ss";

    /**
     * 日期格式简略格式
     */
    public static final String DATE_FORMAT_SIMPLE = "yyyyMMdd";

    /**
     * 收到的消息中的 key 关键字
     */
    public static final String RECEIVE_KEY_BIZ_CODE = "bizCode";
    public static final String RECEIVE_KEY_TENANT_CODE = "tenantCode";
    public static final String RECEIVE_KEY_MODEL_CODE = "modelCode";
    public static final String RECEIVE_KEY_MACHINE_CODE = "machineCode";
    public static final String RECEIVE_KEY_SHOP_CODE = "shopCode";
    public static final String RECEIVE_KEY_TEMPLATE_CODE = "templateCode";
    public static final String RECEIVE_KEY_MENU_CODE = "menuCode";
    public static final String RECEIVE_KEY_CLEAN_RULE_CODE = "cleanRuleCode";
    public static final String RECEIVE_KEY_DRAIN_RULE_CODE = "drainRuleCode";
    public static final String RECEIVE_KEY_WARNING_RULE_CODE = "warningRuleCode";

    /**
     * 发送的消息中的 key 关键字
     */
    public static final String SEND_KEY_BIZ_CODE = "bizCode";
    public static final String SEND_KEY_TENANT_CODE = "tenantCode";
    public static final String SEND_KEY_MODEL_CODE = "modelCode";
    public static final String SEND_KEY_MACHINE_CODE = "machineCode";
    public static final String SEND_KEY_TEMPLATE_CODE = "templateCode";
    public static final String SEND_KEY_SHOP_CODE = "shopCode";
    public static final String SEND_KEY_MENU_CODE = "menuCode";
    public static final String SEND_KEY_DRAIN_RULE_CODE = "drainRuleCode";
    public static final String SEND_KEY_CLEAN_RULE_CODE = "cleanRuleCode";
    public static final String SEND_KEY_WARNING_RULE_CODE = "warningRuleCode";
    public static final String SEND_KEY_MODEL = "model";
    public static final String SEND_KEY_MACHINE = "machine";
    public static final String SEND_KEY_MD5_AS_HEX = "md5AsHex";
    public static final String SEND_KEY_OSS_PATH = "ossPath";
    public static final String SEND_KEY_ACCURACY_TPL = "accuracyTpl";
    public static final String SEND_KEY_OPEN_RULE = "openRule";
    public static final String SEND_KEY_CLEAN_RULE = "cleanRule";
    public static final String SEND_KEY_WARNING_RULE = "warningRule";

    /**
     * console 用的消息 bizCode
     */
    public static final String BIZ_CODE_PREPARE_MODEL = "prepareModel";
    public static final String BIZ_CODE_PREPARE_MACHINE = "prepareMachine";
    public static final String BIZ_CODE_PREPARE_TENANT = "prepareTenant";
    public static final String BIZ_CODE_PREPARE_ACCURACY_TPL = "prepareAccuracy";
    public static final String BIZ_CODE_PREPARE_MENU = "prepareMenu";
    public static final String BIZ_CODE_PREPARE_MENU_INIT_LIST = "prepareMenuInitIist";
    public static final String BIZ_CODE_PREPARE_DRAIN_RULE = "prepareOpenRule";
    public static final String BIZ_CODE_PREPARE_CLEAN_RULE = "prepareCleanRule";
    public static final String BIZ_CODE_PREPARE_WARNING_RULE = "prepareWarningRule";

    /**
     * dispatch 用的消息 bizCode
     */
    public static final String BIZ_CODE_DISPATCH_ACCURACY = "dispatchAccuracy";
    public static final String BIZ_CODE_DISPATCH_CLEAN_RULE = "dispatchCleanRule";
    public static final String BIZ_CODE_DISPATCH_MACHINE = "dispatchMachine";
    public static final String BIZ_CODE_DISPATCH_MENU = "dispatchMenu";
    public static final String BIZ_CODE_DISPATCH_MENU_INIT_LIST = "dispatchMenuInitList";
    public static final String BIZ_CODE_DISPATCH_MODEL = "dispatchModel";
    public static final String BIZ_CODE_DISPATCH_DRAIN_RULE = "dispatchDrainRule";
    public static final String BIZ_CODE_DISPATCH_WARNING_RULE = "dispatchWarningRule";
}
