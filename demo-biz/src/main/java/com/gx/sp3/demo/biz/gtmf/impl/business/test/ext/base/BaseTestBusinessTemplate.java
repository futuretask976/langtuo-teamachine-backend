package com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.base;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.TestRecognizeRequest;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessRenderBlockLinkExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessRenderBlockTextExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessSubmitBlockInputExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessSubmitBlockSelectExtPoints;
import com.gx.sp3.demo.gtmf.annotation.ExtensionDef;
import com.gx.sp3.demo.gtmf.annotation.ExtensionFacade;
import com.gx.sp3.demo.gtmf.exception.GTmfException;
import com.gx.sp3.demo.gtmf.extension.BusinessRecognizeRequest;
import com.gx.sp3.demo.gtmf.extension.BusinessTemplate;

@ExtensionFacade
public abstract class BaseTestBusinessTemplate implements BusinessTemplate {
    @Override
    public boolean match(BusinessRecognizeRequest request) {
        if (!(request instanceof TestRecognizeRequest)) {
            return false;
        }
        TestRecognizeRequest testRecognizeRequest = (TestRecognizeRequest) request;

        if (request == null || testRecognizeRequest.getUserId() == null) {
            return false;
        }

        Long userId = testRecognizeRequest.getUserId();
        if (userId % 10000 < 5000) {
            return match4Render(testRecognizeRequest);
        }
        if (userId % 10000 < 10000) {
            return match4Submit(testRecognizeRequest);
        }
        // 参数不对
        throw new GTmfException(GTmfException.ErrorCode.ILLEGAL_ARGUMENT_ERROR);
    }

    /**
     * 确认收货的生效条件
     * @param request
     * @return
     */
    abstract protected boolean match4Render(TestRecognizeRequest request);

    /**
     * 支付成功的生效条件
     * @param request
     * @return
     */
    abstract protected boolean match4Submit(TestRecognizeRequest request);

    // 以下为支付成功页的定制点
    @ExtensionDef(code = "BaseBlockRenderBusinessTemplate.getTestBusinessRenderBlockTextExtPoints", desc = "测试业务流程1段落1模板")
    public TestBusinessRenderBlockTextExtPoints getTestBusinessRenderBlockTextExtPoints() {
        return null;
    }

    @ExtensionDef(code = "BaseBlockRenderBusinessTemplate.getTestBusinessRenderBlockLinkExtPoints", desc = "测试业务流程1段落2模板")
    public TestBusinessRenderBlockLinkExtPoints getTestBusinessRenderBlockLinkExtPoints() {
        return null;
    }

    @ExtensionDef(code = "BaseBlockRenderBusinessTemplate.getTestBusinessSubmitBlockInputExtPoints", desc = "测试业务流程2段落1模板")
    public TestBusinessSubmitBlockInputExtPoints getTestBusinessSubmitBlockInputExtPoints() {
        return null;
    }

    @ExtensionDef(code = "BaseBlockRenderBusinessTemplate.getTestBusinessSubmitBlockSelectExtPoints", desc = "测试业务流程2段落2模板")
    public TestBusinessSubmitBlockSelectExtPoints getTestBusinessSubmitBlockSelectExtPoints() {
        return null;
    }
}