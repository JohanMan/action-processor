package com.johan.actionprocessor;

/**
 * Created by Administrator on 2017/5/10.
 */

public class ActionExecutor<Input, Output> implements IExecutor <Input, Output> {

    private ActionEvent event;
    private Action<Input, Output> action;
    private boolean isCancel;

    public ActionExecutor(ActionEvent event, Action<Input, Output> action) {
        this.event = event;
        this.action = action;
    }

    public void cancel() {
        this.isCancel = true;
    }

    @Override
    public void execute(Input data, IObserver<Output> observer) {
        if (isCancel) {
            observer.onCancel();
            return ;
        }
        try {
            Output result = action.doAction(data);
            observer.onSuccess(event, result);
        } catch (Exception e) {
            observer.onFail(e);
        }
    }

}
