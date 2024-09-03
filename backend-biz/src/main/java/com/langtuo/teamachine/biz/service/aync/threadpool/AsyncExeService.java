package com.langtuo.teamachine.biz.service.aync.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
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

    public void waitUntilTerminate() {
        if (executorService != null) {
            try {
                log.info("consumeExeService|awaitTermination|beginning");
                // 启动关闭流程
                executorService.shutdown();
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    // 超时则强制关闭
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.error("consumeExeService|awaitTermination|fatal|" + e.getMessage(), e);
                // 重新中断线程
                Thread.currentThread().interrupt();
            }
        }
    }
}
