package com.langtuo.teamachine.web.controller.device;

import com.langtuo.teamachine.api.model.device.DeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.device.DeployPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.device.DeployMgtService;
import com.langtuo.teamachine.web.constant.WebConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

@RestController
@RequestMapping("/deviceset/deploy")
@Slf4j
public class DeployController {
    @Resource
    private DeployMgtService service;

    /**
     * url: http://localhost:8080/teamachine/deploy/{tenantcode}/{deploycode}/get
     * @return
     */
    @GetMapping(value = "/{tenantcode}/{deploycode}/get")
    public LangTuoResult<DeployDTO> get(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "deploycode") String deployCode) {
        LangTuoResult<DeployDTO> rtn = service.getByDeployCode(tenantCode, deployCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/deploy/search?tenantCode={tenantCode}&deployCode={deployCode}&machineCode={machineCode}&shopName={shopName}&state={state}&pageNum=1&pageSize=10
     * @return
     */
    @GetMapping(value = "/search")
    public LangTuoResult<PageDTO<DeployDTO>> search(@RequestParam("tenantCode") String tenantCode,
            @RequestParam("deployCode") String deployCode, @RequestParam("machineCode") String machineCode,
            @RequestParam("shopName") String shopName, @RequestParam(required = false, name = "state") Integer state,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        LangTuoResult<PageDTO<DeployDTO>> rtn = service.search(tenantCode, deployCode, machineCode, shopName, state,
                pageNum, pageSize);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/deploy/put
     * @return
     */
    @PutMapping(value = "/put")
    public LangTuoResult<Void> put(@RequestBody DeployPutRequest request) {
        LangTuoResult<Void> rtn = service.put(request);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/deploy/{tenantcode}/{deploycode}/delete
     * @return
     */
    @DeleteMapping(value = "/{tenantcode}/{deploycode}/delete")
    public LangTuoResult<Void> delete(@PathVariable(name = "tenantcode") String tenantCode,
            @PathVariable(name = "deploycode") String deployCode) {
        LangTuoResult<Void> rtn = service.delete(tenantCode, deployCode);
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/deploy/deploycode/generate?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/deploycode/generate")
    public LangTuoResult<String> generateDeployCode(@RequestParam(name = "tenantCode") String tenantCode) {
        LangTuoResult<String> rtn = service.generateDeployCode();
        return rtn;
    }

    /**
     * url: http://localhost:8080/teamachine/deploy/machinecode/generate?tenantCode={tenantCode}
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/machinecode/generate")
    public LangTuoResult<String> generateMachineCode(@RequestParam(name = "tenantCode") String tenantCode) {
        LangTuoResult<String> rtn = service.generateMachineCode();
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
        headers.setContentDispositionFormData(WebConsts.HTTP_HEADER_DISPOSITION_NAME,
                WebConsts.HTTP_HEADER_DISPOSITION_FILE_NAME_4_DEPLOY_EXPORT);

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }
}
