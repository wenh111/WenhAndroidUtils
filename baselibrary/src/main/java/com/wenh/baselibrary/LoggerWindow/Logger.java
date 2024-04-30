package com.wenh.baselibrary.LoggerWindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.util.List;

public final class Logger {

    public int showInterval = 2000;
    public int maxSize = 200;
    public boolean showDefault = true;

    public static final String TAG = "Logger";
    private static final Logger INSTANCE = new Logger();
    private Context mContext;

    private Logger() {}

    /**
     * 初始化
     * @param context
     */
    public static void init(Context context) {
        getInstance().mContext = context;
    }

    public static Logger getInstance() {
        return INSTANCE;
    }

    public static Context getContext() {
        return getInstance().mContext;
    }



    public void setCallback(Callback callback) {
        LogRecord.getInstance().setCallback(callback);
    }

    public List<LogInfo> getRecordList() {
        return LogRecord.getInstance().getRecordList();
    }

    public interface Callback {
        void onCallback(LogInfo log);
    }

    public static class LogInfo {
        private long time;
        private String content;

        public LogInfo(String content, long time) {
            this.content = content;
            this.time = time;
        }

        public long getTime() {
            return this.time;
        }

        public String getContent() {
            return this.content;
        }
    }


    public static void window() {
        //LogWindow.open(getContext());
    }


    public static void i(final Object contents) {
        String log = LogUtil.i(TAG, contents);
        LogRecord.getInstance().log(log);
    }

    public static void i(final String tag, final Object... contents) {
        String log = LogUtil.i(tag, contents);
        LogRecord.getInstance().log(log);
    }

    public static void w(final Object contents) {
        String log = LogUtil.w(TAG, contents);
        LogRecord.getInstance().log(log);
    }

    public static void w(final String tag, final Object... contents) {
        String log = LogUtil.w(tag, contents);
        LogRecord.getInstance().log(log);
    }

    public static void e(final Object contents) {
        String log = LogUtil.e(TAG, contents);
        LogRecord.getInstance().log(log);
    }

    public static void e(final String tag, final Object... contents) {
        String log = LogUtil.e(tag, contents);
        LogRecord.getInstance().log(log);
    }

    public static void json(String contents) {
        String log = LogUtil.json(TAG, contents);
        LogRecord.getInstance().log(log);
    }

    public static void i(int tag, final Object contents) {
        String log = LogUtil.i(TAG, contents);
        LogRecord.getInstance().log(log, tag);
    }

    public static void w(int tag, Object contents) {
        String log = LogUtil.w(TAG, contents);
        LogRecord.getInstance().log(log, tag);
    }

    public static void e(int tag, Object contents) {
        String log = LogUtil.e(TAG, contents);
        LogRecord.getInstance().log(log, tag);
    }

    public static void json(int tag, String contents) {
        String log = LogUtil.json(TAG, contents);
        LogRecord.getInstance().log(log, tag);
    }

    public static void stack() {
        warning(TAG, Log.getStackTraceString(new Throwable()));
    }

    @Deprecated
    public static void stackTracePrint() {
        warning(TAG, Log.getStackTraceString(new Throwable()));
    }

    public static void write(Object content) {
        LogRecord.getInstance().log(toString(content));
    }

    public static void write(int tag, Object content) {
        LogRecord.getInstance().log(toString(content), tag);
    }

    public static void info(Object content) {
        Log.i(logClassName(), toString(content));
        write(content);
    }

    public static void info(String tag, Object content) {
        Log.i(tag, toString(content));
        write(content);
    }

    public static void info(int tag, Object content) {
        Log.i(logClassName(), toString(content));
        write(tag, content);
    }

    public static void warning(Object content) {
        Log.w(logClassName(), toString(content));
        write(content);
    }

    public static void warning(String tag, Object content) {
        Log.w(tag, toString(content));
        write(content);
    }

    public static void warning(int tag, Object content) {
        Log.w(logClassName(), toString(content));
        write(tag, content);
    }

    public static void error(Object content) {
        Log.e(logClassName(), toString(content));
        write(content);
    }

    public static void error(String tag, Object content) {
        Log.e(tag, toString(content));
        write(content);
    }

    public static void error(int tag, Object content) {
        Log.e(logClassName(), toString(content));
        write(tag, content);
    }

    private static String toString(Object object) {
        return object == null ? "null" : object.toString();
    }

    @SuppressLint("DefaultLocale")
    private static String logClassName() {
        StackTraceElement targetElement = new Throwable().getStackTrace()[2];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1];
        }
        if (className.contains("$")) {
            className = className.split("\\$")[0];
        }
        return String.format("%s.%s(%d)", className, targetElement.getMethodName(), targetElement.getLineNumber());
    }
}
