package com.wenh.baselibrary.fragement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public abstract class SimpleFragment <V extends ViewBinding> extends SupportFragment {

    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    protected V views;
    protected boolean initialled = false;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class cls = (Class) type.getActualTypeArguments()[0];
        try {
            Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            views = (V) inflate.invoke(null, inflater, container, false);
        }  catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        mView = views.getRoot();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onViewCreated();

        if (isDirect()) {
            initialled = true;
            initial();
        }
    }

    protected void onViewCreated() {}

    protected boolean isDirect() {
        return true;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (!isDirect()) {
            initialled = true;
            initial();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        views = null;
    }

    protected abstract void initial();
}
