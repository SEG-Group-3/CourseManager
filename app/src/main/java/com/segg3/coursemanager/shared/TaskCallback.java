package com.segg3.coursemanager.shared;

import android.util.Pair;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TaskCallback<T> {
    private Consumer<Exception> onFail;
    private Consumer<T> onSucc;
    private BiConsumer<T, Exception> onComplete;
    private boolean isComplete = false;
    private Pair<T, Exception> lazyResult;


    public TaskCallback<T> onFailure(Consumer<Exception> func) {
        if (lazyResult != null && lazyResult.second != null)
            func.accept(lazyResult.second);
        onFail = func;
        return this;
    }

    public TaskCallback<T> onSuccess(Consumer<T> func) {
        if (lazyResult != null && lazyResult.first != null)
            func.accept(lazyResult.first);
        onSucc = func;
        return this;
    }

    public TaskCallback<T> onResult(BiConsumer<T, Exception> consumer) {
        if (lazyResult != null)
            consumer.accept(lazyResult.first, lazyResult.second);
        onComplete = consumer;
        return this;
    }

    public void lazyError(Exception e) {
        this.lazyResult = new Pair<>(null, e);
    }

    public void lazyAccept(T value) {
        this.lazyResult = new Pair<>(value, null);
    }

    public void clearLazyAccept() {
        this.lazyResult = null;
    }

    public void accept(T value) {
        if (onSucc != null)
            onSucc.accept(value);
        if (onComplete != null)
            onComplete.accept(value, null);
        isComplete = true;
    }

    public void error(Exception value) {
        if (onFail != null)
            onFail.accept(value);
        if (onComplete != null)
            onComplete.accept(null, value);
        isComplete = true;
    }

    public void join() {
        while (!isComplete) ;
    }
}
