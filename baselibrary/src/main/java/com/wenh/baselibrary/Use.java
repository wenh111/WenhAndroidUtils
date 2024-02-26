package com.wenh.baselibrary;

import android.content.Context;

import com.wenh.baselibrary.log4j.LoggerInit;

public class Use {

    public static final String TAG = "Use";

    //Use实例
    private static Use INSTANCE = new Use();
    //程序的Context对象
    private Context mContext;

    /** 保证只有一个Base实例 */
    private Use() {}

    /** 获取Base实例 ,单例模式 */
    public static Use instance() {
        return INSTANCE;
    }

    /**
     * 初始化
     * @param context
     */
    public static void init(Context context) {
        instance().mContext = context;
        LoggerInit.init(context, context.getPackageName());
    }


    public static Context getContext() {
        return instance().mContext;
    }


    public static String getString(int resId) {
        return getContext().getResources().getString(resId);
    }

    public static String getString(int resId, Object... formatArgs) {
        return getContext().getResources().getString(resId, formatArgs);
    }

    public static float getDimension(int id) {
        return getContext().getResources().getDimension(id);
    }

    public static int getIdentifier(String name, String type) {
        return getContext().getResources().getIdentifier(name, type, getContext().getPackageName());
    }


    private long mServerTimeOffset;

    public static long getServerTime() {
        return instance().mServerTimeOffset + System.currentTimeMillis();
    }

    public static void setServerTime(long serverTime) {
        instance().mServerTimeOffset = serverTime - System.currentTimeMillis();
    }


}
