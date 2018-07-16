package com.appsinfinity.fingercricket.interfaces;

/**
 * Created by macbook on 5/15/15.
 */
public interface ControllerInterface<T> {
    public void onTaskStarted();
    public void onTaskFinished(T t);
}
