package com.example.wenhcommomadapter.base;


import android.view.View;

import com.example.wenhcommomadapter.ViewHolder;


/**
 * Created by zhy on 16/6/22.
 */
public interface ItemViewDelegate<T>
{

    public abstract int getItemViewLayoutId();

    public abstract boolean isForViewType(T item, int position);

    public abstract void convert(ViewHolder holder, T t, int position);



}
