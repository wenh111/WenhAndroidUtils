package com.wenh.baselibrary.LoggerWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogRecord {

    public static final String TAG = "LogRecord";
    private static LogRecord INSTANCE = new LogRecord();
    private HashMap<Integer, Long> lastTime = new HashMap();
    private List<Logger.LogInfo> recordList = new ArrayList();
    private Logger.Callback callback;

    private LogRecord() {
    }

    public static LogRecord getInstance() {
        return INSTANCE;
    }

    public void log(String string) {
        if (Logger.getInstance().showDefault)
            getInstance().add(string, 0);
    }

    public void log(String string, int tag) {
        getInstance().add(string, tag);
    }

    private synchronized void add(String string, int tag) {
        Long last = tag == 0 ? null : this.lastTime.get(tag);
        long current = System.currentTimeMillis();
        if (last == null || current - last > (tag < 1000 ? Logger.getInstance().showInterval : tag)) {
            this.recordList.add(0, new Logger.LogInfo(string, current));
            this.lastTime.put(tag, current);
            if (this.recordList.size() > Logger.getInstance().maxSize) {
                this.recordList.remove(this.recordList.size() - 1);
            }

            if (this.callback != null) {
                this.callback.onCallback(this.recordList.get(0));
            }
        }
    }

    public void setCallback(Logger.Callback callback) {
        this.callback = callback;
    }

    public List<Logger.LogInfo> getRecordList() {
        return this.recordList;
    }

}