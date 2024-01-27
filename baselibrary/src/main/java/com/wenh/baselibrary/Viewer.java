package com.wenh.baselibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class Viewer {

    public static int dip2px(double dpValue) {
        return dip2px(Use.getContext(), dpValue);
    }

    public static int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    public static int px2dp(float pxValue) {
        final float scale = Use.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenHeightFix() {
        WindowManager wm = (WindowManager) Use.getContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        @SuppressLint("InternalInsetResource") int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void click(View.OnClickListener listener, View... views) {
        for (View view : views) {
            if (view != null) view.setOnClickListener(listener);
        }
    }

    public static boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static void visible(boolean visible, View... views) {
        if (visible) show(views);
        else hide(views);
    }
    public static void show(View... views) {
        for (View view : views) {
            if (view != null) view.setVisibility(View.VISIBLE);
        }
    }

    public static void hide(View... views) {
        for (View view : views) {
            if (view != null) view.setVisibility(View.GONE);
        }
    }

    public static void enable(boolean enable, View... views) {
        if (enable) enable(views);
        else disable(views);
    }

    public static void enable(View... views) {
        for (View view : views) {
            if (view != null) view.setEnabled(true);
        }
    }

    public static void disable(View... views) {
        for (View view : views) {
            if (view != null) view.setEnabled(false);
        }
    }

    public static void setMarginTop(View view, int margin) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        lp.topMargin = margin;
        view.setLayoutParams(lp);
    }

    public static void setMarginBottom(View view, int margin) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        lp.bottomMargin = margin;
        view.setLayoutParams(lp);
    }

    public static void setMarginLeft(View view, int margin) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        lp.leftMargin = margin;
        view.setLayoutParams(lp);
    }

    public static void setMarginRight(View view, int margin) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        lp.rightMargin = margin;
        view.setLayoutParams(lp);
    }

    public static void setMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp == null) lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = left;
        lp.topMargin = top;
        lp.rightMargin = right;
        lp.bottomMargin = bottom;
        view.setLayoutParams(lp);
    }


    public static void setWidth(View view, int width) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        view.setLayoutParams(lp);
    }

    public static void setHeight(View view, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);
    }

    public static void setWidthHeight(View view, int width, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }

    public static int getTextWidth(TextView textView, String text) {
        Paint paint = new Paint();
        paint.set(textView.getPaint());
        return (int)Math.ceil(paint.measureText(text));
    }
}
