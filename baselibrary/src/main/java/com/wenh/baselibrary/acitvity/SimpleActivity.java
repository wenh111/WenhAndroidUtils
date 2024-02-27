package com.wenh.baselibrary.acitvity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.wenh.baselibrary.Frame;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public abstract class SimpleActivity <V extends ViewBinding> extends SupportActivity {

    protected Activity mContext;
    protected V views;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        com.gonsin.i18n.lib.T.initFactory(this);
        super.onCreate(savedInstanceState);
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class cls = (Class) type.getActualTypeArguments()[0];
        try {
            Method inflate = cls.getDeclaredMethod("inflate", LayoutInflater.class);
            views = (V) inflate.invoke(null, getLayoutInflater());
            setContentView(views.getRoot());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        setContentView(views.getRoot());
        mContext = this;
        onViewCreated();
        Frame.getInstance().addActivity(this);
        initial();
    }


    protected void onViewCreated() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Frame.getInstance().removeActivity(this);
    }

    protected abstract void initial();
}
