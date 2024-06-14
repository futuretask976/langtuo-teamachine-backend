package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ext;

import com.gx.sp3.demo.gtmf.extension.ExecuteExtResult;
import com.gx.sp3.demo.gtmf.metadata.ExtensionMetadata;

/**
 * @author miya
 * @param <T>
 */
public class GxExecuteExtResult<T> extends ExecuteExtResult {
    public GxExecuteExtResult(ExtensionMetadata extensionMetadata, Object result) {
        super(extensionMetadata, result);
    }
}
