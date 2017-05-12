package com.johan.actionprocessor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/5/10.
 */

public class BackgroundHandler implements IHandler {

    private static final int THREAD_POOL_SIZE = 10;
    private static final BackgroundHandler handler = new BackgroundHandler();
    private ScheduledExecutorService service;

    private BackgroundHandler() {
        service = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
    }

    public static BackgroundHandler create() {
        return handler;
    }

    @Override
    public void deal(final Runnable command, final long delayMillis) {
        service.schedule(command, delayMillis, TimeUnit.MILLISECONDS);
    }

}
