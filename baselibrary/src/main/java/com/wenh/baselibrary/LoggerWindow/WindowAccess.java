package com.wenh.baselibrary.LoggerWindow;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

public class WindowAccess {

    public static int REQUEST_SYSTEM_ALERT = 100;      // 请求悬浮窗返回

    /**
     * 检查悬浮窗权限
     */
    public static boolean check(Activity activity, boolean isRequest){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!checkPermission(activity)) {
                if (isRequest) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivityForResult(intent, REQUEST_SYSTEM_ALERT);
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 悬浮窗权限判断
     * @param context 上下文
     * @return [ true, 有权限 ][ false, 无权限 ]
     */
    public static boolean checkPermission(Context context) {
        Boolean hasPermission = false;
        if (Build.VERSION.SDK_INT < 19) {
            hasPermission = true;
        } else if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()
                    || RomUtils.checkIsMeizuRom()
                    || RomUtils.checkIsHuaweiRom()
                    || RomUtils.checkIs360Rom()) {// 特殊机型
                hasPermission = opPermissionCheck(context, 24);
            } else {// 其他机型
                hasPermission = true;
            }
        } else if (Build.VERSION.SDK_INT >= 23) {// 6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
            hasPermission = highVersionPermissionCheck(context);
        }
        return hasPermission;
    }


    /**
     * [19-23]之间版本通过[AppOpsManager]的权限判断
     * @param context 上下文
     * @param op
     * @return [ true, 有权限 ][ false, 无权限 ]
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean opPermissionCheck(Context context, int op) {
        try {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class clazz = AppOpsManager.class;
            Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
            return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Android 6.0 版本及之后的权限判断
     *
     * @param context 上下文
     * @return [ true, 有权限 ][ false, 无权限 ]
     */
    private static boolean highVersionPermissionCheck(Context context) {
        if (RomUtils.checkIsMeizuRom()) {// 魅族6.0的系统单独适配
            return opPermissionCheck(context, 24);
        }
        try {
            Class clazz = Settings.class;
            Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
            return (Boolean) canDrawOverlays.invoke(null, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}