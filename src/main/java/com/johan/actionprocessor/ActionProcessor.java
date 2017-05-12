package com.johan.actionprocessor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */

public class ActionProcessor implements IProcessor {

    private ActionThread currentThread;
    private long delayMills;
    private List<ActionExecutor> executorList;
    private ActionExceptionListener exceptionListener;
    private ActionCancelListener cancelListener;

    private List<ActionProcessor> childProcessorList;
    private Object result;
    private int index;

    public ActionProcessor() {
        this.executorList = new LinkedList<>();
        this.currentThread = ActionThread.MAIN;
        this.delayMills = 0;
    }

    private ActionProcessor(List<ActionExecutor> executorList, ActionExceptionListener exceptionListener, ActionCancelListener cancelListener, Object result, int index) {
        this.executorList = executorList;
        this.exceptionListener = exceptionListener;
        this.cancelListener = cancelListener;
        this.result = result;
        this.index = index;
    }

    @Override
    public ActionProcessor thread(ActionThread thread) {
        this.currentThread = thread;
        return this;
    }

    @Override
    public ActionProcessor delay(long delayMills) {
        this.delayMills = delayMills;
        return this;
    }

    @Override
    public <Output> ActionProcessor begin(Action<Void, Output> action) {
        action(action);
        return this;
    }

    @Override
    public <Input> ActionProcessor end(Action<Input, Void> action) {
        action(action);
        return this;
    }

    @Override
    public <Input, Output> ActionProcessor action(Action<Input, Output> action) {
        HandlerActionExecutor<Input, Output> executor = new HandlerActionExecutor<>(ActionEvent.ACTION, action, getHandler(), delayMills);
        addToQueue(executor);
        return this;
    }

    @Override
    public <Input> ActionProcessor filter(Action<Input, Boolean> action) {
        ActionExecutor<Input, Boolean> executor = new ActionExecutor<>(ActionEvent.FILTER, action);
        addToQueue(executor);
        return this;
    }

    @Override
    public <Input> ActionProcessor take(final int start, final int end) {
        Action<Input, Rank> action = new Action<Input, Rank>() {
            @Override
            public Rank doAction(Input data) {
                return new Rank(start, end);
            }
        };
        ActionExecutor<Input, Rank> executor = new ActionExecutor<>(ActionEvent.TAKE, action);
        addToQueue(executor);
        return this;
    }

    @Override
    public <Input> ActionProcessor map() {
        Action<Input, Void> action = new Action<Input, Void>() {
            @Override
            public Void doAction(Input data) {
                return null;
            }
        };
        ActionExecutor<Input, Void> executor = new ActionExecutor<>(ActionEvent.MAP, action);
        addToQueue(executor);
        return this;
    }

    @Override
    public ActionProcessor listenCancel(ActionCancelListener listener) {
        this.cancelListener = listener;
        return this;
    }

    @Override
    public ActionProcessor listenException(ActionExceptionListener listener) {
        this.exceptionListener = listener;
        return this;
    }

    @Override
    public void process() {
        if (index == executorList.size()) return;
        ActionExecutor actionExecutor = executorList.get(index++);
        actionExecutor.execute(result, new IObserver() {
            @Override
            public void onSuccess(ActionEvent event, Object result) {
                switch (event) {
                    case ACTION :
                        handleAction(result);
                        break;
                    case FILTER :
                        handleFilter((Boolean)result);
                        break;
                    case TAKE :
                        handleTake((Rank)result);
                        break;
                    case MAP :
                        handleMap();
                        break;
                }
            }
            @Override
            public void onFail(Exception exception) {
                notifyException(exception);
            }
            @Override
            public void onCancel() {
                notifyCancel();
            }
        });
    }

    @Override
    public void cancel() {
        for (ActionExecutor actionExecutor : executorList) {
            actionExecutor.cancel();
        }
        if (childProcessorList != null) {
            for (ActionProcessor processor : childProcessorList) {
                processor.cancel();
            }
        }
    }

    /**
     * 获取IHandler
     * @return
     */
    private IHandler getHandler() {
        return HandlerFactory.create(currentThread);
    }

    /**
     * 加入队列
     * @param executor
     */
    private void addToQueue(ActionExecutor executor) {
        delayMills = 0;
        executorList.add(executor);
    }

    /**
     * 通知异常
     * @param exception
     */
    private void notifyException(Exception exception) {
        if (exceptionListener != null) {
            exceptionListener.onException(exception);
        }
    }

    /**
     * 通知取消
     */
    private void notifyCancel() {
        if (cancelListener != null) {
            cancelListener.onCancel();
        }
    }

    /**
     * 处理 ActionEvent.ACTION
     * @param result
     */
    private void handleAction(Object result) {
        ActionProcessor.this.result = result;
        process();
    }

    /**
     * 处理 ActionEvent.FILTER
     * @param result
     */
    private void handleFilter(Boolean result) {
        if (result) {
            process();
        }
    }

    /**
     * 处理 ActionEvent.TAKE
     * @param result
     */
    private void handleTake(Rank result) {
        if (ActionProcessor.this.result instanceof List) {
            List tempResult = (List) ActionProcessor.this.result;
            ActionProcessor.this.result = tempResult.subList(result.getStart(), result.getEnd());
            process();
        } else {
            notifyException(new Exception("ActionProcessor only can 'take' List type"));
        }
    }

    /**
     * 处理 ActionEvent.MAP
     */
    private void handleMap() {
        if (result instanceof List) {
            childProcessorList = new ArrayList<>();
            List tempResult = (List) result;
            for (int i = 0; i < tempResult.size(); i++) {
                ActionProcessor processor = new ActionProcessor(executorList, exceptionListener, cancelListener, tempResult.get(i), index);
                processor.process();
                childProcessorList.add(processor);
            }
        } else {
            notifyException(new Exception("ActionProcessor only can 'map' List type"));
        }
    }

}
