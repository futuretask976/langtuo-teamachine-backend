package com.langtuo.teamachine.biz.service.menu;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menu.SeriesDTO;
import com.langtuo.teamachine.api.model.menu.SeriesTeaRelDTO;
import com.langtuo.teamachine.api.request.menu.SeriesPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.menu.SeriesMgtService;
import com.langtuo.teamachine.dao.accessor.menu.MenuSeriesRelAccessor;
import com.langtuo.teamachine.dao.accessor.menu.SeriesAccessor;
import com.langtuo.teamachine.dao.accessor.menu.SeriesTeaRelAccessor;
import com.langtuo.teamachine.dao.po.menu.MenuPO;
import com.langtuo.teamachine.dao.po.menu.MenuSeriesRelPO;
import com.langtuo.teamachine.dao.po.menu.SeriesPO;
import com.langtuo.teamachine.dao.po.menu.SeriesTeaRelPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

    @Resource
    private MenuSeriesRelAccessor menuSeriesRelAccessor;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<List<SeriesDTO>> list(String tenantCode) {
        TeaMachineResult<List<SeriesDTO>> teaMachineResult;
        try {
            List<SeriesPO> list = seriesAccessor.selectList(tenantCode);
            List<SeriesDTO> dtoList = convertToSeriesDTO(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<SeriesDTO>> search(String tenantName, String seriesCode, String seriesName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<SeriesDTO>> teaMachineResult;
        try {
            PageInfo<SeriesPO> pageInfo = seriesAccessor.search(tenantName, seriesCode, seriesName,
                    pageNum, pageSize);
            List<SeriesDTO> dtoList = convertToSeriesDTO(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<SeriesDTO> getByCode(String tenantCode, String seriesCode) {
        TeaMachineResult<SeriesDTO> teaMachineResult;
        try {
            SeriesPO toppingTypePO = seriesAccessor.selectOneBySeriesCode(tenantCode, seriesCode);
            SeriesDTO seriesDTO = convert(toppingTypePO);
            teaMachineResult = TeaMachineResult.success(seriesDTO);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<SeriesDTO> getByName(String tenantCode, String seriesName) {
        TeaMachineResult<SeriesDTO> teaMachineResult;
        try {
            SeriesPO toppingTypePO = seriesAccessor.selectOneBySeriesName(tenantCode, seriesName);
            SeriesDTO tenantDTO = convert(toppingTypePO);
            teaMachineResult = TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(SeriesPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        SeriesPO seriesPO = convertSeriesPO(request);
        List<SeriesTeaRelPO> seriesTeaRelPOList = convertToSeriesTeaRelPO(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            SeriesPO exist = seriesAccessor.selectOneBySeriesCode(seriesPO.getTenantCode(),
                    seriesPO.getSeriesCode());
            if (exist != null) {
                int updated = seriesAccessor.update(seriesPO);
            } else {
                int inserted = seriesAccessor.insert(seriesPO);
            }

            int deleted4SeriesTeaRel = seriesTeaRelAccessor.deleteBySeriesCode(seriesPO.getTenantCode(), seriesPO.getSeriesCode());
            if (!CollectionUtils.isEmpty(seriesTeaRelPOList)) {
                seriesTeaRelPOList.forEach(seriesTeaRelPO -> {
                    seriesTeaRelAccessor.insert(seriesTeaRelPO);
                });
            }

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    private TeaMachineResult<Void> putNew(SeriesPO po, List<SeriesTeaRelPO> teaRelPOList) {
        try {
            SeriesPO exist = seriesAccessor.selectOneBySeriesCode(po.getTenantCode(), po.getSeriesCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = seriesAccessor.insert(po);
            if (inserted != CommonConsts.NUM_ONE) {
                log.error("seriesMgtService|putNewSeries|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted4TeaRel = seriesTeaRelAccessor.deleteBySeriesCode(po.getTenantCode(), po.getSeriesCode());
            for (SeriesTeaRelPO teaRelPO : teaRelPOList) {
                int inserted4TeaRel = seriesTeaRelAccessor.insert(teaRelPO);
                if (inserted4TeaRel != CommonConsts.NUM_ONE) {
                    log.error("seriesMgtService|putNewTeaRel|error|" + inserted4TeaRel);
                    return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("seriesMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(SeriesPO po, List<SeriesTeaRelPO> teaRelPOList) {
        try {
            SeriesPO exist = seriesAccessor.selectOneBySeriesCode(po.getTenantCode(), po.getSeriesCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = seriesAccessor.update(po);
            if (updated != CommonConsts.NUM_ONE) {
                log.error("seriesMgtService|putUpdateSeries|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }

            int deleted4TeaRel = seriesTeaRelAccessor.deleteBySeriesCode(po.getTenantCode(), po.getSeriesCode());
            for (SeriesTeaRelPO teaRelPO : teaRelPOList) {
                int inserted4TeaRel = seriesTeaRelAccessor.insert(teaRelPO);
                if (inserted4TeaRel != CommonConsts.NUM_ONE) {
                    log.error("seriesMgtService|putUpdateTeaRel|error|" + inserted4TeaRel);
                    return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("seriesMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String seriesCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int count = menuSeriesRelAccessor.countBySeriesCode(tenantCode, seriesCode);
            if (count == CommonConsts.DB_SELECT_RESULT_EMPTY) {
                int deleted4Series = seriesAccessor.deleteBySeriesCode(tenantCode, seriesCode);
                int deleted4SeriesTeaRel = seriesTeaRelAccessor.deleteBySeriesCode(tenantCode, seriesCode);
                teaMachineResult = TeaMachineResult.success();
            } else {
                teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
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
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setSeriesCode(po.getSeriesCode());
        dto.setSeriesName(po.getSeriesName());

        List<SeriesTeaRelPO> seriesTeaRelPOList = seriesTeaRelAccessor.selectListBySeriesCode(
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
        return po;
    }

    private List<SeriesTeaRelDTO> convert(List<SeriesTeaRelPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream().map(po -> {
            SeriesTeaRelDTO dto = new SeriesTeaRelDTO();
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