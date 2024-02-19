package com.wenh.baselibrary.mvpbase;

import android.app.Activity;

public interface BaseView {

    Activity getActivity();

    void message(String msg);

    void success(String msg);

    void notice(String msg);

    void fail(String msg);

    void request();

    void response();

    int getRequestNumber();

    void useNightMode(boolean isNight);

    //=======  State  =======
    void error(String msg);
    void error();

    void empty(String msg);
    void empty();

    void loading();

    void main();

    void retry();
}