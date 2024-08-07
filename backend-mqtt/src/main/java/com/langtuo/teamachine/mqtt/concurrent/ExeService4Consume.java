package com.langtuo.teamachine.mqtt.concurrent;

import java.util.concurrent.*;

public class ExeService4Consume {
    private static ExecutorService executorService = null;

    public static ExecutorService getExeService() {
        if (executorService == null) {
            synchronized (ExeService4Consume.class) {
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
