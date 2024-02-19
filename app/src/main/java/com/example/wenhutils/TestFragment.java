package com.example.wenhutils;

import com.example.wenhutils.databinding.FragmentTestBinding;
import com.wenh.baselibrary.fragement.SimpleFragment;
import com.wenh.baselibrary.mvpbase.BaseFragment;


public class TestFragment extends BaseFragment<FragmentTestBinding, TestPresenter> implements TestContract.View {


    @Override
    protected void initial() {
        mPresenter.loadData();
    }

    @Override
    public void showData(String data) {
        views.textView.setText(data);
    }

    @Override
    public void empty(String msg) {

    }
}