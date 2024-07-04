package com.langtuo.teamachine.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.AdminDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.ShopDTO;
import com.langtuo.teamachine.api.request.ShopPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.ShopMgtService;
import com.langtuo.teamachine.dao.accessor.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.ShopGroupPO;
import com.langtuo.teamachine.dao.po.ShopPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ShopMgtServiceImpl implements ShopMgtService {
    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private ShopGroupAccessor shopGroupAccessor;

    @Override
    public LangTuoResult<ShopDTO> get(String tenantCode, String shopCode, String shopName) {
        ShopPO shopPO = shopAccessor.selectOne(tenantCode, shopCode, shopName);

        ShopDTO shopDTO = convert(shopPO);
        return LangTuoResult.success(shopDTO);
    }

    @Override
    public LangTuoResult<PageDTO<ShopDTO>> search(String tenantCode, String shopName, String shopGroupName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<ShopDTO>> langTuoResult = null;
        try {
            String shopGroupCode = null;
            if (StringUtils.isNotBlank(shopGroupName)) {
                Optional<ShopGroupPO> opt = shopGroupAccessor.selectList(tenantCode).stream()
                        .filter(item -> item.getShopGroupName().equals(shopGroupName))
                        .findFirst();
                if (opt.isPresent()) {
                    shopGroupCode = opt.get().getShopGroupCode();
                } else {
                    return LangTuoResult.success(new PageDTO<>(null, 0, pageNum, pageSize));
                }
            }

            PageInfo<ShopPO> pageInfo = shopAccessor.search(tenantCode, shopName, shopGroupCode,
                    pageNum, pageSize);
            List<ShopDTO> dtoList = pageInfo.getList().stream()
                    .map(shopPO -> {
                        return convert(shopPO);
                    })
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(new PageDTO<ShopDTO>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<ShopDTO>> list(String tenantCode) {
        LangTuoResult<List<ShopDTO>> langTuoResult = null;
        try {
            List<ShopPO> list = shopAccessor.selectList(tenantCode);
            List<ShopDTO> dtoList = list.stream()
                    .map(shopPO -> {
                        return convert(shopPO);
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
    public LangTuoResult<Void> put(ShopPutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        ShopPO shopPO = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            ShopPO exist = shopAccessor.selectOne(request.getTenantCode(), request.getShopCode(),
                    request.getShopName());
            if (exist != null) {
                int updated = shopAccessor.update(shopPO);
            } else {
                int inserted = shopAccessor.insert(shopPO);
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
            int deleted = shopAccessor.delete(tenantCode, shopGroupCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private ShopDTO convert(ShopPO shopPO) {
        if (shopPO == null) {
            return null;
        }

        ShopDTO dto = new ShopDTO();
        dto.setId(shopPO.getId());
        dto.setGmtCreated(shopPO.getGmtCreated());
        dto.setGmtModified(shopPO.getGmtModified());
        dto.setShopCode(shopPO.getShopCode());
        dto.setShopName(shopPO.getShopName());
        dto.setShopType(shopPO.getShopType());
        dto.setShopGroupCode(shopPO.getShopGroupCode());
        dto.setComment(shopPO.getComment());
        dto.setTenantCode(shopPO.getTenantCode());
        dto.setExtraInfo(shopPO.getExtraInfo());

        return dto;
    }

    private ShopPO convert(ShopPutRequest request) {
        if (request == null) {
            return null;
        }

        ShopPO po = new ShopPO();
        po.setShopName(request.getShopName());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setShopType(request.getShopType());
        po.setComment(request.getComment());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());

        return po;
    }
}
