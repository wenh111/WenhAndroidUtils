package com.wenh.baselibrary.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import com.billy.android.swipe.R;
import com.billy.android.swipe.SmartSwipeRefresh;

/**
 * classic footer for {@link SmartSwipeRefresh}
 *
 * @author billy.qi
 */
public class SmartFooter extends SmartHeader implements SmartSwipeRefresh.SmartSwipeRefreshFooter {

    public boolean mNoMoreData;

    public SmartFooter(Context context) {
        super(context);
    }

    public SmartFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SmartFooter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onProgress(boolean dragging, float progress) {
        if (mNoMoreData) {
            cancelAnimation();
            return;
        }
        if (dragging) {
            setText(progress >= 1 ? context.getString(R.string.ssr_footer_release) : context.getString(R.string.ssr_footer_pulling));
        } else if (progress <= 0) {
            cancelAnimation();
        }
    }

    @Override
    public long onFinish(boolean success) {
        cancelAnimation();
        if (!mNoMoreData) {
            setText(success ? context.getString(R.string.ssr_footer_finish) : context.getString(R.string.ssr_footer_failed));
        }
        return 500;
    }

    @Override
    public void onDataLoading() {
        if (!mNoMoreData) {
            showAnimation();
            setText(context.getString(R.string.ssr_footer_refreshing));
        }
    }


    @Override
    public void setNoMoreData(boolean noMoreData) {
        this.mNoMoreData = noMoreData;
        if (noMoreData)
            setText(context.getString(R.string.ssr_footer_no_more_data));
    }
}
