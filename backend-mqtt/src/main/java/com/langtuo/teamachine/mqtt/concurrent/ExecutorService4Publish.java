package com.langtuo.teamachine.mqtt.concurrent;

import java.util.concurrent.*;

public class ExecutorService4Publish {
    private static ExecutorService executorService = null;

    public static ExecutorService getExecutorService() {
        if (executorService == null) {
            synchronized (ExecutorService4Publish.class) {
                if (executorService == null) {
                    executorService = new ThreadPoolExecutor(1, Integer.MAX_VALUE,
                            10L, TimeUnit.SECONDS,
                            new SynchronousQueue<Runnable>());
                }
            }
        }
        return executorService;
    }
}
