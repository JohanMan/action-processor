package com.johan.actionprocessor;

/**
 * Created by Administrator on 2017/5/10.
 */

public interface IExecutor <Input, Output> {
    void execute(Input data, IObserver<Output> observer);
}
