package com.johan.actionprocessor;

/**
 * Created by Administrator on 2017/5/10.
 */

public interface IObserver <Output> {
    void onSuccess(ActionEvent event, Output result);
    void onFail(Exception exception);
    void onCancel();
}
