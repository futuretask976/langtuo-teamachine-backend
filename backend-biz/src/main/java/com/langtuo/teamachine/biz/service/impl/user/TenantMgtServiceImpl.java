package com.langtuo.teamachine.biz.service.impl.user;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.user.TenantDTO;
import com.langtuo.teamachine.api.request.user.TenantPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.user.TenantMgtService;
import com.langtuo.teamachine.dao.accessor.user.TenantAccessor;
import com.langtuo.teamachine.dao.po.user.TenantPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TenantMgtServiceImpl implements TenantMgtService {
    @Resource
    private TenantAccessor tenantAccessor;

    @Override
    public LangTuoResult<List<TenantDTO>> list() {
        LangTuoResult<List<TenantDTO>> langTuoResult = null;
        try {
            List<TenantPO> list = tenantAccessor.selectList();
            List<TenantDTO> dtoList = convert(list);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<TenantDTO>> search(String tenantName, String contactPerson,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<TenantDTO>> langTuoResult = null;
        try {
            PageInfo<TenantPO> pageInfo = tenantAccessor.search(tenantName, contactPerson, pageNum, pageSize);
            List<TenantDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<TenantDTO> get(String tenantCode) {
        LangTuoResult<TenantDTO> langTuoResult = null;
        try {
            TenantPO tenantPO = tenantAccessor.selectOne(tenantCode);
            TenantDTO tenantDTO = convert(tenantPO);
            langTuoResult = LangTuoResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("get error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(TenantPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        String tenantCode = request.getTenantCode();
        TenantPO tenantPO = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            TenantPO exist = tenantAccessor.selectOne(tenantCode);
            if (exist != null) {
                int updated = tenantAccessor.update(tenantPO);
            } else {
                int inserted = tenantAccessor.insert(tenantPO);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = tenantAccessor.delete(tenantCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private List<TenantDTO> convert(List<TenantPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<TenantDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private TenantDTO convert(TenantPO po) {
        if (po == null) {
            return null;
        }

        TenantDTO dto = new TenantDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setTenantName(po.getTenantName());
        dto.setContactPerson(po.getContactPerson());
        dto.setContactPhone(po.getContactPhone());
        dto.setComment(po.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return dto;
    }

    private TenantPO convert(TenantPutRequest request) {
        if (request == null) {
            return null;
        }

        TenantPO po = new TenantPO();
        po.setTenantCode(request.getTenantCode());
        po.setTenantName(request.getTenantName());
        po.setContactPerson(request.getContactPerson());
        po.setContactPhone(request.getContactPhone());
        po.setComment(request.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }
}
