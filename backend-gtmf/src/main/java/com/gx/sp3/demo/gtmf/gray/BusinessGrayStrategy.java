package com.gx.sp3.demo.gtmf.gray;

import com.gx.sp3.demo.gtmf.extension.BusinessRecognizeRequest;
import com.gx.sp3.demo.gtmf.extension.ExecuteExtRequest;

/**
 * @author miya
 */
public interface BusinessGrayStrategy {
    /**
     *
     * @param req
     * @return
     */
    boolean match(ExecuteExtRequest req);

    /**
     *
     * @param req
     * @return
     */
    boolean simpleMatch(BusinessRecognizeRequest req);
}
