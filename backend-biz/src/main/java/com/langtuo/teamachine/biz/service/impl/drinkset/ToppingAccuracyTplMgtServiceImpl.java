package com.langtuo.teamachine.biz.service.impl.drinkset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drinkset.ToppingAccuracyTplDTO;
import com.langtuo.teamachine.api.request.drinkset.ToppingAccuracyTplPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drinkset.ToppingAccuracyTplMgtService;
import com.langtuo.teamachine.dao.accessor.drinkset.ToppingAccuracyTplAccessor;
import com.langtuo.teamachine.dao.po.drinkset.ToppingAccuracyTplPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ToppingAccuracyTplMgtServiceImpl implements ToppingAccuracyTplMgtService {
    @Resource
    private ToppingAccuracyTplAccessor accessor;

    @Override
    public LangTuoResult<List<ToppingAccuracyTplDTO>> list(String tenantCode) {
        LangTuoResult<List<ToppingAccuracyTplDTO>> langTuoResult = null;
        try {
            List<ToppingAccuracyTplPO> list = accessor.selectList(tenantCode);
            List<ToppingAccuracyTplDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<ToppingAccuracyTplDTO>> search(String tenantName, String templateCode, String templateName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<ToppingAccuracyTplDTO>> langTuoResult = null;
        try {
            PageInfo<ToppingAccuracyTplPO> pageInfo = accessor.search(tenantName, templateCode, templateName,
                    pageNum, pageSize);
            List<ToppingAccuracyTplDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<ToppingAccuracyTplDTO> getByCode(String tenantCode, String specCode) {
        LangTuoResult<ToppingAccuracyTplDTO> langTuoResult = null;
        try {
            ToppingAccuracyTplPO po = accessor.selectOneByCode(tenantCode, specCode);
            ToppingAccuracyTplDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<ToppingAccuracyTplDTO> getByName(String tenantCode, String specName) {
        LangTuoResult<ToppingAccuracyTplDTO> langTuoResult = null;
        try {
            ToppingAccuracyTplPO po = accessor.selectOneByName(tenantCode, specName);
            ToppingAccuracyTplDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(ToppingAccuracyTplPutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }
        ToppingAccuracyTplPO po = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            ToppingAccuracyTplPO exist = accessor.selectOneByCode(po.getTenantCode(), po.getTemplateCode());
            if (exist != null) {
                int updated = accessor.update(po);
            } else {
                int inserted = accessor.insert(po);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String templateCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = accessor.delete(tenantCode, templateCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private ToppingAccuracyTplDTO convert(ToppingAccuracyTplPO po) {
        if (po == null) {
            return null;
        }

        ToppingAccuracyTplDTO dto = new ToppingAccuracyTplDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setTemplateCode(po.getTemplateCode());
        dto.setTemplateName(po.getTemplateName());
        dto.setState(po.getState());
        dto.setOverUnit(po.getOverUnit());
        dto.setOverAmount(po.getOverAmount());
        dto.setUnderUnit(po.getUnderUnit());
        dto.setUnderAmount(po.getUnderAmount());
        dto.setToppingCode(po.getToppingCode());
        dto.setComment(po.getComment());
        return dto;
    }

    private ToppingAccuracyTplPO convert(ToppingAccuracyTplPutRequest request) {
        if (request == null) {
            return null;
        }

        ToppingAccuracyTplPO po = new ToppingAccuracyTplPO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setTemplateCode(request.getTemplateCode());
        po.setTemplateName(request.getTemplateName());
        po.setState(request.getState());
        po.setOverUnit(request.getOverUnit());
        po.setOverAmount(request.getOverAmount());
        po.setUnderUnit(request.getUnderUnit());
        po.setUnderAmount(request.getUnderAmount());
        po.setToppingCode(request.getToppingCode());
        po.setComment(request.getComment());
        return po;
    }
}