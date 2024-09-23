package com.langtuo.teamachine.biz.aync.worker.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.rule.WarningRuleDTO;
import com.langtuo.teamachine.biz.convert.rule.WarningRuleMgtConvertor;
import com.langtuo.teamachine.biz.util.SpringServiceUtils;
import com.langtuo.teamachine.dao.accessor.rule.WarningRuleAccessor;
import com.langtuo.teamachine.dao.accessor.rule.WarningRuleDispatchAccessor;
import com.langtuo.teamachine.dao.po.rule.WarningRuleDispatchPO;
import com.langtuo.teamachine.dao.po.rule.WarningRulePO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class WarningRuleDispatchWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 预警规则编码
     */
    private String warningRuleCode;

    public WarningRuleDispatchWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(CommonConsts.JSON_KEY_TENANT_CODE);
        this.warningRuleCode = jsonPayload.getString(CommonConsts.JSON_KEY_WARNING_RULE_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(warningRuleCode)) {
            log.error("drainRuleDispatchWorker|init|illegalArgument|" + tenantCode + "|" + warningRuleCode);
            throw new IllegalArgumentException("tenantCode or warningRuleCode is blank");
        }
    }

    @Override
    public void run() {
        JSONObject jsonDispatchCont = getDispatchCont();

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_WARNING_RULE);
        jsonMsg.put(CommonConsts.JSON_KEY_WARNING_RULE, jsonDispatchCont);

        // 准备发送
        List<String> machineCodeList = getMachineCodeList();
        if (CollectionUtils.isEmpty(machineCodeList)) {
            log.error("warningRuleDispatchWorker|getMachineCodeList|empty|stopWorker|" + machineCodeList);
            return;
        }

        MqttProducer mqttProducer = SpringServiceUtils.getMqttProducer();
        for (String machineCode : machineCodeList) {
            mqttProducer.sendP2PMsgByTenant(tenantCode, machineCode, jsonMsg.toJSONString());
        }
    }

    private JSONObject getDispatchCont() {
        WarningRuleAccessor warningRuleAccessor = SpringAccessorUtils.getWarningRuleAccessor();
        WarningRulePO po = warningRuleAccessor.getByWarningRuleCode(tenantCode, warningRuleCode);
        WarningRuleDTO dto = WarningRuleMgtConvertor.convertToWarningRuleDTO(po);
        if (dto == null) {
            log.error("warningRuleDispatchWorker|getRule|error|stopWorker|" + dto);
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSON.toJSON(dto);
        return jsonObject;
    }

    private List<String> getMachineCodeList() {
        WarningRuleDispatchAccessor warningRuleDispatchAccessor = SpringAccessorUtils.getWarningRuleDispatchAccessor();
        List<WarningRuleDispatchPO> warningRuleDispatchPOList = warningRuleDispatchAccessor.listByWarningRuleCode(
                tenantCode, warningRuleCode);
        if (CollectionUtils.isEmpty(warningRuleDispatchPOList)) {
            log.error("warningRuleDispatchWorker|getDispatchPOList|error|stopWorker|" + warningRuleDispatchPOList);
            return null;
        }

        List<String> shopCodeList = DaoUtils.getShopCodeListByShopGroupCode(tenantCode,
                warningRuleDispatchPOList.stream()
                        .map(WarningRuleDispatchPO::getShopGroupCode)
                        .collect(Collectors.toList()));
        List<String> machineCodeList = DaoUtils.getMachineCodeListByShopCodeList(tenantCode, shopCodeList);
        return machineCodeList;
    }
}
