package com.wenh.baselibrary.LoggerWindow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MaxLinearLayout extends LinearLayout {

    private float maxWidth;
    private float maxHeight;

    public MaxLinearLayout(Context context) {
        super(context, null);
    }

    public MaxLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
    }

    //传入参数widthMeasureSpec、heightMeasureSpec
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽度的模式和尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (maxWidth > 0 && widthSize > maxWidth) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) maxWidth, MeasureSpec.EXACTLY);
        }

        if (maxHeight > 0 && heightSize > maxHeight) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) maxHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
