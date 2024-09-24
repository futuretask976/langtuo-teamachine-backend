package com.langtuo.teamachine.api.request.drink;

import com.google.common.collect.Lists;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TeaPutRequest {
    /**
     * 茶编码
     */
    private String teaCode;

    /**
     * 茶名称
     */
    private String teaName;

    /**
     * 外部茶编码
     */
    private String outerTeaCode;

    /**
     * 状态，0：禁用，1：启用
     */
    private int state;

    /**
     * 茶类型编码
     */
    private String teaTypeCode;

    /**
     * 茶品图片链接
     */
    private String imgLink;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 备注
     */
    private String comment;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 茶-物料关系列表
     */
    private List<TeaUnitPutRequest> teaUnitList;

    /**
     * 物料基础规则
     */
    private List<ActStepPutRequest> actStepList;

    /**
     * 是否新建
     */
    private boolean putNew;

    public void addTeaUnit(TeaUnitPutRequest request) {
        if (request == null) {
            return;
        }
        if (this.teaUnitList == null) {
            this.teaUnitList = Lists.newArrayList();
        }
        this.teaUnitList.add(request);
    }

    public void addToppingBaseRule(int stepIndex, ToppingBaseRulePutRequest request) {
        if (request == null) {
            return;
        }
        if (this.actStepList == null) {
            this.actStepList = Lists.newArrayList();
        }
        boolean added = false;
        for (ActStepPutRequest actStepPutRequest : this.actStepList) {
            if (stepIndex == actStepPutRequest.getStepIndex()) {
                actStepPutRequest.getToppingBaseRuleList().add(request);
                added = true;
                break;
            }
        }
        if (!added) {
            ActStepPutRequest actStepPutRequest = new ActStepPutRequest();
            actStepPutRequest.setStepIndex(stepIndex);
            actStepPutRequest.setToppingBaseRuleList(Lists.newArrayList(request));
            this.actStepList.add(actStepPutRequest);
        }
    }

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (!RegexUtils.isValidCode(tenantCode, true)) {
            System.out.println("$$$$$ tenantCode invalid: " + tenantCode);
            return false;
        }
        if (!RegexUtils.isValidComment(comment, false)) {
            System.out.println("$$$$$ comment invalid: " + comment);
            return false;
        }
        if (!RegexUtils.isValidCode(teaCode, true)) {
            System.out.println("$$$$$ teaCode invalid: " + teaCode);
            return false;
        }
        if (!RegexUtils.isValidName(teaName, true)) {
            System.out.println("$$$$$ teaCode invalid: " + teaCode);
            return false;
        }
        if (!RegexUtils.isValidCode(outerTeaCode, true)) {
            System.out.println("$$$$$ outerTeaCode invalid: " + outerTeaCode);
            return false;
        }
        if (!RegexUtils.isValidCode(teaTypeCode, true)) {
            System.out.println("$$$$$ teaTypeCode invalid: " + teaTypeCode);
            return false;
        }
        if (!isValidTeaUnitList()) {
            return false;
        }
        if (!isValidActStepList()) {
            return false;
        }
        return true;
    }

    private boolean isValidTeaUnitList() {
        if (CollectionUtils.isEmpty(teaUnitList)) {
            return false;
        }
        for (TeaUnitPutRequest s : teaUnitList) {
            if (!s.isValid()) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidActStepList() {
        if (CollectionUtils.isEmpty(actStepList)) {
            return false;
        }
        for (ActStepPutRequest s : actStepList) {
            if (!s.isValid()) {
                return false;
            }
        }
        return true;
    }
}
