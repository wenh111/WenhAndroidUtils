package com.wenh.baselibrary;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

public class BaseWindow extends Service implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {


    //创建浮动窗口设置布局参数的对象
    WindowManager windowManager;
    WindowManager.LayoutParams wmParams;

    //定义浮动窗口布局
    FrameLayout mFloatWindow;
    private View rootView;

    private int windowFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN;

    private boolean fullScreen;
    protected boolean draggable = true;

    protected int windowX;
    protected int windowY;

    @Override
    public void onCreate() {
        super.onCreate();
        initDetector();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public View getView() {
        return rootView;
    }

    public FrameLayout getFloatWindow() {
        return mFloatWindow;
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public void setContentView(int res) {
        setContentView(res, windowX, windowY,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, Gravity.LEFT);
    }

    /**
     * 创建悬浮窗
     */
    public void setContentView(int res, int x, int y, int gravity) {
        setContentView(res, x, y, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, gravity);
    }

    /**
     * 创建悬浮窗
     */
    public void setContentView(int res, int x, int y, int width, int height, int gravity) {

        //获取浮动窗口视图所在布局
        LayoutInflater inflater = LayoutInflater.from(this);

        rootView = inflater.inflate(res, null);

        setContentView(rootView, x, y, width, height, gravity);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void setContentView(View view, int x, int y, int width, int height, int gravity) {

        fullScreen = width == WindowManager.LayoutParams.MATCH_PARENT;

        rootView = view;
        mFloatWindow = new FrameLayout(this);

        mFloatWindow.addView(rootView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        mFloatWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchWindow(event);
            }
        });

        wmParams = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(WINDOW_SERVICE);
        //设置window type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }

        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = windowFlags;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = gravity;

        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = x;
        wmParams.y = y;

        //设置悬浮窗口长宽数据
        wmParams.width = width;
        wmParams.height = height;

        //添加mFloatLayout
        windowManager.addView(mFloatWindow, wmParams);
    }

    public WindowManager.LayoutParams getWmParams() {
        return wmParams;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void updateWindowLayout(WindowManager.LayoutParams wmParams) {
        windowManager.updateViewLayout(mFloatWindow, wmParams);
    }


    /**
     * setContentView前调用
     * 设置的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
     */
    public void setNotTouchableFlags() {
        windowFlags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
    }

    /**
     * 置顶
     */
    public void topLayer() {
        windowManager.removeView(mFloatWindow);
        windowManager.addView(mFloatWindow, wmParams);
    }

    /**
     * 设置窗口无移动动画
     */
    public void setWindowNoMoveAnimation() {
        String className = "android.view.WindowManager$LayoutParams";
        try {
            Class layoutParamsClass = Class.forName(className);

            Field privateFlags = layoutParamsClass.getField("privateFlags");
            Field noAnim = layoutParamsClass.getField("PRIVATE_FLAG_NO_MOVE_ANIMATION");

            int privateFlagsValue = privateFlags.getInt(getWmParams());
            int noAnimFlag = noAnim.getInt(getWmParams());
            privateFlagsValue |= noAnimFlag;

            privateFlags.setInt(getWmParams(), privateFlagsValue);

            // Dynamically do stuff with this class
            // List constructors, fields, methods, etc.

        } catch (ClassNotFoundException e) {
            // Class not found!
        } catch (Exception e) {
            // Unknown exception
        }
    }


    public void moveTo(int x, int y) {
        if (wmParams != null) {
            wmParams.x = x;
            wmParams.y = y;
            lastX = x;
            lastY = y;
            wmParams.gravity = Gravity.LEFT | Gravity.TOP;
            windowManager.updateViewLayout(mFloatWindow, wmParams);
        }
    }


    //最小化
    public void minimum() {
        if (wmParams != null) {
            fullScreen = false;
            wmParams.width = Viewer.getScreenWidth(this) / 4;
            wmParams.height = Viewer.getScreenHeight(this) / 4;
            wmParams.x = getLastX();
            wmParams.y = getLastY();
            windowManager.updateViewLayout(mFloatWindow, wmParams);
            onWindowMinimum();
        }
    }

    //最大化
    public void maximum() {
        if (wmParams != null) {
            fullScreen = true;
            wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            wmParams.x = 0;
            wmParams.y = 0;
            windowManager.updateViewLayout(mFloatWindow, wmParams);
            onWindowMaximum();
        }
    }

    //最小化监听
    public void onWindowMinimum() {
    }

    //最大化监听
    public void onWindowMaximum() {
    }


    private int lastX = 100;
    private int lastY = 100;

    public int getLastX() {
        return lastX;
    }

    public void setLastX(int lastX) {
        this.lastX = lastX;
    }

    public int getLastY() {
        return lastY;
    }

    public void setLastY(int lastY) {
        this.lastY = lastY;
    }


    protected GestureDetector gestureDetector;
    protected ScaleGestureDetector scaleGestureDetector;
    private boolean zoom;           // 是否正在缩放
    private float firstFocusX;      // 开始缩放时的焦点
    private float firstFocusY;      // 开始缩放时的焦点
    private float firstSpan;      // 开始缩放时手指距离


    void initDetector() {
        gestureDetector = new GestureDetector(this, this);
        scaleGestureDetector = new ScaleGestureDetector(this, this);
    }


    protected boolean onTouchWindow(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            onTouchUp();
        }
        boolean touched = false;
        if (!zoom) {
            touched = gestureDetector.onTouchEvent(event);
        }
        if (!touched) {
            touched = scaleGestureDetector.onTouchEvent(event);
        }
        return true;
    }


    protected void onTouchUp() {
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        zoom = true;
        firstFocusX = detector.getFocusX();
        firstFocusY = detector.getFocusY();
        firstSpan = detector.getCurrentSpan();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        zoom = false;

    }

    public <T extends View> T findViewById(int id) {
        return rootView.findViewById(id);
    }

    /**
     * 关闭悬浮窗
     */
    public void finish() {
        stopSelf();
    }

    /**
     * 双击手势
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /**
     * 拖动手势
     *
     * @param e1 第一次触摸是的触摸事件
     * @param e2 每次的触摸事件
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        // 只在小窗口的时候进行拖动
        if (fullScreen || !draggable) {
            return false;
        }

        moveTo(
                (int) e2.getRawX() - (int) e1.getX(),
                (int) e2.getRawY() - (int) e1.getY()
        );
        return true;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatWindow != null) {
            windowManager.removeView(mFloatWindow);
        }
    }


}
