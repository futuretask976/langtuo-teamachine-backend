package com.langtuo.teamachine.biz.service.impl.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingTypeDTO;
import com.langtuo.teamachine.api.request.drink.ToppingTypePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import com.langtuo.teamachine.api.service.drink.ToppingTypeMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.drink.ToppingTypeAccessor;
import com.langtuo.teamachine.dao.po.drink.ToppingTypePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getModel;

@Component
@Slf4j
public class ToppingTypeMgtServiceImpl implements ToppingTypeMgtService {
    @Resource
    private ToppingTypeAccessor accessor;

    @Resource
    private ToppingAccessor toppingAccessor;

    @Override
    public TeaMachineResult<List<ToppingTypeDTO>> list(String tenantCode) {
        TeaMachineResult<List<ToppingTypeDTO>> teaMachineResult;
        try {
            List<ToppingTypePO> list = accessor.selectList(tenantCode);
            List<ToppingTypeDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<ToppingTypeDTO>> search(String tenantName, String toppingTypeCode,
            String toppingTypeName, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<ToppingTypeDTO>> teaMachineResult;
        try {
            PageInfo<ToppingTypePO> pageInfo = accessor.search(tenantName, toppingTypeCode, toppingTypeName,
                    pageNum, pageSize);
            List<ToppingTypeDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<ToppingTypeDTO> getByCode(String tenantCode, String toppingTypeCode) {
        TeaMachineResult<ToppingTypeDTO> teaMachineResult;
        try {
            ToppingTypePO toppingTypePO = accessor.selectOneByToppingTypeCode(tenantCode, toppingTypeCode);
            ToppingTypeDTO tenantDTO = convert(toppingTypePO);

            teaMachineResult = TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<ToppingTypeDTO> getByName(String tenantCode, String toppingTypeName) {
        TeaMachineResult<ToppingTypeDTO> teaMachineResult;
        try {
            ToppingTypePO toppingTypePO = accessor.selectOneByToppingTypeName(tenantCode, toppingTypeName);
            ToppingTypeDTO tenantDTO = convert(toppingTypePO);

            teaMachineResult = TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(ToppingTypePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        ToppingTypePO toppingTypePO = convert(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            ToppingTypePO exist = accessor.selectOneByToppingTypeCode(toppingTypePO.getTenantCode(),
                    toppingTypePO.getToppingTypeCode());
            if (exist != null) {
                int updated = accessor.update(toppingTypePO);
            } else {
                int inserted = accessor.insert(toppingTypePO);
            }
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String toppingTypeCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = accessor.delete(tenantCode, toppingTypeCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    private ToppingTypeDTO convert(ToppingTypePO po) {
        if (po == null) {
            return null;
        }

        ToppingTypeDTO dto = new ToppingTypeDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setToppingTypeCode(po.getToppingTypeCode());
        dto.setToppingTypeName(po.getToppingTypeName());
        dto.setComment(po.getComment());
        po.setExtraInfo(po.getExtraInfo());

        int toppingCount = toppingAccessor.countByToppingTypeCode(po.getTenantCode(), po.getToppingTypeCode());
        dto.setToppingCount(toppingCount);

        return dto;
    }

    private ToppingTypePO convert(ToppingTypePutRequest request) {
        if (request == null) {
            return null;
        }

        ToppingTypePO po = new ToppingTypePO();
        po.setToppingTypeCode(request.getToppingTypeCode());
        po.setToppingTypeName(request.getToppingTypeName());
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }
}
