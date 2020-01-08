package com.ars.commons.thread;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * 〈一句话介绍功能〉<br>
 * 线程池创建工具类
 *
 * @author jierui on 2020-01-08.
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ThreadPoolUtils {

    private static int keepAliveMin = 5;

    private static RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
        try {
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };

    public static ExecutorService createSingle(int queueSize){
        return create(1, 1, queueSize);
    }

    public static ExecutorService createSingle(int queueSize, String threadPrefix){
        return create(1, 1, queueSize, threadPrefix);
    }

    public static ExecutorService create(int coreSize, int maxSize, int queueSize) {
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveMin, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(queueSize), rejectedExecutionHandler);
    }

    public static ExecutorService create(int coreSize, int maxSize, int queueSize, String threadPrefix) {
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveMin, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(queueSize),
                new BasicThreadFactory.Builder().namingPattern(threadPrefix + "-%d").build(),
                rejectedExecutionHandler);
    }
}
