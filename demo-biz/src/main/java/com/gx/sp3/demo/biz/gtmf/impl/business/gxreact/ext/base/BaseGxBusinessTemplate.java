package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ext.base;

import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ext.GxRecognizeRequest;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ext.customized.GxBusinessRenderBlogPageExtPoints;
import com.gx.sp3.demo.gtmf.annotation.ExtensionDef;
import com.gx.sp3.demo.gtmf.annotation.ExtensionFacade;
import com.gx.sp3.demo.gtmf.exception.GTmfException;
import com.gx.sp3.demo.gtmf.extension.BusinessRecognizeRequest;
import com.gx.sp3.demo.gtmf.extension.BusinessTemplate;

@ExtensionFacade
public abstract class BaseGxBusinessTemplate implements BusinessTemplate {
    @Override
    public boolean match(BusinessRecognizeRequest request) {
        if (!(request instanceof GxRecognizeRequest)) {
            return false;
        }
        GxRecognizeRequest gxRecognizeRequest = (GxRecognizeRequest) request;

        if (request == null || gxRecognizeRequest.getUserId() == null) {
            return false;
        }

        Long userId = gxRecognizeRequest.getUserId();
        if (userId % 10000 < 5000) {
            return true;
        } else if (userId % 10000 < 10000) {
            return true;
        }
        // 参数不对
        throw new GTmfException(GTmfException.ErrorCode.ILLEGAL_ARGUMENT_ERROR);
    }

    @ExtensionDef(code = "BaseGxBusinessTemplate.getGxBusinessRenderBlogPageExtPoints", desc = "GX业务模板.渲染Blog页面")
    public GxBusinessRenderBlogPageExtPoints getGxBusinessRenderBlogPageExtPoints() {
        return null;
    }
}