package com.wenh.baselibrary.network;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author Wenh·Wong
 * @Date 2024/4/6 8:40
 */
public abstract class BaseObserver<T> implements Observer<T> {
    /**
     * 获取数据成功
     */
    @Override
    public void onNext(T t) {

    }

    /**
     * 完成
     */
    @Override
    public void onComplete() {

    }

    /**
     * 获取数据失败
     *
     * @param e the exception encountered by the Observable
     */
    @Override
    public void onError(Throwable e) {

    }

    /**
     * 订阅
     *
     * @param d the subscription object
     */
    @Override
    public void onSubscribe(Disposable d) {

    }


}
