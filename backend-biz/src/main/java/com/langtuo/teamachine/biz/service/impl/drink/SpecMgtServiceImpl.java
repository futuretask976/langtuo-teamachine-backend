package com.langtuo.teamachine.biz.service.impl.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.SpecDTO;
import com.langtuo.teamachine.api.model.drink.SpecItemDTO;
import com.langtuo.teamachine.api.request.drink.SpecPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.SpecMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.drink.SpecAccessor;
import com.langtuo.teamachine.dao.accessor.drink.SpecItemAccessor;
import com.langtuo.teamachine.dao.accessor.drink.TeaUnitAccessor;
import com.langtuo.teamachine.dao.po.drink.SpecPO;
import com.langtuo.teamachine.dao.po.drink.SpecItemPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SpecMgtServiceImpl implements SpecMgtService {
    @Resource
    private SpecAccessor specAccessor;

    @Resource
    private SpecItemAccessor specItemAccessor;

    @Resource
    private TeaUnitAccessor teaUnitAccessor;

    @Override
    public TeaMachineResult<List<SpecDTO>> list(String tenantCode) {
        TeaMachineResult<List<SpecDTO>> teaMachineResult;
        try {
            List<SpecPO> list = specAccessor.selectList(tenantCode);
            List<SpecDTO> dtoList = convert(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<SpecDTO>> search(String tenantName, String specCode, String specName,
            int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<SpecDTO>> teaMachineResult;
        try {
            PageInfo<SpecPO> pageInfo = specAccessor.search(tenantName, specCode, specName,
                    pageNum, pageSize);
            List<SpecDTO> dtoList = convert(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<SpecDTO> getByCode(String tenantCode, String specCode) {
        TeaMachineResult<SpecDTO> teaMachineResult;
        try {
            SpecPO po = specAccessor.selectOneBySpecCode(tenantCode, specCode);
            SpecDTO dto = convert(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<SpecDTO> getByName(String tenantCode, String specName) {
        TeaMachineResult<SpecDTO> teaMachineResult;
        try {
            SpecPO po = specAccessor.selectOneBySpecName(tenantCode, specName);
            SpecDTO dto = convert(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(SpecPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        SpecPO specPO = convert(request);
        List<SpecItemPO> specItemPOList = convertToSpecItemPO(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            SpecPO exist = specAccessor.selectOneBySpecCode(specPO.getTenantCode(), specPO.getSpecCode());
            if (exist != null) {
                int updated = specAccessor.update(specPO);
            } else {
                int inserted = specAccessor.insert(specPO);
            }

            int deleted4SpecSub = specItemAccessor.deleteBySpecCode(specPO.getTenantCode(), specPO.getSpecCode());
            if (!CollectionUtils.isEmpty(specItemPOList)) {
                specItemPOList.stream().forEach(item -> {
                    specItemAccessor.insert(item);
                });
            }

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String specCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int countBySpecCode = teaUnitAccessor.countBySpecCode(tenantCode, specCode);
            if (countBySpecCode == BizConsts.DATABASE_SELECT_NONE) {
                int deleted4Spec = specAccessor.deleteBySpecCode(tenantCode, specCode);
                int deleted4SpecSub = specItemAccessor.deleteBySpecCode(tenantCode, specCode);
                teaMachineResult = TeaMachineResult.success();
            } else {
                teaMachineResult = TeaMachineResult.error(ErrorEnum.BIZ_ERR_CANNOT_DELETE_USING_SPEC);
            }
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    private List<SpecDTO> convert(List<SpecPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<SpecDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private SpecDTO convert(SpecPO po) {
        if (po == null) {
            return null;
        }

        SpecDTO dto = new SpecDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setSpecCode(po.getSpecCode());
        dto.setSpecName(po.getSpecName());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());

        List<SpecItemPO> poList = specItemAccessor.selectListBySpecCode(po.getTenantCode(), po.getSpecCode());
        if (!CollectionUtils.isEmpty(poList)) {
            dto.setSpecItemList(poList.stream().map(item -> convert(item)).collect(Collectors.toList()));
        }
        return dto;
    }

    private List<SpecItemDTO> convertToSpecItemDTO(List<SpecItemPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
    }

    private SpecItemDTO convert(SpecItemPO po) {
        if (po == null) {
            return null;
        }

        SpecItemDTO dto = new SpecItemDTO();
        dto.setSpecCode(po.getSpecCode());
        dto.setSpecItemCode(po.getSpecItemCode());
        dto.setSpecItemName(po.getSpecItemName());
        dto.setOuterSpecItemCode(po.getOuterSpecItemCode());
        return dto;
    }

    private SpecPO convert(SpecPutRequest request) {
        if (request == null) {
            return null;
        }

        SpecPO po = new SpecPO();
        po.setSpecCode(request.getSpecCode());
        po.setSpecName(request.getSpecName());
        po.setComment(request.getComment());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }

    private List<SpecItemPO> convertToSpecItemPO(SpecPutRequest specPutRequest) {
        if (specPutRequest == null || CollectionUtils.isEmpty(specPutRequest.getSpecItemList())) {
            return null;
        }

        List<SpecItemPO> specItemPOList = specPutRequest.getSpecItemList().stream()
                .map(item -> {
                    SpecItemPO po = new SpecItemPO();
                    po.setSpecCode(specPutRequest.getSpecCode());
                    po.setTenantCode(specPutRequest.getTenantCode());
                    po.setSpecCode(specPutRequest.getSpecCode());
                    po.setSpecItemCode(item.getSpecItemCode());
                    po.setSpecItemName(item.getSpecItemName());
                    po.setOuterSpecItemCode(item.getOuterSpecItemCode());
                    return po;
                }).collect(Collectors.toList());
        return specItemPOList;
    }
}
