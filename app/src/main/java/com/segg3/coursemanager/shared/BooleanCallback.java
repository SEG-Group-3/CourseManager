package com.segg3.coursemanager.shared;

import java.util.function.Consumer;

public class BooleanCallback {
    private Runnable onFalseConsumer;
    private Runnable onTrueConsumer;
    private Consumer<Boolean> onResult;

    public BooleanCallback onTrue(Runnable func) {
        onTrueConsumer = func;
        return this;
    }

    public BooleanCallback onFalse(Runnable func) {
        onFalseConsumer = func;
        return this;
    }

    public BooleanCallback onResult(Consumer<Boolean> consumer) {
        onResult = consumer;
        return this;
    }


    public void set(boolean value) {
        if (onResult != null)
            onResult.accept(value);
        if (value) {
            if (onTrueConsumer != null)
                onTrueConsumer.run();
        } else {
            if (onFalseConsumer != null)
                onFalseConsumer.run();
        }
    }


}
