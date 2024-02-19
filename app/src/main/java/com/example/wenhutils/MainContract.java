package com.example.wenhutils;

import com.wenh.baselibrary.mvpbase.BasePresenter;
import com.wenh.baselibrary.mvpbase.BaseView;

public interface MainContract {

    interface View extends BaseView {
        void changeFragment();

    }

    interface Presenter extends BasePresenter<View> {
        void init();

    }
}