package com.langtuo.teamachine.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.MachineModelDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.MachineModelMgtService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/machine/model")
public class MachineModelController {
    @Resource
    private MachineModelMgtService machineModelMgtService;

    /**
     * Access: http://localhost:8080/teamachine/machine/model/get
     * @return
     */
    @GetMapping(value = "/get")
    public LangTuoResult<MachineModelDTO> get(@RequestParam("modelCode") String modelCode) {
        LangTuoResult<MachineModelDTO> rtn = machineModelMgtService.get(modelCode);
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/machine/model/list
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<MachineModelDTO>> list() {
        LangTuoResult<List<MachineModelDTO>> rtn = machineModelMgtService.list();
        return rtn;
    }

    /**
     * Access: http://localhost:8080/teamachine/machine/get
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestParam("modelCode") String modelCode,
            @RequestParam("enableFlowAll") String enableFlowAll, @RequestParam("extraInfo") String extraInfo) {
        if (StringUtils.isBlank(modelCode) || !StringUtils.isNumeric(enableFlowAll)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> rtn = machineModelMgtService.put(buildMachineModelDTO(modelCode, enableFlowAll,
                extraInfo));
        return rtn;
    }

    private MachineModelDTO buildMachineModelDTO(String modelCode, String enableFlowAll, String extraInfo) {
        MachineModelDTO machineModelDTO = new MachineModelDTO();
        machineModelDTO.setModelCode(modelCode);
        machineModelDTO.setEnableFlowAll(Integer.valueOf(enableFlowAll));

        try {
            JSONObject extraInfoJSON = JSONObject.parseObject(extraInfo);
            Set<Map.Entry<String, Object>> entrySet = extraInfoJSON.entrySet();

            Map<String, String> extraInfoMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : entrySet) {
                String key = entry.getKey();
                String val = String.valueOf(entry.getValue());
                extraInfoMap.put(key, val);
            }
            machineModelDTO.setExtraInfo(extraInfoMap);
        } catch(Exception e) {
            // Do nothing
        }
        return machineModelDTO;
    }
}