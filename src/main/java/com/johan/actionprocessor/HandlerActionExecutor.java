package com.johan.actionprocessor;

/**
 * Created by Administrator on 2017/5/10.
 */

public class HandlerActionExecutor<Input, Output> extends ActionExecutor<Input, Output> {

    private IHandler handler;
    private long delayMills;

    public HandlerActionExecutor(ActionEvent event, Action<Input, Output> action, IHandler handler, long delayMills) {
        super(event, action);
        this.handler = handler;
        this.delayMills = delayMills;
    }

    @Override
    public void execute(final Input data, final IObserver<Output> observer) {
        handler.deal(new Runnable() {
            @Override
            public void run() {
                HandlerActionExecutor.super.execute(data, observer);
            }
        }, delayMills);
    }

}
