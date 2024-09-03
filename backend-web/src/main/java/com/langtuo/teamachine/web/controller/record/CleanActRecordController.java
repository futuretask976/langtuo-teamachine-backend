package com.langtuo.teamachine.web.controller.record;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.CleanActRecordDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.CleanActRecordMgtService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/recordset/clean")
public class CleanActRecordController {
    @Resource
    private CleanActRecordMgtService service;

    /**
     * url: http://{host}:{port}/teamachinebackend/recordset/clean/tenant_001/shopGroup_001/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{idempotentmark}/get")
    public TeaMachineResult<CleanActRecordDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "idempotentmark") String idempotentMark) {
        TeaMachineResult<CleanActRecordDTO> rtn = service.get(tenantCode, idempotentMark);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/recordset/clean/search?tenantCode=tenant_001&shopGroupName=&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public TeaMachineResult<PageDTO<CleanActRecordDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("shopGroupCodeList") List<String> shopGroupCodeList,
            @RequestParam("shopCodeList") List<String> shopCodeList,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<CleanActRecordDTO>> rtn = service.search(tenantCode, shopGroupCodeList, shopCodeList,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://{host}:{port}/teamachinebackend/recordset/clean/{tenantcode}/{cleanrulecode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{idempotentmark}/delete")
    public TeaMachineResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "idempotentmark") String idempotentMark) {
        TeaMachineResult<Void> rtn = service.delete(tenantCode, idempotentMark);
        return rtn;
    }
}
