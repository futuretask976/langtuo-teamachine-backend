package com.langtuo.teamachine.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.TenantDTO;
import com.langtuo.teamachine.api.request.TenantPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.TenantMgtService;
import com.langtuo.teamachine.dao.accessor.TenantAccessor;
import com.langtuo.teamachine.dao.po.TenantPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TenantMgtServiceImpl implements TenantMgtService {
    @Resource
    private TenantAccessor tenantAccessor;

    @Override
    public LangTuoResult<PageDTO<TenantDTO>> list(int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<TenantDTO>> langTuoResult = null;
        try {
            PageInfo<TenantPO> pageInfo = tenantAccessor.selectList(pageNum, pageSize);
            List<TenantDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            PageDTO<TenantDTO> pageDTO = new PageDTO<>();
            pageDTO.setList(dtoList);
            pageDTO.setPageNum(pageNum);
            pageDTO.setPageSize(pageSize);
            pageDTO.setTotal(pageInfo.getTotal());

            langTuoResult = LangTuoResult.success(pageDTO);
        } catch (Exception e) {
            e.printStackTrace();
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
            List<TenantDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            PageDTO<TenantDTO> pageDTO = new PageDTO<>();
            pageDTO.setList(dtoList);
            pageDTO.setPageNum(pageNum);
            pageDTO.setPageSize(pageSize);
            pageDTO.setTotal(pageInfo.getTotal());

            langTuoResult = LangTuoResult.success(pageDTO);
        } catch (Exception e) {
            e.printStackTrace();
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
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(TenantPutRequest tenantPutRequest) {
        if (tenantPutRequest == null
                || StringUtils.isBlank(tenantPutRequest.getTenantCode())
                || StringUtils.isBlank(tenantPutRequest.getTenantName())
                || StringUtils.isBlank(tenantPutRequest.getContactPerson())
                || StringUtils.isBlank(tenantPutRequest.getContactPhone())
                || StringUtils.isBlank(tenantPutRequest.getImgLink())) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        String tenantCode = tenantPutRequest.getTenantCode();
        TenantPO tenantPO = convert(tenantPutRequest);

        LangTuoResult<Void> langTuoResult = null;
        try {
            TenantPO exist = tenantAccessor.selectOne(tenantCode);
            System.out.printf("$$$$$ TenantMgtServiceImpl#put exist=%s\n", exist);
            if (exist != null) {
                int updated = tenantAccessor.update(tenantPO);
                System.out.printf("$$$$$ TenantMgtServiceImpl#put updated=%s\n", updated);
            } else {
                int inserted = tenantAccessor.insert(tenantPO);
                System.out.printf("$$$$$ TenantMgtServiceImpl#put inserted=%s\n", inserted);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
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
            System.out.printf("$$$$$ TenantMgtServiceImpl#delete deleted=%s\n", deleted);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private TenantDTO convert(TenantPO po) {
        if (po == null) {
            return null;
        }

        TenantDTO dto = new TenantDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setTenantName(po.getTenantName());
        dto.setContactPerson(po.getContactPerson());
        dto.setContactPhone(po.getContactPhone());
        dto.setImgLink(po.getImgLink());
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
        po.setImgLink(request.getImgLink());
        po.setComment(request.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }
}
