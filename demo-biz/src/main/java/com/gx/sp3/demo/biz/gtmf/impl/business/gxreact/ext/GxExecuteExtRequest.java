package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ext;

import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.context.RenderBlogPageContext;
import com.gx.sp3.demo.gtmf.extension.ExecuteExtRequest;
import com.gx.sp3.demo.gtmf.param.RequestSource;
import lombok.Data;

import java.util.Set;

/**
 * @author miya
 */
@Data
public class GxExecuteExtRequest extends ExecuteExtRequest {
    /**
     *
     */
    private long userId;

    /**
     *
     */
    private Set<String> bizCodes;

    /**
     *
     */
    private RequestSource requestSource;

    private GxExecuteExtRequest() {
        super();
    }

    /**
     *
     * @param context
     * @return
     */
    public static GxExecuteExtRequest from(RenderBlogPageContext context) {
        GxExecuteExtRequest request = new GxExecuteExtRequest();
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
    public static GxExecuteExtRequest from(RenderBlogPageContext context, String specifiedBizCode) {
        GxExecuteExtRequest req = from(context);
        return req;
    }
}
