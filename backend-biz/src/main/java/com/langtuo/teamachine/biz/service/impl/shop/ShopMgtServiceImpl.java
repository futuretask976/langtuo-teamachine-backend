package com.langtuo.teamachine.biz.service.impl.shop;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.request.shop.ShopPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.api.service.user.AdminMgtService;
import com.langtuo.teamachine.api.service.user.OrgMgtService;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.getListModel;
import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

@Component
@Slf4j
public class ShopMgtServiceImpl implements ShopMgtService {
    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private AdminMgtService adminMgtService;

    @Resource
    private OrgMgtService orgMgtService;

    @Resource
    private ShopGroupMgtService shopGroupMgtService;

    @Override
    public LangTuoResult<ShopDTO> getByCode(String tenantCode, String shopCode) {
        ShopPO shopPO = shopAccessor.selectOneByCode(tenantCode, shopCode);
        ShopDTO shopDTO = convert(shopPO);
        return LangTuoResult.success(shopDTO);
    }

    @Override
    public LangTuoResult<ShopDTO> getByName(String tenantCode, String shopName) {
        ShopPO shopPO = shopAccessor.selectOneByName(tenantCode, shopName);
        ShopDTO shopDTO = convert(shopPO);
        return LangTuoResult.success(shopDTO);
    }

    @Override
    public LangTuoResult<PageDTO<ShopDTO>> search(String tenantCode, String shopName, String shopGroupName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<ShopDTO>> langTuoResult;
        try {
            String shopGroupCode = null;
            if (StringUtils.isNotBlank(shopGroupName)) {
                ShopGroupDTO shopGroupDTO = getModel(shopGroupMgtService.getByName(tenantCode, shopGroupName));
                if (shopGroupDTO == null) {
                    return LangTuoResult.success(new PageDTO<>(null, 0, pageNum, pageSize));
                } else {
                    shopGroupCode = shopGroupDTO.getShopGroupCode();
                }
            }

            PageInfo<ShopPO> pageInfo = shopAccessor.search(tenantCode, shopName, shopGroupCode,
                    pageNum, pageSize);
            List<ShopDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<ShopDTO>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }

        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<ShopDTO>> list(String tenantCode) {
        LangTuoResult<List<ShopDTO>> langTuoResult;
        try {
            List<ShopPO> list = shopAccessor.selectList(tenantCode);
            List<ShopDTO> dtoList = convert(list);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<ShopDTO>> listByShopGroupCode(String tenantCode, String shopGroupCode) {
        LangTuoResult<List<ShopDTO>> langTuoResult;
        try {
            List<ShopPO> list = shopAccessor.selectList(tenantCode, shopGroupCode);
            List<ShopDTO> dtoList = convert(list);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<ShopDTO>> listByAdminOrg(String tenantCode) {
        LangTuoResult<List<ShopDTO>> langTuoResult;
        try {
            List<ShopGroupDTO> shopGroupDTOList = getListModel(shopGroupMgtService.listByAdminOrg(tenantCode));
            List<String> shopGroupCodeList = shopGroupDTOList.stream()
                    .map(shopGroupDTO -> shopGroupDTO.getShopGroupCode())
                    .collect(Collectors.toList());

            List<ShopPO> list = shopAccessor.selectListByShopGroupCodeList(tenantCode, shopGroupCodeList);
            langTuoResult = LangTuoResult.success(convert(list));
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(ShopPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        ShopPO shopPO = convert(request);

        LangTuoResult<Void> langTuoResult;
        try {
            ShopPO exist = shopAccessor.selectOneByCode(request.getTenantCode(), request.getShopCode());
            if (exist != null) {
                int updated = shopAccessor.update(shopPO);
            } else {
                int inserted = shopAccessor.insert(shopPO);
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

        LangTuoResult<Void> langTuoResult;
        try {
            int deleted = shopAccessor.delete(tenantCode, shopGroupCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Integer> countByShopGroupCode(String tenantCode, String shopGroupCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(shopGroupCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Integer> langTuoResult = null;
        try {
            int cnt = shopAccessor.countByShopGroupCode(tenantCode, shopGroupCode);
            langTuoResult = LangTuoResult.success(cnt);
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private List<ShopDTO> convert(List<ShopPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<ShopDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private ShopDTO convert(ShopPO po) {
        if (po == null) {
            return null;
        }

        ShopDTO dto = new ShopDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setShopCode(po.getShopCode());
        dto.setShopName(po.getShopName());
        dto.setShopType(po.getShopType());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());

        ShopGroupDTO shopGroupDTO = getModel(shopGroupMgtService.getByCode(po.getTenantCode(), po.getShopGroupCode()));
        if (shopGroupDTO != null) {
            dto.setShopGroupCode(shopGroupDTO.getShopGroupCode());
            dto.setShopGroupName(shopGroupDTO.getShopGroupName());
        }

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
