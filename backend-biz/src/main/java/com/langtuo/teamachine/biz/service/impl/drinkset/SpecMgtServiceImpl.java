package com.langtuo.teamachine.biz.service.impl.drinkset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drinkset.SpecDTO;
import com.langtuo.teamachine.api.model.drinkset.SpecItemDTO;
import com.langtuo.teamachine.api.request.drinkset.SpecPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drinkset.SpecMgtService;
import com.langtuo.teamachine.dao.accessor.drinkset.SpecAccessor;
import com.langtuo.teamachine.dao.accessor.drinkset.SpecItemAccessor;
import com.langtuo.teamachine.dao.po.drinkset.SpecPO;
import com.langtuo.teamachine.dao.po.drinkset.SpecItemPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpecMgtServiceImpl implements SpecMgtService {
    @Resource
    private SpecAccessor specAccessor;

    @Resource
    private SpecItemAccessor specItemAccessor;

    @Override
    public LangTuoResult<List<SpecDTO>> list(String tenantCode) {
        LangTuoResult<List<SpecDTO>> langTuoResult = null;
        try {
            List<SpecPO> list = specAccessor.selectList(tenantCode);
            List<SpecDTO> dtoList = convert(list);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<SpecDTO>> search(String tenantName, String specCode, String specName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<SpecDTO>> langTuoResult = null;
        try {
            PageInfo<SpecPO> pageInfo = specAccessor.search(tenantName, specCode, specName,
                    pageNum, pageSize);
            List<SpecDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<SpecDTO> getByCode(String tenantCode, String specCode) {
        LangTuoResult<SpecDTO> langTuoResult = null;
        try {
            SpecPO po = specAccessor.selectOneByCode(tenantCode, specCode);
            SpecDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<SpecDTO> getByName(String tenantCode, String specName) {
        LangTuoResult<SpecDTO> langTuoResult = null;
        try {
            SpecPO po = specAccessor.selectOneByName(tenantCode, specName);
            SpecDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(SpecPutRequest specPutRequest) {
        if (specPutRequest == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        SpecPO specPO = convert(specPutRequest);
        List<SpecItemPO> specItemPOList = convertToSpecItemPO(specPutRequest);

        LangTuoResult<Void> langTuoResult = null;
        try {
            SpecPO exist = specAccessor.selectOneByCode(specPO.getTenantCode(), specPO.getSpecCode());
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

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String specCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted4Spec = specAccessor.delete(tenantCode, specCode);
            int deleted4SpecSub = specItemAccessor.deleteBySpecCode(tenantCode, specCode);

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
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
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setSpecCode(po.getSpecCode());
        dto.setSpecName(po.getSpecName());
        dto.setState(po.getState());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());

        List<SpecItemPO> poList = specItemAccessor.selectList(po.getTenantCode(), po.getSpecCode());
        if (!CollectionUtils.isEmpty(poList)) {
            dto.setSpecItemList(poList.stream().map(item -> convert(item)).collect(Collectors.toList()));
        }
        return dto;
    }

    private SpecItemDTO convert(SpecItemPO po) {
        if (po == null) {
            return null;
        }

        SpecItemDTO dto = new SpecItemDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
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
        po.setState(request.getState());
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
