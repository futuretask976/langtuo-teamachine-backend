package com.gx.sp3.demo.biz.gtmf.impl.business.test.ext;

import com.gx.sp3.demo.gtmf.extension.BusinessRecognizeRequest;
import com.gx.sp3.demo.gtmf.param.RequestSource;
import lombok.Data;

/**
 * @author miya
 */
@Data
public class TestRecognizeRequest implements BusinessRecognizeRequest {
    /**
     *
     */
    private RequestSource requestSource;

    /**
     *
     */
    private Long userId;

    // TODO 可以将其它需要用于识别request的字段放在这里
}
