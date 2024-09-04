package com.langtuo.teamachine.web.controller.record;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.SupplyActRecordDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.SupplyActRecordMgtService;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/recordset/supply")
public class SupplyActRecordController {
    @Resource
    private SupplyActRecordMgtService service;

    @GetMapping(value = "/get")
    public TeaMachineResult<SupplyActRecordDTO> get(@RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "idempotentMark") String idempotentMark) {
        TeaMachineResult<SupplyActRecordDTO> rtn = service.get(tenantCode, idempotentMark);
        return rtn;
    }

    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<SupplyActRecordDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopGroupCode") String shopGroupCode,
            @RequestParam("shopCode") String shopCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        List<String> shopGroupCodeList = Lists.newArrayList();
        if (!StringUtils.isBlank(shopGroupCode)) {
            shopGroupCodeList.add(shopGroupCode);
        }
        List<String> shopCodeList = Lists.newArrayList();
        if (!StringUtils.isBlank(shopCode)) {
            shopCodeList.add(shopCode);
        }
        TeaMachineResult<PageDTO<SupplyActRecordDTO>> rtn = service.search(tenantCode, shopGroupCodeList, shopCodeList,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/recordset/warning/{tenantcode}/{cleanrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/delete")
    public TeaMachineResult<Void> delete(@RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "idempotentMark") String idempotentMark) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, idempotentMark);
        return rtn;
    }
}
