package com.wenh.baselibrary;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;


public class NoticeUtil {

    public static int TYPE_NOTICE = 0;
    public static int TYPE_SUCCESS = 1;
    public static int TYPE_WARNING = 2;
    public static int TYPE_ERROR = 3;

    static Handler handler = new Handler(Looper.getMainLooper());


    WindowManager windowManager;
    WindowManager.LayoutParams wmParams;


    private Context mContext;

    private static NoticeUtil notice;

    LinearLayout container;
    int containerId = View.generateViewId();

    public static NoticeUtil ins() {
        return ins(Base.getContext());
    }
    public static NoticeUtil ins(Context context) {
        if (notice == null) notice = new NoticeUtil(context);
        return notice;
    }

    public NoticeUtil(Context context) {
        mContext = context;
        window(context);
    }

    public NoticeUtil(Activity activity) {
        this.mContext = activity;

        container = new LinearLayout(activity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        container.setGravity(Gravity.CENTER_HORIZONTAL);
        container.setLayoutParams(layoutParams);
        container.setId(containerId);
        container.setOrientation(LinearLayout.VERTICAL);
        handler.post(() -> { activity.addContentView(container, layoutParams); });
    }


    private void window(Context context) {

        wmParams = new WindowManager.LayoutParams();
        windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        //设置window type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }

        wmParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;


        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        //wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.TOP;

        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        //wmParams.x = 0;
        //wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        container = new LinearLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(layoutParams);
        container.setId(containerId);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setGravity(Gravity.CENTER_HORIZONTAL);

        //添加mFloatLayout
        windowManager.addView(container, wmParams);
    }


    public void notice(String msg) {
        message(msg, TYPE_NOTICE);
    }

    public void success(String msg) {
        message(msg, TYPE_SUCCESS);
    }

    public void warning(String msg) {
        message(msg, TYPE_WARNING);
    }

    public void error(String msg) {
        message(msg, TYPE_ERROR);
        //Logger.stackTracePrint();
    }


    public synchronized void message(String msg, int type) {

        View layout = LayoutInflater.from(mContext).inflate(R.layout.view_notice, null);
        layout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        if (type == TYPE_SUCCESS) ((ImageView)layout.findViewById(R.id.notice_icon)).setImageResource(R.mipmap.icon_success);
        Animation animationUp = AnimationUtils.loadAnimation(mContext, R.anim.in_alpha_to_bottom);

        TextView noticeText = layout.findViewById(R.id.notice_text);
        noticeText.setText(msg);

        handler.post(() -> {
            int maxCount = windowManager == null ? 7 : 3;
            if (container.getChildCount() > maxCount) removeView(container.getChildAt(0));

            container.addView(layout);
            layout.startAnimation(animationUp);
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //layout.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.out_alpha_to_top));
                //container.removeView(layout);
                removeView(layout);
            }
        }, 3000);
    }


    private synchronized void removeView(View view) {
        TypeEvaluator<ViewGroup.LayoutParams> evaluator = new HeightEvaluator(view);

        ViewGroup.LayoutParams start = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, view.getMeasuredHeight());
        ViewGroup.LayoutParams end = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        ValueAnimator animator = ObjectAnimator.ofObject(view, "layoutParams", evaluator, start, end);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                container.removeView(view);
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        AnimatorSet set = new AnimatorSet();
        set.play(animator);
        set.setDuration(200);
        set.start();

    }


    class HeightEvaluator implements TypeEvaluator<ViewGroup.LayoutParams> {

        View view;

        public HeightEvaluator(View view) {
            this.view = view;
        }

        @Override
        public ViewGroup.LayoutParams evaluate(float fraction, ViewGroup.LayoutParams startValue, ViewGroup.LayoutParams endValue) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = (int) (startValue.height + fraction * (endValue.height - startValue.height));
            return params;
        }
    }




    public static void showSnackBar(Dialog layout, String msg) {
        showSnackBar(((ViewGroup) layout.findViewById(android.R.id.content)).getChildAt(0), msg, 0, 0);
    }

    public static void showSnackBar(View view, String msg) {
        showSnackBar(view, msg, 0, 0);
    }

    public static void showSnackBar(View view, String msg, int bgRes, int iconRes) {

        TSnackbar snackBar = TSnackbar.make(view, "", TSnackbar.LENGTH_LONG);

        View snack_view = snackBar.getView();


        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snack_view.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        snack_view.setLayoutParams(params);

        snack_view.setBackgroundColor(snack_view.getContext().getResources().getColor(android.R.color.transparent));

        TSnackbar.SnackbarLayout snack_Layout = (TSnackbar.SnackbarLayout) snack_view;//将获取的View转换成SnackbarLayout


        View add_view = LayoutInflater.from(snack_view.getContext()).inflate(R.layout.view_notice, null);//加载布局文件新建View

        if (bgRes > 0) add_view.setBackground(snack_view.getContext().getDrawable(bgRes));

        if (iconRes > 0) ((ImageView) add_view.findViewById(R.id.notice_icon)).setImageResource(iconRes);
        ((TextView) add_view.findViewById(R.id.notice_text)).setText(msg);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);//设置新建布局参数

        p.gravity = Gravity.CENTER;//设置新建布局在Snackbar内垂直居中显示

        snack_Layout.addView(add_view, 0, p);//将新建布局添加进snackbarLayout相应位置


        snackBar.show();
    }

    public static void showShortSnackBar(View view, String msg) {
        Snackbar.make(view, msg, BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}
