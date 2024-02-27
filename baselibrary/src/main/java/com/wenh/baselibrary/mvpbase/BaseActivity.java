package com.wenh.baselibrary.mvpbase;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewbinding.ViewBinding;

import com.wenh.baselibrary.RootView;
import com.wenh.baselibrary.acitvity.SimpleActivity;
import com.wenh.baselibrary.util.ToastUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivity<V extends ViewBinding, T extends BasePresenter> extends SimpleActivity<V> implements BaseView {

    protected T mPresenter;

    private RootView mRootView;

    @Override
    protected void onViewCreated() {

        super.onViewCreated();
        inject();
        //if (mPresenter != null)
        //    mPresenter.attachView(this);
        //通过获得泛型类的父类，拿到泛型的接口类实例，通过反射来实例化 presenter
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType)
            mPresenter = viewRoot().createPresenter((ParameterizedType)type);
        viewRoot().bindPresenter();

    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();

        viewRoot().onDestroyView();

        super.onDestroy();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void message(String msg) {
        ToastUtil.showSnackBar(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), msg);
    }


    @Override
    public void success(String msg) {
        viewRoot().success(msg, this);
    }

    @Override
    public void notice(String msg) {
        message(msg);
    }

    @Override
    public void fail(String msg) {
        message(msg);
    }

    @Override
    public void request() {
        viewRoot().request();
    }

    @Override
    public void response() {
        viewRoot().response();
    }

    @Override
    public int getRequestNumber() {
        return viewRoot().getRequestNumber();
    }

    protected RootView viewRoot() {
        if (mRootView == null)
            mRootView = new RootView(this, this);
        return mRootView;
    }


    @Override
    public void useNightMode(boolean isNight) {
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }

    @Override
    public void error() {}

    @Override
    public void error(String msg) {}

    @Override
    public void empty() {}

    @Override
    public void empty(String msg) {}

    @Override
    public void loading() {}

    @Override
    public void main() {}

    @Override
    public void retry() {}

    protected void inject() {}
}