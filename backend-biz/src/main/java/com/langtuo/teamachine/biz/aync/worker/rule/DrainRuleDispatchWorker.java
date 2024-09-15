package com.langtuo.teamachine.biz.aync.worker.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.model.rule.DrainRuleDTO;
import com.langtuo.teamachine.api.service.rule.DrainRuleMgtService;
import com.langtuo.teamachine.biz.util.SpringServiceUtils;
import com.langtuo.teamachine.dao.accessor.rule.DrainRuleDispatchAccessor;
import com.langtuo.teamachine.dao.po.rule.DrainRuleDispatchPO;
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
public class DrainRuleDispatchWorker implements Runnable {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 开业规则编码
     */
    private String drainRuleCode;

    public DrainRuleDispatchWorker(JSONObject jsonPayload) {
        this.tenantCode = jsonPayload.getString(CommonConsts.JSON_KEY_TENANT_CODE);
        this.drainRuleCode = jsonPayload.getString(CommonConsts.JSON_KEY_DRAIN_RULE_CODE);
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(drainRuleCode)) {
            throw new IllegalArgumentException("tenantCode is blank");
        }
    }

    @Override
    public void run() {
        JSONObject jsonDispatchCont = getDispatchCont();

        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_DISPATCH_DRAIN_RULE);
        jsonMsg.put(CommonConsts.JSON_KEY_OPEN_RULE, jsonDispatchCont);
        log.info("$$$$$ OpenRuleDispatchWorker jsonMsg: " + jsonMsg.toJSONString());

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
        DrainRuleMgtService drainRuleMgtService = SpringServiceUtils.getDrainRuleMgtService() ;
        DrainRuleDTO drainRuleDTO = getModel(drainRuleMgtService.getByCode(tenantCode, drainRuleCode));
        if (drainRuleDTO == null) {
            log.info("open rule error, stop worker");
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSON.toJSON(drainRuleDTO);
        return jsonObject;
    }

    private List<String> getMachineCodeList() {
        DrainRuleDispatchAccessor drainRuleDispatchAccessor = SpringAccessorUtils.getDrainRuleDispatchAccessor();
        List<DrainRuleDispatchPO> drainRuleDispatchPOList = drainRuleDispatchAccessor.selectListByDrainRuleCode(
                tenantCode, drainRuleCode);
        if (CollectionUtils.isEmpty(drainRuleDispatchPOList)) {
            log.info("drainRuleDispatchWorker|getDispatch|null|stopWorker");
            return null;
        }

        List<String> shopCodeList = DaoUtils.getShopCodeListByShopGroupCodeList(tenantCode,
                drainRuleDispatchPOList.stream()
                        .map(DrainRuleDispatchPO::getShopGroupCode)
                        .collect(Collectors.toList()));
        List<String> machineCodeList = DaoUtils.getMachineCodeListByShopCodeList(tenantCode, shopCodeList);
        return machineCodeList;
    }
}
