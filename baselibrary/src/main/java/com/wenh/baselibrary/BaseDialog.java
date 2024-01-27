package com.wenh.baselibrary;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseDialog extends Dialog {

    protected Context mContext;

    protected WindowManager.LayoutParams mLayoutParams;

    protected View mView;

    public WindowManager.LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context, 1f, Gravity.CENTER);
    }

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context, 1f, Gravity.CENTER);
    }

    public BaseDialog(Context context) {
        super(context);
        initView(context, 1f, Gravity.CENTER);
    }

    /**
     * @param context
     * @param alpha   透明度 0.0f--1f(不透明)
     * @param gravity 方向(Gravity.BOTTOM,Gravity.TOP,Gravity.LEFT,Gravity.RIGHT)
     */
    public BaseDialog(Context context, float alpha, int gravity) {
        super(context);
        initView(context, alpha, gravity);
    }


    private void initView(Context context, float alpha, int gravity) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.getWindow().setDimAmount(0f);
        mContext = context;
        Window window = this.getWindow();
        mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = alpha;
        window.setAttributes(mLayoutParams);
        if (mLayoutParams != null) {
            mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLayoutParams.gravity = gravity;
        }

        mView = LayoutInflater.from(getContext()).inflate(getLayout(), null);

        setContentView(mView);
    }


    /**
     * 隐藏头部导航栏状态栏
     */
    public void skipTools() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置全屏显示
     */
    public void setFullScreen() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }

    /**
     * 设置宽度match_parent
     */
    public void setFullScreenWidth() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    /**
     * 设置高度为match_parent
     */
    public void setFullScreenHeight() {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }

    public void setOnWhole() {
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    protected abstract int getLayout();
}
