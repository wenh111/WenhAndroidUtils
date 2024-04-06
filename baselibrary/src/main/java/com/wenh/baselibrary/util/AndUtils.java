package com.wenh.baselibrary.util;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * android工具类
 * 可以方便简单的调用android自带的一些功能
 * 例如Broadcast广播等
 * Created by MONDAY on 2016/4/11.
 */
@SuppressLint("MissingPermission")
public class AndUtils {

    private static Logger logger = Logger.getLogger(AndUtils.class);

    private static final String MY_BROAD_PERMISION = "com.gonsin.paperless.BROAD_ACTION";

    private static final String BROWER_FINISH = "com.gonsin.paperless.BROWER_FINISH";


    /*
     * 发送关闭浏览器广播
     * */
    public static void closeWindowBroadcast(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(BROWER_FINISH);
        context.sendBroadcast(intent);
    }


    /**
     * 获取当前 进程名字
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        int pid = android.os.Process.myPid();
        Iterator<ActivityManager.RunningAppProcessInfo> iterator = list.iterator();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = iterator.next();
            try {
                if (info.pid == pid) {
                    return info.processName;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return "";
    }


    /*
     * 关闭浏览器
     * */
    public static BroadcastReceiver registerCloseBrowserBroadcast(Context context, final BrowserBroadcastReceiver receiver) {
        if (receiver == null) {
            return null;
        }

        IntentFilter intentFilter = new IntentFilter(BROWER_FINISH);

        BroadcastReceiver receiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BROWER_FINISH)) {
                    receiver.onReceive();
                }
            }
        };
        context.registerReceiver(receiver1, intentFilter);
        return receiver1;

    }


    /**
     * 注销广播
     *
     * @param context
     * @param receiver
     */
    public static void unregisterBroadcast(Context context, BroadcastReceiver receiver) {
        if (context != null && receiver != null) {
            context.unregisterReceiver(receiver);
        }
    }


    public static String getProductVersion(String versionName) {
        if (!versionName.contains("S") && !versionName.contains("C")) {
            return "S";
        }
        String productSight = versionName.contains("S") ? "S" : "C";
        if (productSight.equalsIgnoreCase("S")) {
            return "S";
        }
        int sightIndex = versionName.indexOf(productSight);
        if (sightIndex <= 0) {
            return "S";
        }
        int endIndex = versionName.indexOf(".", sightIndex);
        if (endIndex <= 0) {
            return "S";
        }
        return versionName.substring(sightIndex, endIndex);
    }


    /**
     * 浏览器广播事件接收器
     */
    public interface BrowserBroadcastReceiver {

        void onReceive();
    }

    /*
     * WPS广播
     * */
    public interface WpsBroadcastReceiver {

        void onReceive(int type);
    }


    public static String getUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }


    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
