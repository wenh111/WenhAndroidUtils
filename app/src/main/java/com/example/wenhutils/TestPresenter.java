package com.example.wenhutils;

import com.wenh.baselibrary.mvpbase.RxPresenter;

public class TestPresenter extends RxPresenter<TestContract.View> implements TestContract.Presenter {
    @Override
    public void attachView(TestContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void loadData() {
        mView.showData("MyTestPresenter");
    }
}
