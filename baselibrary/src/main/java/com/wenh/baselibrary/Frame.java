package com.wenh.baselibrary;

import android.app.Activity;
import android.content.Context;

import java.util.HashSet;
import java.util.Set;

public class Frame {

    public static final String TAG = "Frame";

    //Base实例
    private static Frame INSTANCE = new Frame();
    //程序的Context对象
    private Context mContext;

    /** 保证只有一个Base实例 */
    private Frame() {}

    /** 获取Base实例 ,单例模式 */
    public static Frame getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        getInstance().mContext = context;
        Use.init(context);
    }


    public static Context getContext() {
        return getInstance().mContext;
    }


    public static String getString(int resId) {
        return getContext().getResources().getString(resId);
    }

    public static String getString(int resId, Object... formatArgs) {
        return getContext().getResources().getString(resId, formatArgs);
    }


    private long mServerTimeOffset;

    public static long getServerTime() {
        return getInstance().mServerTimeOffset + System.currentTimeMillis();
    }

    public static void setServerTime(long serverTime) {
        getInstance().mServerTimeOffset = serverTime - System.currentTimeMillis();
    }


    private Set<Activity> allActivities = new HashSet<>();


    public void addActivity(Activity act) {
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        allActivities.remove(act);
    }

    public boolean hasActivity(Class<?> cls) {
        for (Activity act : allActivities) {
            if (act.getClass().equals(cls)) return true;
        }
        return false;
    }

    public void retainActivity(Class<?>... cls) {
        for (Activity act : allActivities) {
            boolean retain = false;
            for(Class<?> c : cls) {
                if (act.getClass().equals(c)) { retain = true;  break; }
            }
            if (!retain) act.finish();
        }
    }

    public void exit() {
        synchronized (allActivities) {
            for (Activity act : allActivities) {
                act.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
