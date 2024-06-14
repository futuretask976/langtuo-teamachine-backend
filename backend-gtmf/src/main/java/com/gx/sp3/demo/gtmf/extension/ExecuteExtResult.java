package com.gx.sp3.demo.gtmf.extension;

import com.gx.sp3.demo.gtmf.metadata.ExtensionMetadata;
import lombok.Getter;
import lombok.Setter;

public class ExecuteExtResult<T> {
    /**
     *
     */
    @Getter
    @Setter
    private ExtensionMetadata extensionMetadata;

    /**
     *
     */
    @Getter
    @Setter
    private T result;

    public ExecuteExtResult(ExtensionMetadata extensionMetadata, T result) {
        this.extensionMetadata = extensionMetadata;
        this.result = result;
    }
}
