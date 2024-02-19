package com.example.wenhutils;

import com.wenh.baselibrary.log4j.LoggerInit;
import com.wenh.baselibrary.mvpbase.BasePresenter;
import com.wenh.baselibrary.mvpbase.RxPresenter;

import org.apache.log4j.Logger;

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {
    @Override
    public void init() {
        mView.changeFragment();
        LoggerInit.init(mView.getActivity(), mView.getActivity().getPackageName());
        Logger logger = Logger.getLogger(MainPresenter.class);
        logger.info("myinit");
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
