package com.example.vladzakharo.androidapplication.images;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vlad Zakharo on 02.01.2017.
 */

public class ImageExecutor {
    private static final int corePoolSize = Runtime.getRuntime().availableProcessors();
    private static final int maxPoolSize = corePoolSize;
    private static final int keepAliveTime = 1;
    private static final TimeUnit timeUnit = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> mBlockingQueue = new LinkedBlockingDeque<>();

    public static final ExecutorService threadPoolExecutor = new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime,
            timeUnit,
            mBlockingQueue);
}
