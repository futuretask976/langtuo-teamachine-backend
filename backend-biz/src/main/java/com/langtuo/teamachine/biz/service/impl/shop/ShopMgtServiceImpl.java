package com.langtuo.teamachine.biz.service.impl.shop;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.request.shop.ShopPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.accessor.user.AdminAccessor;
import com.langtuo.teamachine.dao.accessor.user.OrgAccessor;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ShopMgtServiceImpl implements ShopMgtService {
    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private AdminAccessor adminAccessor;

    @Resource
    private OrgAccessor orgAccessor;

    @Resource
    private ShopGroupAccessor shopGroupAccessor;

    @Override
    public TeaMachineResult<ShopDTO> getByCode(String tenantCode, String shopCode) {
        ShopPO shopPO = shopAccessor.selectOneByShopCode(tenantCode, shopCode);
        ShopDTO shopDTO = convert(shopPO);
        return TeaMachineResult.success(shopDTO);
    }

    @Override
    public TeaMachineResult<ShopDTO> getByName(String tenantCode, String shopName) {
        ShopPO shopPO = shopAccessor.selectOneByShopName(tenantCode, shopName);
        ShopDTO shopDTO = convert(shopPO);
        return TeaMachineResult.success(shopDTO);
    }

    @Override
    public TeaMachineResult<PageDTO<ShopDTO>> search(String tenantCode, String shopName, String shopGroupName,
            int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<ShopDTO>> teaMachineResult;
        try {
            String shopGroupCode = null;
            if (StringUtils.isNotBlank(shopGroupName)) {
                ShopGroupPO shopGroupPO = shopGroupAccessor.selectOneByShopGroupName(tenantCode, shopGroupName);
                if (shopGroupPO == null) {
                    return TeaMachineResult.success(new PageDTO<>(null, 0, pageNum, pageSize));
                } else {
                    shopGroupCode = shopGroupPO.getShopGroupCode();
                }
            }

            PageInfo<ShopPO> pageInfo = shopAccessor.search(tenantCode, shopName, shopGroupCode,
                    pageNum, pageSize);
            List<ShopDTO> dtoList = convert(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<ShopDTO>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }

        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<ShopDTO>> list(String tenantCode) {
        TeaMachineResult<List<ShopDTO>> teaMachineResult;
        try {
            List<ShopPO> list = shopAccessor.selectList(tenantCode);
            List<ShopDTO> dtoList = convert(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<ShopDTO>> listByShopGroupCode(String tenantCode, String shopGroupCode) {
        TeaMachineResult<List<ShopDTO>> teaMachineResult;
        try {
            List<ShopPO> list = shopAccessor.selectListByShopGroupCode(tenantCode, shopGroupCode);
            List<ShopDTO> dtoList = convert(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<ShopDTO>> listByAdminOrg(String tenantCode) {
        TeaMachineResult<List<ShopDTO>> teaMachineResult;
        try {
            List<ShopGroupPO> shopGroupPOList = shopGroupAccessor.selectList(tenantCode);
            List<String> shopGroupCodeList = shopGroupPOList.stream()
                    .map(shopGroupDTO -> shopGroupDTO.getShopGroupCode())
                    .collect(Collectors.toList());

            List<ShopPO> list = shopAccessor.selectListByShopGroupCode(tenantCode, shopGroupCodeList);
            teaMachineResult = TeaMachineResult.success(convert(list));
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(ShopPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        ShopPO shopPO = convert(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            ShopPO exist = shopAccessor.selectOneByShopCode(request.getTenantCode(), request.getShopCode());
            if (exist != null) {
                int updated = shopAccessor.update(shopPO);
            } else {
                int inserted = shopAccessor.insert(shopPO);
            }
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String shopGroupCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(shopGroupCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = shopAccessor.delete(tenantCode, shopGroupCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Integer> countByShopGroupCode(String tenantCode, String shopGroupCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(shopGroupCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Integer> teaMachineResult;
        try {
            int cnt = shopAccessor.countByShopGroupCode(tenantCode, shopGroupCode);
            teaMachineResult = TeaMachineResult.success(cnt);
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
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

        ShopGroupPO shopGroupPO = shopGroupAccessor.selectOneByShopGroupCode(po.getTenantCode(), po.getShopGroupCode());
        if (shopGroupPO != null) {
            dto.setShopGroupCode(shopGroupPO.getShopGroupCode());
            dto.setShopGroupName(shopGroupPO.getShopGroupName());
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
