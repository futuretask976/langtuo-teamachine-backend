package com.langtuo.teamachine.web.controller.record;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.OrderActRecordDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.record.OrderActRecordMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/recordset/order")
public class OrderActRecordController {
    @Resource
    private OrderActRecordMgtService service;

    /**
     * url: http://localhost:8080/teamachine/recordset/warning/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{idempotentmark}/get")
    public LangTuoResult<OrderActRecordDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "idempotentmark") String idempotentMark) {
        LangTuoResult<OrderActRecordDTO> rtn = service.get(tenantCode, idempotentMark);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/recordset/warning/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<OrderActRecordDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopGroupCode") String shopGroupCode, @RequestParam("shopCode") String shopCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<OrderActRecordDTO>> rtn = service.search(tenantCode, shopGroupCode, shopCode,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/recordset/warning/{tenantcode}/{cleanrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{idempotentmark}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "idempotentmark") String idempotentMark) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, idempotentMark);
        return rtn;
    }
}
