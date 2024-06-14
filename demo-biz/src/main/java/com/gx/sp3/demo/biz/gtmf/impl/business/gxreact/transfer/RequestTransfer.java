package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.transfer;

import com.gx.sp3.demo.api.request.RenderBlogPageRequest;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.context.RenderBlogPageContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ext.GxRecognizeRequest;
import com.gx.sp3.demo.gtmf.consts.ClientTypeEnum;
import com.gx.sp3.demo.gtmf.manager.BusinessExtensionManager;
import com.gx.sp3.demo.gtmf.param.RequestSource;
import com.gx.sp3.demo.gtmf.param.UserAgent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component("com.gx.biz.gtmf.impl.business.gxreact.transfer.RequestTransfer")
public class RequestTransfer {
    @Resource
    private BusinessExtensionManager businessExtensionManager;

    public RenderBlogPageRequest transferToRenderBlogPageRequest(Long userId, Long modelId) {
        RenderBlogPageRequest renderBlogPageRequestDTO = new RenderBlogPageRequest();
        renderBlogPageRequestDTO.setUserId(userId);
        renderBlogPageRequestDTO.setModelId(modelId);
        return renderBlogPageRequestDTO;
    }

    /**
     *
     * @param renderRequestDTO
     * @return
     */
    public RenderBlogPageContext transferToRenderBlogPageContext(RenderBlogPageRequest renderRequestDTO) {
        RequestSource requestSource = new RequestSource();
        requestSource.setAppName("idea");
        requestSource.setAppVersion("v1.0");
        requestSource.setTtid("guangxia@idea_v1.0");
        requestSource.setUserAgent(new UserAgent());
        requestSource.setClientTypeEnum(ClientTypeEnum.ALIPAY_H5_CLIENT);

        RenderBlogPageContext renderContext = new RenderBlogPageContext();
        renderContext.setRequestSource(requestSource);
        renderContext.setUserId(1234567890L);

        renderContext.setBizCodeSet(parseBizCodeSet(renderContext));

        return renderContext;
    }

    /**
     *
     * @param renderRequestDTO
     * @return
     */
    private Set<String> parseBizCodeSet(RenderBlogPageContext renderRequestDTO) {
        GxRecognizeRequest gxRecognizeRequest = new GxRecognizeRequest();
        gxRecognizeRequest.setUserId(renderRequestDTO.getUserId());

        if (renderRequestDTO.getRequestSource() != null) {
            RequestSource requestSource = renderRequestDTO.getRequestSource();
            gxRecognizeRequest.setReferer(requestSource.getReferer());
        }

        Set<String> matchBizCodes = businessExtensionManager.getMatchBizCodes(gxRecognizeRequest);
        return matchBizCodes;
    }
}
