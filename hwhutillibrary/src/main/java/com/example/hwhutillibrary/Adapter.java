package com.example.hwhutillibrary;

import android.content.Context;

import com.example.hwhutillibrary.bean.UDisKBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class Adapter extends CommonAdapter<UDisKBean> {
    public Adapter(Context context, int layoutId, List<UDisKBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, UDisKBean uDisKBean, int position) {

    }
}
