package com.gx.sp3.demo.gtmf.manager;

import com.gx.sp3.demo.gtmf.extension.ExecuteExtRequest;
import com.gx.sp3.demo.gtmf.extension.ExecuteExtResult;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author miya
 */
public interface ExtensionExecutable {
    /**
     *
     * @param req
     * @param extension
     * @param func
     * @param <T>
     * @param <R>
     * @return
     */
    <T, R> List<ExecuteExtResult<R>> executeAllGetNonNull(ExecuteExtRequest req, Class<T> extension, Function<T,R> func);

    /**
     *
     * @param req
     * @param extension
     * @param func
     * @param <T>
     * @param <R>
     * @return
     */
    <T, R> Optional<ExecuteExtResult<R>> executeUntilNonNull(ExecuteExtRequest req, Class<T> extension, Function<T,R> func);
}