//        return context.getResources().getBoolean(R.dimen.table)
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断activity是否已经被杀死
     *
     * @param activity
     * @return
     */
    public static boolean isActivityDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return activity.isDestroyed();
        }
        return activity.isFinishing();
    }


    /**
     * 获取apk版本
     *
     * @param context
     * @return
     */
    public static PackageInfo getAppVersion(Context context) {
        PackageManager manager;
        PackageInfo info = null;
        manager = context.getPackageManager();
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    /**
     * 判断是否有root权限
     *
     * @return
     */
    public static synchronized boolean isRoot() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Unexpected error - Here is what I know: " + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 设置远程调试
     */
    public static synchronized void initRemoteDebug() {
        if (isRoot()) {
            DataOutputStream os = null;
            ProcessBuilder execBuild =
                    new ProcessBuilder("/system/bin/sh", "-");
            execBuild.redirectErrorStream(true);
            Process exec = null;
            try {
                exec = execBuild.start();
            } catch (Exception e) {
                logger.error("Could not start terminal process. " + e.getMessage());
                return;
            }
            os = new DataOutputStream(exec.getOutputStream());
            try {
                os.writeBytes("su\n\r");
                os.writeBytes("setprop service.adb.tcp.port 5555\n\r");
                os.writeBytes("stop adbd\n\r");
                os.writeBytes("start adbd\n\r");
//                os.writeBytes("exit\n");
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                exec.destroy();
            }
        }

    }


    /**
     * 按键音开关
     */
    public static void setBeepSound(Context context, ContentResolver cr, boolean bBeep) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (bBeep) {
            mAudioManager.loadSoundEffects();
            Settings.System.putInt(cr, Settings.System.SOUND_EFFECTS_ENABLED, 1);
        } else {
            mAudioManager.unloadSoundEffects();
            Settings.System.putInt(cr, Settings.System.SOUND_EFFECTS_ENABLED, 0);
        }
    }

    /**
     * 获取按键音状态  0 关闭   1 开
     */
    public static int getBeepSound(ContentResolver cr) {
        int beepSound = Settings.System.getInt(cr, Settings.System.SOUND_EFFECTS_ENABLED, 0);
        return beepSound;
    }


    /**
     * 将系统音量平分为0~8
     * 根据2062的音量级别获取这个级别的当前音量
     *
     * @param context
     * @param currentVolume
     * @return
     */
    public static int getCurrentSystemVolumeLevel(
            Context context,
            int currentVolume) {

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int maxSystemVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //获取当前音量
        float volumeLevelNum = new BigDecimal(maxSystemVolume / 8.0)
                .setScale(0, BigDecimal.ROUND_HALF_UP)
                .floatValue();

        int result = Math.round(currentVolume * volumeLevelNum);
        if (result <= maxSystemVolume) {
            return result;
        } else {
            return maxSystemVolume;
        }
    }


    public static int getCurrent2062VolLevelBySystemVol(Context context, int systemVol) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int maxSystemVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //获取当前音量
        float volumeLevelNum = new BigDecimal(maxSystemVolume / 8.0)
                .setScale(0, BigDecimal.ROUND_HALF_UP)
                .floatValue();

        int result = Math.round(systemVol / volumeLevelNum);
        if (result <= maxSystemVolume) {
            return result;
        } else {
            return maxSystemVolume;
        }
    }


    /**
     * 设置当前音量
     *
     * @param context
     * @param currentVolume
     * @return
     */
    public static void setCurrentSystemVolumeLevel(
            Context context,
            int currentVolume) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int maxSystemVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //获取当前音量
        float volumeLevelNum = new BigDecimal(maxSystemVolume / 8.0)
                .setScale(0, BigDecimal.ROUND_HALF_UP)
                .floatValue();

        int result = Math.round(currentVolume * volumeLevelNum);
        if (result <= maxSystemVolume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, result, 0);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxSystemVolume, 0);
        }
    }


    /**
     * 设置当前系统通知音量
     *
     * @param context
     * @param currentVolume
     * @return
     */
    public static void setCurrentNotificationVolumeLevel(
            Context context,
            int currentVolume) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, currentVolume, 0);
    }

    public static Intent getAppOpenIntentByPackageName(Context context, String packageName) {
        String mainAct = null;
        PackageManager pkgMag = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

        @SuppressLint("WrongConstant") List<ResolveInfo> list = pkgMag.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo info = list.get(i);
            if (info.activityInfo.packageName.equals(packageName)) {
                mainAct = info.activityInfo.name;
                break;
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            return null;
        }
        intent.setComponent(new ComponentName(packageName, mainAct));
        return intent;
    }


    public static boolean isSoundMuteMode(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }

    public static boolean isSoundVibrateMode(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE;
    }

    public static boolean isSoundNormalMode(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    public static int getMaxSound(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public static int getCurrentSound(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static void setCurrentSound(Context context, int volume) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    /**
     * 关闭触摸提示音
     */
    public static void disTouchEffectAndSaveState(Context context) {
        SharedPreferences setting = context.getSharedPreferences("SystemSetting", MODE_PRIVATE);
        int mSoundEffect = setting.getInt("SOUND_EFFECTS_ENABLED", -1);

        if (mSoundEffect == -1) {
            SharedPreferences.Editor editor = setting.edit();
            mSoundEffect = Settings.System.getInt(context.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);

            editor.putInt("SOUND_EFFECTS_ENABLED", mSoundEffect);
            editor.commit();
        }


        AudioManager mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
        mAudioManager.unloadSoundEffects();
        Settings.System.putInt(context.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);
    }

    /**
     * 打开日历应用
     */
    public static void gotoCalendarApp(Context cnt) {
        try {
            Intent t_intent = new Intent(Intent.ACTION_VIEW);
            t_intent.addCategory(Intent.CATEGORY_DEFAULT);
            t_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            t_intent.setDataAndType(Uri.parse("content://com.android.calendar/"), "time/epoch");
            cnt.startActivity(t_intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断app是否在最前端
     */
    public static boolean isAppInForeground(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    public static void restartApp(final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent mPendingIntent = PendingIntent.getActivity(context, 123456, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);

            }
        }, 200);
    }
}
