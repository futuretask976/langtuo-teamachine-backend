package com.langtuo.teamachine.mqtt.threadpool;

import java.util.concurrent.*;

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
}
