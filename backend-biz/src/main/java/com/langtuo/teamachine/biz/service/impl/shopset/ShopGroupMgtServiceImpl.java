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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
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
            List<ShopGroupDTO> dtoList = pageInfo.getList().stream()
                    .map(shopGroupPO -> {
                        return convert(shopGroupPO);
                    })
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<ShopGroupDTO>> list(String tenantCode) {
        LangTuoResult<List<ShopGroupDTO>> langTuoResult = null;
        try {
            List<ShopGroupPO> list = shopGroupAccessor.selectList(tenantCode);
            List<ShopGroupDTO> dtoList = list.stream()
                    .map(shopGroupPO -> {
                        return convert(shopGroupPO);
                    })
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(ShopGroupPutRequest request) {
        if (request == null) {
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
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private ShopGroupDTO convert(ShopGroupPO shopGroupPO) {
        if (shopGroupPO == null) {
            return null;
        }

        ShopGroupDTO dto = new ShopGroupDTO();
        dto.setId(shopGroupPO.getId());
        dto.setGmtCreated(shopGroupPO.getGmtCreated());
        dto.setGmtModified(shopGroupPO.getGmtModified());
        dto.setShopGroupCode(shopGroupPO.getShopGroupCode());
        dto.setShopGroupName(shopGroupPO.getShopGroupName());
        dto.setComment(shopGroupPO.getComment());
        dto.setExtraInfo(shopGroupPO.getExtraInfo());

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
