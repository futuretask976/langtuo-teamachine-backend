package com.langtuo.teamachine.biz.service.menu;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.menu.SeriesDTO;
import com.langtuo.teamachine.api.request.menu.SeriesPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.menu.SeriesMgtService;
import com.langtuo.teamachine.dao.accessor.menu.MenuSeriesRelAccessor;
import com.langtuo.teamachine.dao.accessor.menu.SeriesAccessor;
import com.langtuo.teamachine.dao.accessor.menu.SeriesTeaRelAccessor;
import com.langtuo.teamachine.dao.po.menu.SeriesPO;
import com.langtuo.teamachine.dao.po.menu.SeriesTeaRelPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.langtuo.teamachine.biz.convert.menu.SeriesMgtConvertor.*;

@Component
@Slf4j
public class SeriesMgtServiceImpl implements SeriesMgtService {
    @Resource
    private SeriesAccessor seriesAccessor;

    @Resource
    private SeriesTeaRelAccessor seriesTeaRelAccessor;

    @Resource
    private MenuSeriesRelAccessor menuSeriesRelAccessor;

    @Override
    public TeaMachineResult<List<SeriesDTO>> list(String tenantCode) {
        TeaMachineResult<List<SeriesDTO>> teaMachineResult;
        try {
            List<SeriesPO> list = seriesAccessor.list(tenantCode);
            List<SeriesDTO> dtoList = convertToSeriesDTO(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("seriesMgtService|list|fatal|" + e.getMessage(), e);
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
            log.error("seriesMgtService|search|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<SeriesDTO> getByCode(String tenantCode, String seriesCode) {
        TeaMachineResult<SeriesDTO> teaMachineResult;
        try {
            SeriesPO toppingTypePO = seriesAccessor.getBySeriesCode(tenantCode, seriesCode);
            SeriesDTO seriesDTO = convertToSeriesDTO(toppingTypePO);
            teaMachineResult = TeaMachineResult.success(seriesDTO);
        } catch (Exception e) {
            log.error("seriesMgtService|getByCode|fatal|" + e.getMessage(), e);
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
            SeriesPO exist = seriesAccessor.getBySeriesCode(seriesPO.getTenantCode(),
                    seriesPO.getSeriesCode());
            if (exist != null) {
                int updated = seriesAccessor.update(seriesPO);
            } else {
                int inserted = seriesAccessor.insert(seriesPO);
            }

            int deleted4SeriesTeaRel = seriesTeaRelAccessor.deleteBySeriesCode(seriesPO.getTenantCode(), seriesPO.getSeriesCode());
            if (!CollectionUtils.isEmpty(seriesTeaRelPOList)) {
                for (SeriesTeaRelPO seriesTeaRelPO : seriesTeaRelPOList) {
                    seriesTeaRelAccessor.insert(seriesTeaRelPO);
                }
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
            SeriesPO exist = seriesAccessor.getBySeriesCode(po.getTenantCode(), po.getSeriesCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = seriesAccessor.insert(po);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("seriesMgtService|putNewSeries|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted4TeaRel = seriesTeaRelAccessor.deleteBySeriesCode(po.getTenantCode(), po.getSeriesCode());
            for (SeriesTeaRelPO teaRelPO : teaRelPOList) {
                int inserted4TeaRel = seriesTeaRelAccessor.insert(teaRelPO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4TeaRel) {
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
            SeriesPO exist = seriesAccessor.getBySeriesCode(po.getTenantCode(), po.getSeriesCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = seriesAccessor.update(po);
            if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
                log.error("seriesMgtService|putUpdateSeries|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }

            int deleted4TeaRel = seriesTeaRelAccessor.deleteBySeriesCode(po.getTenantCode(), po.getSeriesCode());
            for (SeriesTeaRelPO teaRelPO : teaRelPOList) {
                int inserted4TeaRel = seriesTeaRelAccessor.insert(teaRelPO);
                if (CommonConsts.DB_INSERTED_ONE_ROW != inserted4TeaRel) {
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
            if (count == CommonConsts.DB_SELECT_ZERO_ROW) {
                int deleted4Series = seriesAccessor.deleteBySeriesCode(tenantCode, seriesCode);
                int deleted4SeriesTeaRel = seriesTeaRelAccessor.deleteBySeriesCode(tenantCode, seriesCode);
                teaMachineResult = TeaMachineResult.success();
            } else {
                teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }
        } catch (Exception e) {
            log.error("seriesMgtService|delete|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }
}
