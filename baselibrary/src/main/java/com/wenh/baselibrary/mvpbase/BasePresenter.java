package com.wenh.baselibrary.mvpbase;

public interface BasePresenter<T extends BaseView>{

    void attachView(T view);

    void detachView();
}
