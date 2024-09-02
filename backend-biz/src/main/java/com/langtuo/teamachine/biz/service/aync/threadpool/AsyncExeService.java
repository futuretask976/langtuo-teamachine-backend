package com.langtuo.teamachine.biz.service.aync.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncExeService {
    private static ExecutorService executorService = null;

    public static ExecutorService getExecutorService() {
        if (executorService == null) {
            synchronized (AsyncExeService.class) {
                if (executorService == null) {
                    executorService = new ThreadPoolExecutor(10, Integer.MAX_VALUE,
                            10L, TimeUnit.SECONDS,
                            new SynchronousQueue<Runnable>());
                }
            }
        }
        return executorService;
    }
}
