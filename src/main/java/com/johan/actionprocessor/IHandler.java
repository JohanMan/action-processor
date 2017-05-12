package com.johan.actionprocessor;

/**
 * Created by Administrator on 2017/5/10.
 */

public interface IHandler {
    void deal(Runnable command, long delayMillis);
}
