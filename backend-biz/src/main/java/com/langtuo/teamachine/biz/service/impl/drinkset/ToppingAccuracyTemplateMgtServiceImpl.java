package com.langtuo.teamachine.biz.service.impl.drinkset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drinkset.ToppingAccuracyTemplateDTO;
import com.langtuo.teamachine.api.request.drinkset.ToppingAccuracyTemplatePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drinkset.ToppingAccuracyTemplateMgtService;
import com.langtuo.teamachine.dao.accessor.drinkset.ToppingAccuracyTemplateAccessor;
import com.langtuo.teamachine.dao.po.drinkset.ToppingAccuracyTemplatePO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ToppingAccuracyTemplateMgtServiceImpl implements ToppingAccuracyTemplateMgtService {
    @Resource
    private ToppingAccuracyTemplateAccessor accessor;

    @Override
    public LangTuoResult<List<ToppingAccuracyTemplateDTO>> list(String tenantCode) {
        LangTuoResult<List<ToppingAccuracyTemplateDTO>> langTuoResult = null;
        try {
            List<ToppingAccuracyTemplatePO> list = accessor.selectList(tenantCode);
            List<ToppingAccuracyTemplateDTO> dtoList = list.stream()
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
    public LangTuoResult<PageDTO<ToppingAccuracyTemplateDTO>> search(String tenantName, String templateCode, String templateName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<ToppingAccuracyTemplateDTO>> langTuoResult = null;
        try {
            PageInfo<ToppingAccuracyTemplatePO> pageInfo = accessor.search(tenantName, templateCode, templateName,
                    pageNum, pageSize);
            List<ToppingAccuracyTemplateDTO> dtoList = pageInfo.getList().stream()
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
    public LangTuoResult<ToppingAccuracyTemplateDTO> getByCode(String tenantCode, String specCode) {
        LangTuoResult<ToppingAccuracyTemplateDTO> langTuoResult = null;
        try {
            ToppingAccuracyTemplatePO po = accessor.selectOneByCode(tenantCode, specCode);
            ToppingAccuracyTemplateDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<ToppingAccuracyTemplateDTO> getByName(String tenantCode, String specName) {
        LangTuoResult<ToppingAccuracyTemplateDTO> langTuoResult = null;
        try {
            ToppingAccuracyTemplatePO po = accessor.selectOneByName(tenantCode, specName);
            ToppingAccuracyTemplateDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(ToppingAccuracyTemplatePutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }
        ToppingAccuracyTemplatePO po = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            ToppingAccuracyTemplatePO exist = accessor.selectOneByCode(po.getTenantCode(), po.getTemplateCode());
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

    private ToppingAccuracyTemplateDTO convert(ToppingAccuracyTemplatePO po) {
        if (po == null) {
            return null;
        }

        ToppingAccuracyTemplateDTO dto = new ToppingAccuracyTemplateDTO();
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

    private ToppingAccuracyTemplatePO convert(ToppingAccuracyTemplatePutRequest request) {
        if (request == null) {
            return null;
        }

        ToppingAccuracyTemplatePO po = new ToppingAccuracyTemplatePO();
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
