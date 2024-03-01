package com.wenh.baselibrary;

import android.content.Context;

import com.gonsin.i18n.lib.T;
import com.gonsin.i18n.lib.config.TConfig;


public class Base {

    public static final String TAG = "Base";

    //Base实例
    private static Base INSTANCE = new Base();
    //程序的Context对象
    private Context mContext;

    /**
     * 保证只有一个Base实例
     */
    private Base() {
    }

    /**
     * 获取Base实例 ,单例模式
     */
    public static Base getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        Frame.init(context);
        getInstance().mContext = context;
//        getInstance().initI18nLanguage();
    }

    private void initI18nLanguage() {
        // 在应用中尽早初始化字符串资源的服务器和端口，并且拉取服务器上的字符串资源。
        TConfig config = new TConfig();
        config.setProject("venus_door");
        config.setIp(Mods.prefers().serviceAddress());
        config.setPort(Integer.parseInt(Mods.prefers().httpPort()));
        config.setEncoder("utf8");
        config.setKey("nusTIdVQFhQs2lrz");
        /*
         * 调试模式
         * debug = true的情况下，会启用调试模式，调试模式会每1分钟就会向服务器请求字符串，
         * 用于前端实时去修改当前显示的字符串
         *
         * 该效果需要配合  TextObserver才会有效，每次字符串有修改都会触发 TextObserver的 applyText 方法
         */
        config.setDebug(false);
        T.config(config);
        T.setLang(Mods.prefers().language());
    }


    public static Context getContext() {
        return getInstance().mContext;
    }


    public static String getString(int resId) {
        if (Mods.prefers().language().equals(Constants.CHINESE))
            return getContext().getResources().getString(resId);
        else return T.t(getContext(), resId);
    }

    public static String getString(int resId, Object... formatArgs) {
        if (Mods.prefers().language().equals(Constants.CHINESE))
            return getContext().getResources().getString(resId, formatArgs);
        else {
            try {
                return T.t(getContext(), resId, formatArgs);
            } catch (Throwable e) {
                return getContext().getResources().getString(resId, formatArgs);
            }
        }
    }

    private long mServerTimeOffset;

    public static long getServerTime() {
        return getInstance().mServerTimeOffset + System.currentTimeMillis();
    }

    public static void setServerTime(long serverTime) {
        getInstance().mServerTimeOffset = serverTime - System.currentTimeMillis();
    }

    private String apiUrl;

    public static String getApiUrl() {
        return getInstance().apiUrl;
    }

    public static void setApiUrl(String url) {
        getInstance().apiUrl = url;
    }
}