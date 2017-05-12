package com.johan.actionprocessor;

/**
 * Created by Administrator on 2017/5/10.
 */

public interface Action <Input, Output> {
    Output doAction(Input data);
}
