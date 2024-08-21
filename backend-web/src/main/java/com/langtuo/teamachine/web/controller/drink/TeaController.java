package com.langtuo.teamachine.web.controller.drink;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.TeaDTO;
import com.langtuo.teamachine.api.request.drink.TeaPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.TeaMgtService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

@RestController
@RequestMapping("/drinkset/tea")
@Slf4j
public class TeaController {
    @Resource
    private TeaMgtService service;

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/{tenantcode}/{teacode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{teacode}/get")
    public LangTuoResult<TeaDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "teacode") String teaCode) {
        LangTuoResult<TeaDTO> rtn = service.getByCode(tenantCode, teaCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/list?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/list")
    public LangTuoResult<List<TeaDTO>> list(@RequestParam("tenantCode") String tenantCode) {
        LangTuoResult<List<TeaDTO>> rtn = service.list(tenantCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/topping/type/search?tenantCode={tenantCode}&teaCode={teaCode}&teaName={teaName}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<TeaDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("teaCode") String teaCode, @RequestParam("teaName") String teaName,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<TeaDTO>> rtn = service.search(tenantCode, teaCode, teaName,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody TeaPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/drinkset/tea/{tenantcode}/{teacode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{teacode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "teacode") String teaCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, teaCode);
        return rtn;
    }

    @GetMapping("/{tenantcode}/export")
    public ResponseEntity<byte[]> exportExcel(@PathVariable(name = "tenantcode") String tenantCode) {
        LangTuoResult<XSSFWorkbook> rtn = service.exportByExcel(tenantCode);
        XSSFWorkbook xssfWorkbook = getModel(rtn);

        // 导出Excel文件
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            xssfWorkbook.write(outputStream);
            xssfWorkbook.close();
        } catch (IOException e) {
            log.error("write output stream error: " + e.getMessage(), e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "export.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }
}
