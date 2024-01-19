package com.food.order.system.kafka.producer.config;

import java.util.function.Consumer;
import org.springframework.kafka.support.SendResult;

public class ProducerListenerCallBack<K, V> {
    private Consumer<SendResult<K, V>> successCallback;

    public ProducerListenerCallBack(Consumer<SendResult<K, V>> successCallback,
            Consumer<Throwable> errorCallback) {
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;
    }

    private Consumer<Throwable> errorCallback;

    public Consumer<SendResult<K, V>> getSuccessCallback() {
        return successCallback;
    }

    public void setSuccessCallback(Consumer<SendResult<K, V>> successCallback) {
        this.successCallback = successCallback;
    }

    public Consumer<Throwable> getErrorCallback() {
        return errorCallback;
    }

    public void setErrorCallback(Consumer<Throwable> errorCallback) {
        this.errorCallback = errorCallback;
    }

    public void onSuccess(SendResult<K, V> result) {
        successCallback.accept(result);
    }

    public void onFailure(Throwable ex) {
        errorCallback.accept(ex);
    }
}
