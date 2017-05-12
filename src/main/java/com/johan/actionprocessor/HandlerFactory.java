package com.johan.actionprocessor;

/**
 * Created by Administrator on 2017/5/11.
 */

public class HandlerFactory {

    public static IHandler create(ActionThread thread) {
        IHandler handler = null;
        switch (thread) {
            case MAIN :
                handler = MainHandler.create();
                break;
            case BACKGROUND :
                handler = BackgroundHandler.create();
                break;
        }
        return handler;
    }

}
