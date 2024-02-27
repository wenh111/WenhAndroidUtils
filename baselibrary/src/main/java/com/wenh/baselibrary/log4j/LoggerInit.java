package com.wenh.baselibrary.log4j;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.wenh.baselibrary.util.TimeUtil;

import org.apache.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LoggerInit {
    public static void init(Context context) {
        LogConfigurator logConfigurator = new LogConfigurator();
        String[] permissions = {
                Permission.MANAGE_EXTERNAL_STORAGE//管理外部存储
        };
        XXPermissions.with(context)
                .permission(permissions)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (allGranted) {
                            File loggerFile = new File(logFile());
                            File loggerPath = loggerFile.getParentFile();
                            if (loggerPath != null && !loggerPath.exists()) {
                                loggerPath.mkdirs();
                            }
                            if (!loggerFile.exists()) {
                                try {
                                    loggerFile.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            logConfigurator.setFileName(logFile());
                            logConfigurator.setRootLevel(Level.INFO);
                            logConfigurator.setUseFileAppender(true);
                            logConfigurator.setMaxFileSize(1024 * 1024 * 1024);
                            logConfigurator.setLevel(context.getPackageName(), Level.INFO);
                            logConfigurator.configure();
                        }

                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        /*if (doNotAskAgain) {

                        } else {

                        }*/
                    }

                });


    }

    public static String logFile() {
        String loggerFileName = TimeUtil.format(System.currentTimeMillis(), TimeUtil.YYYYMMDDHHMM) + ".txt";
        String logPath = staticCachePath() + File.separator + "logs";
        String s = logPath + File.separator + loggerFileName;
        System.out.println("logFile:" + s);
        return s;
    }

    public static String staticCachePath() {
        return Environment.getExternalStorageDirectory().getPath()
                + File.separator
                + "GONSIN_LOG";
    }

}
