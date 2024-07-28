package com.langtuo.teamachine.biz.service.impl.menuset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menuset.SeriesDTO;
import com.langtuo.teamachine.api.model.menuset.SeriesTeaRelDTO;
import com.langtuo.teamachine.api.request.menuset.SeriesPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.menuset.SeriesMgtService;
import com.langtuo.teamachine.dao.accessor.menuset.SeriesAccessor;
import com.langtuo.teamachine.dao.accessor.menuset.SeriesTeaRelAccessor;
import com.langtuo.teamachine.dao.po.menuset.SeriesPO;
import com.langtuo.teamachine.dao.po.menuset.SeriesTeaRelPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SeriesMgtServiceImpl implements SeriesMgtService {
    @Resource
    private SeriesAccessor seriesAccessor;

    @Resource
    private SeriesTeaRelAccessor seriesTeaRelAccessor;

    @Override
    public LangTuoResult<List<SeriesDTO>> list(String tenantCode) {
        LangTuoResult<List<SeriesDTO>> langTuoResult = null;
        try {
            List<SeriesPO> list = seriesAccessor.selectList(tenantCode);
            List<SeriesDTO> dtoList = convertToSeriesDTO(list);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<SeriesDTO>> search(String tenantName, String seriesCode, String seriesName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<SeriesDTO>> langTuoResult = null;
        try {
            PageInfo<SeriesPO> pageInfo = seriesAccessor.search(tenantName, seriesCode, seriesName,
                    pageNum, pageSize);
            List<SeriesDTO> dtoList = convertToSeriesDTO(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<SeriesDTO> getByCode(String tenantCode, String seriesCode) {
        LangTuoResult<SeriesDTO> langTuoResult = null;
        try {
            SeriesPO toppingTypePO = seriesAccessor.selectOneByCode(tenantCode, seriesCode);
            SeriesDTO seriesDTO = convert(toppingTypePO);
            langTuoResult = LangTuoResult.success(seriesDTO);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<SeriesDTO> getByName(String tenantCode, String seriesName) {
        LangTuoResult<SeriesDTO> langTuoResult = null;
        try {
            SeriesPO toppingTypePO = seriesAccessor.selectOneByName(tenantCode, seriesName);
            SeriesDTO tenantDTO = convert(toppingTypePO);
            langTuoResult = LangTuoResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(SeriesPutRequest request) {
        if (request == null
                || StringUtils.isBlank(request.getTenantCode())) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        SeriesPO seriesPO = convertSeriesPO(request);
        List<SeriesTeaRelPO> seriesTeaRelPOList = convertToSeriesTeaRelPO(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            SeriesPO exist = seriesAccessor.selectOneByCode(seriesPO.getTenantCode(),
                    seriesPO.getSeriesCode());
            if (exist != null) {
                int updated = seriesAccessor.update(seriesPO);
            } else {
                int inserted = seriesAccessor.insert(seriesPO);
            }

            int deleted4SeriesTeaRel = seriesTeaRelAccessor.delete(seriesPO.getTenantCode(), seriesPO.getSeriesCode());
            if (!CollectionUtils.isEmpty(seriesTeaRelPOList)) {
                seriesTeaRelPOList.forEach(seriesTeaRelPO -> {
                    seriesTeaRelAccessor.insert(seriesTeaRelPO);
                });
            }

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String seriesCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted4Series = seriesAccessor.delete(tenantCode, seriesCode);
            int deleted4SeriesTeaRel = seriesTeaRelAccessor.delete(tenantCode, seriesCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private List<SeriesDTO> convertToSeriesDTO(List<SeriesPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<SeriesDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private SeriesDTO convert(SeriesPO po) {
        if (po == null) {
            return null;
        }

        SeriesDTO dto = new SeriesDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setSeriesCode(po.getSeriesCode());
        dto.setSeriesName(po.getSeriesName());
        dto.setImgLink(po.getImgLink());

        List<SeriesTeaRelPO> seriesTeaRelPOList = seriesTeaRelAccessor.selectList(
                po.getTenantCode(), po.getSeriesCode());
        dto.setSeriesTeaRelList(convert(seriesTeaRelPOList));
        return dto;
    }

    private SeriesPO convertSeriesPO(SeriesPutRequest request) {
        if (request == null) {
            return null;
        }

        SeriesPO po = new SeriesPO();
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(request.getExtraInfo());
        po.setSeriesCode(request.getSeriesCode());
        po.setSeriesName(request.getSeriesName());
        po.setImgLink(request.getImgLink());
        return po;
    }

    private List<SeriesTeaRelDTO> convert(List<SeriesTeaRelPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream().map(po -> {
            SeriesTeaRelDTO dto = new SeriesTeaRelDTO();
            dto.setId(po.getId());
            dto.setGmtCreated(po.getGmtCreated());
            dto.setGmtModified(po.getGmtModified());
            dto.setSeriesCode(po.getSeriesCode());
            dto.setTeaCode(po.getTeaCode());
            return dto;
        }).collect(Collectors.toList());
    }

    private List<SeriesTeaRelPO> convertToSeriesTeaRelPO(SeriesPutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getSeriesTeaRelList())) {
            return null;
        }

        return request.getSeriesTeaRelList().stream()
                .map(seriesTeaRelPutRequest -> {
                    SeriesTeaRelPO po = new SeriesTeaRelPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setSeriesCode(seriesTeaRelPutRequest.getSeriesCode());
                    po.setTeaCode(seriesTeaRelPutRequest.getTeaCode());
                    return po;
                }).collect(Collectors.toList());
    }
}
