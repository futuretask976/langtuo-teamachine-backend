package com.gx.sp3.demo.gtmf.extension;

import com.gx.sp3.demo.gtmf.manager.BusinessExtensionManager;
import com.gx.sp3.demo.gtmf.manager.ExtensionExecutable;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author miya
 */
public class SystemAbility implements ExtensionExecutable {
    @Resource
    private BusinessExtensionManager businessExtensionManager;

    @Override
    public <T, R> List<ExecuteExtResult<R>> executeAllGetNonNull(ExecuteExtRequest req, Class<T> extension, Function<T, R> func) {
        return businessExtensionManager.executeAllGetNonNull(req, extension, func);
    }

    @Override
    public <T, R> Optional<ExecuteExtResult<R>> executeUntilNonNull(ExecuteExtRequest req, Class<T> extension, Function<T, R> func) {
        return businessExtensionManager.executeUntilNonNull(req, extension, func);
    }
}
