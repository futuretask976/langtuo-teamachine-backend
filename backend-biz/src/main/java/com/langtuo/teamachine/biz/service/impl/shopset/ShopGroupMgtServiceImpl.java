package com.langtuo.teamachine.biz.service.impl.shopset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.shopset.ShopGroupDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.shopset.ShopGroupPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.shopset.ShopGroupMgtService;
import com.langtuo.teamachine.dao.accessor.shopset.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.shopset.ShopGroupPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ShopGroupMgtServiceImpl implements ShopGroupMgtService {
    @Resource
    private ShopGroupAccessor shopGroupAccessor;

    @Override
    public LangTuoResult<ShopGroupDTO> get(String tenantCode, String loginName) {
        ShopGroupPO shopGroupPO = shopGroupAccessor.selectOne(tenantCode, loginName);
        ShopGroupDTO shopGroupDTO = convert(shopGroupPO);
        return LangTuoResult.success(shopGroupDTO);
    }

    @Override
    public LangTuoResult<PageDTO<ShopGroupDTO>> search(String tenantCode, String shopGroupName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<ShopGroupDTO>> langTuoResult = null;
        try {
            PageInfo<ShopGroupPO> pageInfo = shopGroupAccessor.search(tenantCode, shopGroupName,
                    pageNum, pageSize);
            List<ShopGroupDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<ShopGroupDTO>> list(String tenantCode) {
        LangTuoResult<List<ShopGroupDTO>> langTuoResult = null;
        try {
            List<ShopGroupPO> list = shopGroupAccessor.selectList(tenantCode);
            List<ShopGroupDTO> dtoList = convert(list);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(ShopGroupPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        ShopGroupPO shopGroupPO = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            ShopGroupPO exist = shopGroupAccessor.selectOne(request.getTenantCode(), request.getShopGroupCode());
            if (exist != null) {
                int updated = shopGroupAccessor.update(shopGroupPO);
            } else {
                int inserted = shopGroupAccessor.insert(shopGroupPO);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String shopGroupCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(shopGroupCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = shopGroupAccessor.delete(tenantCode, shopGroupCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private List<ShopGroupDTO> convert(List<ShopGroupPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<ShopGroupDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private ShopGroupDTO convert(ShopGroupPO po) {
        if (po == null) {
            return null;
        }

        ShopGroupDTO dto = new ShopGroupDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setShopGroupCode(po.getShopGroupCode());
        dto.setShopGroupName(po.getShopGroupName());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());

        return dto;
    }

    private ShopGroupPO convert(ShopGroupPutRequest request) {
        if (request == null) {
            return null;
        }

        ShopGroupPO po = new ShopGroupPO();
        po.setShopGroupName(request.getShopGroupName());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setComment(request.getComment());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());

        return po;
    }
}
