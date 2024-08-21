package com.langtuo.teamachine.web.controller.record;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.InvalidActRecordDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.InvalidActRecordMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/recordset/invalid")
public class InvalidActRecordController {
    @Resource
    private InvalidActRecordMgtService service;

    /**
     * url: http://localhost:8080/teamachine/recordset/warning/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{idempotentmark}/get")
    public TeaMachineResult<InvalidActRecordDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "idempotentmark") String idempotentMark) {
        TeaMachineResult<InvalidActRecordDTO> rtn = service.get(tenantCode, idempotentMark);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/recordset/warning/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<InvalidActRecordDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopGroupCodeList") List<String> shopGroupCodeList,
            @RequestParam("shopCodeList") List<String> shopCodeList,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<InvalidActRecordDTO>> rtn = service.search(tenantCode, shopGroupCodeList, shopCodeList,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/recordset/warning/{tenantcode}/{cleanrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{idempotentmark}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "idempotentmark") String idempotentMark) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, idempotentMark);
        return rtn;
    }
}
