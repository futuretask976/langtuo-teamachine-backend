package com.gx.sp3.demo.biz.gtmf.impl.business.test.product.producta;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.RenderBlockContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.SubmitBlockContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.TestRecognizeRequest;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.base.BaseTestBusinessTemplate;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessRenderBlockLinkExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessRenderBlockTextExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessSubmitBlockInputExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessSubmitBlockSelectExtPoints;
import com.gx.sp3.demo.gtmf.annotation.BusinessDef;
import com.gx.sp3.demo.gtmf.exception.GTmfException;

/**
 * @author miya
 */
@BusinessDef(code = BusinessAProduct.CODE, desc = "业务产品A")
public class BusinessAProduct extends BaseTestBusinessTemplate {
    public static final String CODE = "BusinessAProduct";

    @Override
    protected boolean match4Render(TestRecognizeRequest request) {
        return true;
    }

    @Override
    protected boolean match4Submit(TestRecognizeRequest request) {
        return true;
    }

    @Override
    public TestBusinessRenderBlockTextExtPoints getTestBusinessRenderBlockTextExtPoints() {
        return new TestBusinessRenderBlockTextExtPoints() {
            @Override
            public String getTextStyle(RenderBlockContext context) throws GTmfException {
                return "BusinessAProduct___TestBusinessRenderBlockTextExtPoints___getTextStyle";
            }

            @Override
            public String getTextColor(RenderBlockContext context) throws GTmfException {
                return "BusinessAProduct___TestBusinessRenderBlockTextExtPoints___getTextColor";
            }
        };
    }

    @Override
    public TestBusinessRenderBlockLinkExtPoints getTestBusinessRenderBlockLinkExtPoints() {
        return new TestBusinessRenderBlockLinkExtPoints() {
            @Override
            public String getLinkStyle(RenderBlockContext context) throws GTmfException {
                return "BusinessAProduct___TestBusinessRenderBlockLinkExtPoints___getLinkStyle";
            }

            @Override
            public String getLinkColor(RenderBlockContext context) throws GTmfException {
                return "BusinessAProduct___TestBusinessRenderBlockLinkExtPoints___getLinkColor";
            }
        };
    }

    @Override
    public TestBusinessSubmitBlockInputExtPoints getTestBusinessSubmitBlockInputExtPoints() {
        return new TestBusinessSubmitBlockInputExtPoints() {
            @Override
            public String getInputStyle(SubmitBlockContext context) throws GTmfException {
                return "BusinessAProduct___TestBusinessSubmitBlockInputExtPoints___getInputStyle";
            }

            @Override
            public String getInputVal(SubmitBlockContext context) throws GTmfException {
                return "BusinessAProduct___TestBusinessSubmitBlockInputExtPoints___getInputVal";
            }
        };
    }

    @Override
    public TestBusinessSubmitBlockSelectExtPoints getTestBusinessSubmitBlockSelectExtPoints() {
        return new TestBusinessSubmitBlockSelectExtPoints() {
            @Override
            public String getSelectStyle(SubmitBlockContext context) throws GTmfException {
                return "BusinessAProduct___TestBusinessSubmitBlockSelectExtPoints___getSelectStyle";
            }

            @Override
            public String getSelectVal(SubmitBlockContext context) throws GTmfException {
                return "BusinessAProduct___TestBusinessSubmitBlockSelectExtPoints___getSelectVal";
            }
        };
    }
}
