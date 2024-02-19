package com.wenh.baselibrary;

import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

import com.wenh.baselibrary.mvpbase.BaseFragment;
import com.wenh.baselibrary.mvpbase.BasePresenter;

public abstract class RootFragment<V extends ViewBinding, T extends BasePresenter> extends BaseFragment<V, T> {

    private ViewGroup viewMain;

    @Override
    protected void onViewCreated() {
        if (getView() == null)
            return;

        super.onViewCreated();

        viewMain = (ViewGroup) getView().findViewById(R.id.view_main);
        viewRoot().onViewCreated(viewMain);
    }

    @Override
    public void error(String msg) {
        viewRoot().error(msg);
    }

    @Override
    public void error() {
        error(null);
    }

    @Override
    public void empty(String msg) {
        viewRoot().empty(msg);
    }

    @Override
    public void empty() {
        empty(null);
    }

    @Override
    public void loading() {
        viewRoot().loading();
    }

    @Override
    public void main() {
        viewRoot().main();
    }


    public void setErrorResource(int errorLayoutResource) {
        viewRoot().setErrorResource(errorLayoutResource);
    }

    public void setEmptyResource(int emptyResource) {
        viewRoot().setEmptyResource(emptyResource);
    }

    public void setLoadingResource(int loadingResource) {
        viewRoot().setLoadingResource(loadingResource);
    }
}