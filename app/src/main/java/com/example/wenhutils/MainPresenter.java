package com.example.wenhutils;

import com.wenh.baselibrary.mvpbase.BasePresenter;
import com.wenh.baselibrary.mvpbase.RxPresenter;

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {
    @Override
    public void init() {
        mView.changeFragment();
    }

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
