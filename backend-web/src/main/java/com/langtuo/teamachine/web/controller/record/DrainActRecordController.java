package com.langtuo.teamachine.web.controller.record;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.DrainActRecordDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.record.DrainActRecordMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/recordset/drain")
public class DrainActRecordController {
    @Resource
    private DrainActRecordMgtService service;

    /**
     * url: http://localhost:8080/teamachine/recordset/drain/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{idempotentmark}/get")
    public LangTuoResult<DrainActRecordDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "idempotentmark") String idempotentMark) {
        LangTuoResult<DrainActRecordDTO> rtn = service.get(tenantCode, idempotentMark);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/recordset/drain/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<DrainActRecordDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopGroupCodeList") List<String> shopGroupCodeList,
            @RequestParam("shopCodeList") List<String> shopCodeList,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<DrainActRecordDTO>> rtn = service.search(tenantCode, shopGroupCodeList, shopCodeList,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/recordset/drain/{tenantcode}/{cleanrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{idempotentmark}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "idempotentmark") String idempotentMark) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, idempotentMark);
        return rtn;
    }
}