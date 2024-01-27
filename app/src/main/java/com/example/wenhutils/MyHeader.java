package com.example.wenhutils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeRefresh;


public class MyHeader extends RelativeLayout implements SmartSwipeRefresh.SmartSwipeRefreshHeader {
    public TextView mTitleTextView;
    public ImageView mProgressImageView;
    public int mStrResId;
    public ObjectAnimator animator;

    public MyHeader(Context context) {
        super(context);
        if (isInEditMode()) {
            onInit(false);
        }
    }

    public MyHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            onInit(false);
        }
    }

    public MyHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            onInit(false);

        }
    }

    public MyHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (isInEditMode()) {
            onInit(false);
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onInit(boolean horizontal) {

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        LayoutInflater.from(getContext()).inflate(R.layout.my_header, this);
        if (layoutParams == null) {

            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        setLayoutParams(layoutParams);
        /*Drawable background = getBackground();
        if (background == null) {
            setBackgroundColor(0xFFEEEEEE);
        }*/
        mProgressImageView = findViewById(R.id.ssr_classics_progress);
        mProgressImageView.setVisibility(GONE);
        mTitleTextView = findViewById(R.id.ssr_classics_title);
        mTitleTextView.setText("下拉刷新");
        animator = ObjectAnimator.ofFloat(mProgressImageView, "rotation", 0, 3600);
        animator.setDuration(5000);
        animator.setInterpolator(null);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);

    }

    public void cancelAnimation() {
        animator.cancel();
        mProgressImageView.setVisibility(GONE);
    }

    public void showAnimation() {
        animator.start();
        mProgressImageView.setVisibility(VISIBLE);
    }

    @Override
    public void onStartDragging() {

    }

    @Override
    public void onProgress(boolean dragging, float progress) {
        if (dragging) {
            setText(progress >= 1 ? "释放刷新" : "下拉刷新");
        } else if (progress <= 0) {
            cancelAnimation();
        }
    }

    @Override
    public long onFinish(boolean success) {
        cancelAnimation();
        setText(success ? TimeUtil.time(TimeUtil.MM_DD_HH_MM) : "刷新失败");
        return 500;
    }

    @Override
    public void onReset() {
        //修复了:先下拉刷新完成(此时显示为刷新完成),然后调用 startRefresh 时,看到的不是"下拉刷新"而是"刷新完成"的问题.
        //fix:first pull to refresh finished(hint:Refresh Success),then invoke startRefresh,now will allways see the 'Refresh Success'.so reset the text.
        setText("下拉刷新");
    }

    @Override
    public void onDataLoading() {
        showAnimation();
        setText("正在加载");
    }


    public void setText(String str) {
        if (mTitleTextView != null) {
            mTitleTextView.setText(str);

        }
    }
}
