package com.gx.sp3.demo.biz.gtmf.impl.business.test.ext;

import com.gx.sp3.demo.gtmf.extension.ExecuteExtResult;
import com.gx.sp3.demo.gtmf.metadata.ExtensionMetadata;

/**
 * @author miya
 * @param <T>
 */
public class TestExecuteExtResult<T> extends ExecuteExtResult {
    public TestExecuteExtResult(ExtensionMetadata extensionMetadata, Object result) {
        super(extensionMetadata, result);
    }
}
