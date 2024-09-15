package com.langtuo.teamachine.biz.aync.worker.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.rule.WarningRuleDTO;
import com.langtuo.teamachine.api.service.rule.WarningRuleMgtService;
import com.langtuo.teamachine.biz.util.SpringServiceUtils;
import com.langtuo.teamachine.dao.accessor.rule.WarningRuleDispatchAccessor;
import com.langtuo.teamachine.dao.po.rule.WarningRuleDispatchPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.dao.util.SpringAccessorUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.mqtt.produce.MqttProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getModel;

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
            throw new IllegalArgumentException("tenantCode or warningRuleCode is blank");
        }
    }

    @Override
    public void run() {
        JSONObject jsonDispatchCont = getDispatchCont();

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_WARNING_RULE);
        jsonMsg.put(CommonConsts.JSON_KEY_WARNING_RULE, jsonDispatchCont);
        log.info("$$$$$ WarningRuleDispatchWorker sendMsg: " + jsonMsg.toJSONString());

        // 准备发送
        List<String> machineCodeList = getMachineCodeList();
        if (CollectionUtils.isEmpty(machineCodeList)) {
            log.info("machine code list is empty, stop worker");
        }

        MqttProducer mqttProducer = SpringServiceUtils.getMqttProducer();
        machineCodeList.stream().forEach(machineCode -> {
            mqttProducer.sendP2PMsgByTenant(tenantCode, machineCode, jsonMsg.toJSONString());
        });
    }

    private JSONObject getDispatchCont() {
        WarningRuleMgtService warningRuleMgtService = SpringServiceUtils.getWarningRuleMgtService();
        WarningRuleDTO warningRuleDTO = getModel(warningRuleMgtService.getByCode(tenantCode, warningRuleCode));
        if (warningRuleDTO == null) {
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSON.toJSON(warningRuleDTO);
        return jsonObject;
    }

    private List<String> getMachineCodeList() {
        WarningRuleDispatchAccessor warningRuleDispatchAccessor = SpringAccessorUtils.getWarningRuleDispatchAccessor();
        List<WarningRuleDispatchPO> warningRuleDispatchPOList = warningRuleDispatchAccessor.selectListByWarningRuleCode(
                tenantCode, warningRuleCode);
        if (CollectionUtils.isEmpty(warningRuleDispatchPOList)) {
            log.info("warningRuleDispatchWorker|getDispatch|null|stopWorker");
            return null;
        }

        List<String> shopCodeList = DaoUtils.getShopCodeListByShopGroupCodeList(tenantCode,
                warningRuleDispatchPOList.stream()
                        .map(WarningRuleDispatchPO::getShopGroupCode)
                        .collect(Collectors.toList()));
        List<String> machineCodeList = DaoUtils.getMachineCodeListByShopCodeList(tenantCode, shopCodeList);
        return machineCodeList;
    }
}
