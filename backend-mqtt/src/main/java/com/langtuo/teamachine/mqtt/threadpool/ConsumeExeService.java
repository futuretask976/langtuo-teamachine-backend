package com.langtuo.teamachine.mqtt.threadpool;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.util.concurrent.*;

@Slf4j
public class ConsumeExeService {
    private static ExecutorService executorService = null;

    public static ExecutorService getExeService() {
        if (executorService == null) {
            synchronized (ConsumeExeService.class) {
                if (executorService == null) {
                    executorService = new ThreadPoolExecutor(10, Integer.MAX_VALUE,
                            100L, TimeUnit.SECONDS,
                            new SynchronousQueue<Runnable>());
                }
            }
        }
        return executorService;
    }

    public static void waitUntilTerminate() {
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
