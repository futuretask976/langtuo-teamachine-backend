package com.gx.sp3.demo.biz.gtmf.impl.business.test.ext;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.RenderBlockContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.SubmitBlockContext;
import com.gx.sp3.demo.gtmf.extension.ExecuteExtRequest;
import lombok.Data;

/**
 * @author miya
 */
@Data
public class TestExecuteExtRequest extends ExecuteExtRequest {
    /**
     *
     */
    private long userId;

    private TestExecuteExtRequest() {
        super();
    }

    /**
     *
     * @param context
     * @return
     */
    public static TestExecuteExtRequest from(RenderBlockContext context) {
        TestExecuteExtRequest request = new TestExecuteExtRequest();
        request.setRequestSource(context.getRequestSource());
        request.setUserId(context.getUserId());
        request.setBizCodes(context.getBizCodeSet());
        return request;
    }

    /**
     *
     * @param context
     * @return
     */
    public static TestExecuteExtRequest from(SubmitBlockContext context) {
        TestExecuteExtRequest request = new TestExecuteExtRequest();
        request.setRequestSource(context.getRequestSource());
        request.setUserId(context.getUserId());
        request.setBizCodes(context.getBizCodeSet());
        return request;
    }

    /**
     *
     * @param context
     * @param specifiedBizCode
     * @return
     */
    public static TestExecuteExtRequest from(RenderBlockContext context, String specifiedBizCode) {
        TestExecuteExtRequest req = from(context);
        return req;
    }
}
