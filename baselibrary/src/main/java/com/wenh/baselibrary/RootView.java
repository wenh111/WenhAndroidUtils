package com.wenh.baselibrary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewbinding.ViewBinding;

import com.wenh.baselibrary.mvpbase.BasePresenter;
import com.wenh.baselibrary.mvpbase.BaseView;
import com.wenh.baselibrary.util.ToastUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RootView implements View.OnClickListener {

    protected Context mContext;

    private BaseView mView;

    private static final int STATE_MAIN = 0x00;
    private static final int STATE_LOADING = 0x01;
    private static final int STATE_EMPTY = 0x02;
    private static final int STATE_ERROR = 0x03;

    private View viewError;
    private View viewEmpty;
    private View viewLoading;
    private ViewGroup viewMain;
    private ViewGroup mParent;

    private int mErrorResource = R.layout.view_error;
    private int mEmptyResource = R.layout.view_empty;
    private int mLoadingResource = R.layout.view_progress;

    private int currentState = STATE_LOADING;
    private boolean isErrorViewAdded = false;
    private boolean isEmptyViewAdded = false;

    BaseDialog mLoadDialog;

    private List<BasePresenter> mInjectPresenters = new ArrayList<>();

    public RootView(Context context, BaseView view) {
        this.mContext = context;
        mView = view;
    }

    public void onDestroyView() {
        if (isErrorViewAdded) {
            mParent.removeView(viewError);
            isErrorViewAdded = false;
        }
        if (isEmptyViewAdded) {
            mParent.removeView(viewEmpty);
            isEmptyViewAdded = false;
        }

        unbindPresenter();

        response();
    }

    public void onViewCreated(ViewGroup main) {
        viewMain = main;
        if (viewMain == null) return;

        if (!(viewMain.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException(
                "view_main's ParentView should be a ViewGroup.");
        }
        mParent = (ViewGroup) viewMain.getParent();
        View.inflate(mContext, mLoadingResource, mParent);
        viewLoading = mParent.findViewById(R.id.view_loading);
        viewMain.setVisibility(View.GONE);
        currentState = STATE_LOADING;
    }

    public static <V extends ViewBinding> V bindViews(Class<?> cls, LayoutInflater layoutInflater) {
        ParameterizedType type = (ParameterizedType) cls.getGenericSuperclass();
        Class c = (Class) type.getActualTypeArguments()[0];
        try {
            Method inflate = c.getDeclaredMethod("inflate", LayoutInflater.class);
            return (V) inflate.invoke(null, layoutInflater);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T extends BasePresenter> T createPresenter(ParameterizedType type) {
        if (type != null) {
            Type[] types = type.getActualTypeArguments();
            if (types.length > 1) {
                try {
                    T presenter = (T) ((Class<?>) types[1]).newInstance();
                    presenter.attachView(mView);
                    return presenter;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void bindPresenter() {
        //获得已经申明的变量，包括私有的
        Field[] fields = mView.getClass().getDeclaredFields();
        for (Field field : fields) {
            //获取变量上面的注解类型
            Presenter injectPresenter = field.getAnnotation(Presenter.class);
            if (injectPresenter != null) {
                try {
                    Class<? extends BasePresenter> type = (Class<? extends BasePresenter>) field.getType();
                    BasePresenter mInjectPresenter = type.newInstance();
                    mInjectPresenter.attachView(mView);
                    field.setAccessible(true);
                    field.set(mView, mInjectPresenter);
                    mInjectPresenters.add(mInjectPresenter);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    throw new RuntimeException("SubClass must extends Class:BasePresenter");
                }
            }
        }
    }


    public void unbindPresenter() {
        /**
         * 解绑，避免内存泄漏
         */
        for (BasePresenter presenter : mInjectPresenters) {
            presenter.detachView();
        }
        mInjectPresenters.clear();
        //mInjectPresenters = null;
    }



    public void success(String msg, Activity activity) {
        ToastUtil.showSnackBar(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0), msg, R.drawable.round_green_lightest_5dp, R.mipmap.success_green);
    }

    public void error(String msg) {
        if (viewMain == null || currentState != STATE_LOADING) return;
        if (!isErrorViewAdded) { isErrorViewAdded = true;
            View.inflate(mContext, mErrorResource, mParent);
            viewError = mParent.findViewById(R.id.view_error);
            if (viewError == null) throw new IllegalStateException(
                "A View should be named 'view_error' in ErrorLayoutResource.");
            viewError.setOnClickListener(this);
        }
        if (msg != null) ((TextView) viewError.findViewById(R.id.error_msg)).setText(msg);

        hideCurrentView();
        currentState = STATE_ERROR;
        viewError.setVisibility(View.VISIBLE);
    }


    public void empty(String msg) {
        if (viewMain == null || currentState == STATE_EMPTY) return;
        if (!isEmptyViewAdded) { isEmptyViewAdded = true;
            View.inflate(mContext, mEmptyResource, mParent);
            viewEmpty = mParent.findViewById(R.id.view_empty);
            if (viewEmpty == null) throw new IllegalStateException(
                "A View should be named 'view_error' in ErrorLayoutResource.");
            viewEmpty.setOnClickListener(this);
        }
        if (msg != null) ((TextView) viewEmpty.findViewById(R.id.empty_text)).setText(msg);

        hideCurrentView();
        currentState = STATE_EMPTY;
        viewEmpty.setVisibility(View.VISIBLE);
    }

    public void loading() {
        if (viewMain == null || currentState == STATE_LOADING)
            return;
        hideCurrentView();
        currentState = STATE_LOADING;
        viewLoading.setVisibility(View.VISIBLE);
        //progressView.getAnimation().start();
    }


    public void main() {
        if (viewMain == null || currentState == STATE_MAIN)
            return;
        hideCurrentView();
        currentState = STATE_MAIN;
        viewMain.setVisibility(View.VISIBLE);
    }

    private void hideCurrentView() {
        switch (currentState) {
            case STATE_MAIN:
                viewMain.setVisibility(View.GONE);
                break;
            case STATE_LOADING:
                viewLoading.setVisibility(View.GONE);
                break;
            case STATE_EMPTY:
                if (viewEmpty != null) viewEmpty.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                if (viewError != null) viewError.setVisibility(View.GONE);
                break;
        }
    }

    public void setErrorResource(int errorLayoutResource) {
        this.mErrorResource = errorLayoutResource;
    }

    public void setEmptyResource(int emptyResource) {
        this.mEmptyResource = emptyResource;
    }

    public void setLoadingResource(int loadingResource) {
        this.mLoadingResource = loadingResource;
    }

    private int requestNumber = 0;

    public void request() {
        requestNumber ++;
        if (mLoadDialog == null) {
            mLoadDialog = new BaseDialog(mContext) {
                @Override
                protected int getLayout() {
                    return mLoadingResource;
                }
            };
            mLoadDialog.setCanceledOnTouchOutside(false);
            mLoadDialog.setCancelable(false);
        }
        mLoadDialog.show();
    }

    public void response() {
        if (requestNumber > 0) requestNumber --;
        if (mLoadDialog != null && requestNumber == 0)
            mLoadDialog.dismiss();
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    @Override
    public void onClick(View view) {
        mView.retry();
    }
}