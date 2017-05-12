package com.johan.actionprocessor;

import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/5/10.
 */

public class MainHandler extends Handler implements IHandler {

    private static final ThreadLocal<WeakReference<MainHandler>> handlerLocal = new ThreadLocal<>();

    public static MainHandler create() {
        WeakReference<MainHandler> handlerReference = handlerLocal.get();
        MainHandler handler = handlerReference != null ? handlerReference.get() : null;
        if (handler == null) {
            handler = new MainHandler();
            handlerLocal.set(new WeakReference<>(handler));
        }
        return handler;
    }

    private MainHandler() {

    }

    @Override
    public void deal(Runnable command, long delayMillis) {
        postDelayed(command, delayMillis);
    }

}
