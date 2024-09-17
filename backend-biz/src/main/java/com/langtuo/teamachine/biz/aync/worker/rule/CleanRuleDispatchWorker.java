package com.langtuo.teamachine.biz.aync.worker.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.rule.CleanRuleDTO;
import com.langtuo.teamachine.api.service.rule.CleanRuleMgtService;
import com.langtuo.teamachine.biz.util.SpringServiceUtils;
import com.langtuo.teamachine.dao.accessor.rule.CleanRuleDispatchAccessor;
import com.langtuo.teamachine.dao.po.rule.CleanRuleDispatchPO;
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
public class CleanRuleDispatchWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 清洁规则编码
     */
    private String cleanRuleCode;

    public CleanRuleDispatchWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(CommonConsts.JSON_KEY_TENANT_CODE);
        this.cleanRuleCode = jsonPayload.getString(CommonConsts.JSON_KEY_CLEAN_RULE_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(cleanRuleCode)) {
            log.error("cleanRuleDispatchWorker|init|illegalArgument|" + tenantCode + "|" + cleanRuleCode);
            throw new IllegalArgumentException("tenantCode or cleanRuleCode is blank");
        }
    }

    @Override
    public void run() {
        JSONObject jsonDispatchCont = getDispatchCont();
        if (jsonDispatchCont == null) {
            log.error("cleanRuleDispatchWorker|getDispatchCont|error|stopWorker|" + jsonDispatchCont);
            return;
        }

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_CLEAN_RULE);
        jsonMsg.put(CommonConsts.JSON_KEY_CLEAN_RULE, jsonDispatchCont);

        // 准备发送
        List<String> machineCodeList = getMachineCodeList();
        if (CollectionUtils.isEmpty(machineCodeList)) {
            log.error("cleanRuleDispatchWorker|getMachineCodeList|empty|stopWorker|" + machineCodeList);
            return;
        }

        MqttProducer mqttProducer = SpringServiceUtils.getMqttProducer();
        machineCodeList.stream().forEach(machineCode -> {
            mqttProducer.sendP2PMsgByTenant(tenantCode, machineCode, jsonMsg.toJSONString());
        });
    }

    private JSONObject getDispatchCont() {
        CleanRuleMgtService cleanRuleMgtService = SpringServiceUtils.getCleanRuleMgtService();
        CleanRuleDTO dto = getModel(cleanRuleMgtService.getByCode(tenantCode, cleanRuleCode));
        if (dto == null) {
            log.error("cleanRuleDispatchWorker|getRule|error|stopWorker|" + dto);
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSON.toJSON(dto);
        return jsonObject;
    }

    private List<String> getMachineCodeList() {
        CleanRuleDispatchAccessor cleanRuleDispatchAccessor = SpringAccessorUtils.getCleanRuleDispatchAccessor();
        List<CleanRuleDispatchPO> cleanRuleDispatchPOList = cleanRuleDispatchAccessor.listByCleanRuleCode(
                tenantCode, cleanRuleCode);
        if (CollectionUtils.isEmpty(cleanRuleDispatchPOList)) {
            log.info("cleanRuleDispatchWorker|getDispatchPOList|error|stopWorker|" + cleanRuleDispatchPOList);
            return null;
        }

        List<String> shopCodeList = DaoUtils.getShopCodeListByShopGroupCodeList(tenantCode,
                cleanRuleDispatchPOList.stream()
                        .map(CleanRuleDispatchPO::getShopGroupCode)
                        .collect(Collectors.toList()));
        List<String> machineCodeList = DaoUtils.getMachineCodeListByShopCodeList(tenantCode, shopCodeList);
        return machineCodeList;
    }
}
