package com.gx.sp3.demo.biz.gtmf.impl.business.test.transfer;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.RenderBlockContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.SubmitBlockContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.TestRecognizeRequest;
import com.gx.sp3.demo.gtmf.consts.ClientTypeEnum;
import com.gx.sp3.demo.gtmf.manager.BusinessExtensionManager;
import com.gx.sp3.demo.gtmf.param.RequestSource;
import com.gx.sp3.demo.gtmf.param.UserAgent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component("com.gx.biz.gtmf.impl.business.test.transfer.RequestTransfer")
public class RequestTransfer {
    @Resource
    private BusinessExtensionManager businessExtensionManager;

    /**
     *
     * @param renderRequestDTO
     * @return
     */
    public RenderBlockContext buildContext(RenderRequestDTO renderRequestDTO) {
        RequestSource requestSource = new RequestSource();
        requestSource.setAppName("idea");
        requestSource.setAppVersion("v1.0");
        requestSource.setTtid("guangxia@idea_v1.0");
        requestSource.setUserAgent(new UserAgent());
        requestSource.setClientTypeEnum(ClientTypeEnum.ALIPAY_H5_CLIENT);

        RenderBlockContext renderBlockContext = new RenderBlockContext();
        renderBlockContext.setRequestSource(requestSource);
        renderBlockContext.setUserId(1234567890L);

        renderBlockContext.setBizCodeSet(parseBizCodeSet(renderRequestDTO));

        return renderBlockContext;
    }

    public SubmitBlockContext buildContext(SubmitRequestDTO submitRequestDTO) {
        RequestSource requestSource = new RequestSource();
        requestSource.setAppName("idea");
        requestSource.setAppVersion("v1.0");
        requestSource.setTtid("guangxia@idea_v1.0");
        requestSource.setUserAgent(new UserAgent());
        requestSource.setClientTypeEnum(ClientTypeEnum.ALIPAY_H5_CLIENT);

        SubmitBlockContext submitBlockContext = new SubmitBlockContext();
        submitBlockContext.setRequestSource(requestSource);
        submitBlockContext.setUserId(1234567890L);

        submitBlockContext.setBizCodeSet(parseBizCodeSet(submitRequestDTO));

        return submitBlockContext;
    }

    /**
     *
     * @param renderRequestDTO
     * @return
     */
    private Set<String> parseBizCodeSet(RenderRequestDTO renderRequestDTO) {
        TestRecognizeRequest testRecognizeRequest = new TestRecognizeRequest();
        testRecognizeRequest.setUserId(renderRequestDTO.getUserId());
        Set<String> matchBizCodes = businessExtensionManager.getMatchBizCodes(testRecognizeRequest);
        return matchBizCodes;
    }

    /**
     *
     * @param submitRequestDTO
     * @return
     */
    private Set<String> parseBizCodeSet(SubmitRequestDTO submitRequestDTO) {
        TestRecognizeRequest testRecognizeRequest = new TestRecognizeRequest();
        testRecognizeRequest.setUserId(submitRequestDTO.getUserId());
        Set<String> matchBizCodes = businessExtensionManager.getMatchBizCodes(testRecognizeRequest);
        return matchBizCodes;
    }
}
