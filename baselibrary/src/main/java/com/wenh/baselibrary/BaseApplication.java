package com.wenh.baselibrary;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.wenh.baselibrary.log4j.LoggerInit;

/**
 * @Author Wenh·Wong
 * @Date 2024/2/26 9:32
 */
public class BaseApplication extends Application {
    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Base.init(this);
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();


    }


}
