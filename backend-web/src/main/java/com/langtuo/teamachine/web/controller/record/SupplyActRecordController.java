package com.langtuo.teamachine.web.controller.record;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.SupplyActRecordDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.SupplyActRecordMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
            @RequestParam("shopGroupCode") String shopGroupCode, @RequestParam("shopCode") String shopCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<SupplyActRecordDTO>> rtn = service.search(tenantCode, shopGroupCode, shopCode,
                pageNum, pageSize);
        return rtn;
    }

    @DeleteMapping(value = "/delete")
    public TeaMachineResult<Void> delete(@RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "idempotentMark") String idempotentMark) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, idempotentMark);
        return rtn;
    }
}
