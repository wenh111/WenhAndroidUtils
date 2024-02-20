package com.wenh.baselibrary.mqtt;

public interface SubscribeCallback<T> {

    void onSubscribe(String name, T result);

}
