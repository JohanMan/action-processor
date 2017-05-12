package com.johan.actionprocessor;

/**
 * Created by Administrator on 2017/5/12.
 */

public interface IProcessor {

    /**
     * 切换线程
     * @param thread
     * @return
     */
    ActionProcessor thread(ActionThread thread);

    /**
     * 延时
     * @param delayMills
     * @return
     */
    ActionProcessor delay(long delayMills);

    /**
     * begin
     * @param action
     * @param <Output>
     * @return
     */
    <Output> ActionProcessor begin(Action<Void, Output> action);

    /**
     * end
     * @param action
     * @param <Input>
     * @return
     */
    <Input> ActionProcessor end(Action<Input, Void> action);

    /**
     * action
     * @param action
     * @param <Input>
     * @param <Output>
     * @return
     */
    <Input, Output> ActionProcessor action(Action<Input, Output> action);

    /**
     * filter
     * @param action
     * @param <Input>
     * @return
     */
    <Input> ActionProcessor filter(Action<Input, Boolean> action);

    /**
     * take
     * @param start
     * @param end
     * @return
     */
    <Input> ActionProcessor take(int start, int end);

    /**
     * map
     * @return
     */
    <Input> ActionProcessor map();

    /**
     * 监听取消
     * @param listener
     * @return
     */
    ActionProcessor listenCancel(ActionCancelListener listener);

    /**
     * 监听取消
     * @param listener
     * @return
     */
    ActionProcessor listenException(ActionExceptionListener listener);

    /**
     * 处理
     */
    void process();

    /**
     * 取消
     */
    void cancel();

}
