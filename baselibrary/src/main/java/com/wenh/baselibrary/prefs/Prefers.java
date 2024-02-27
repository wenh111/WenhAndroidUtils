package com.wenh.baselibrary.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.wenh.baselibrary.Frame;


/**
 * @author: Est <codeest.dev@gmail.com>
 * @date: 2017/4/21
 * @description:
 */

public class Prefers {

    public static final String LOGIN_BG = "LOGIN_BG";
    public static final String LOGIN_NAME_SIZE = "LOGIN_NAME_SIZE";

    public static final String STREAM_SOFT_DECODE = "STREAM_SOFT_DECODE";

    public static final String SUBTITLE_ENABLE = "SUBTITLE_ENABLE";
    public static final String SUBTITLE_LOCK = "SUBTITLE_LOCK";
    public static final String SUBTITLE_Y = "SUBTITLE_Y";
    public static final String SUBTITLE_TEXT_SIZE = "SUBTITLE_TEXT_SIZE";
    public static final String SUBTITLE_TEXT_COLOR = "SUBTITLE_TEXT_COLOR";
    public static final String SUBTITLE_LINES = "SUBTITLE_LINES";
    public static final String SUBTITLE_BG_TRANSPARENT = "SUBTITLE_BG_TRANSPARENT";
    public static final String SUBTITLE_TRANSLATED_TEXT = "SUBTITLE_TRANSLATED_TEXT";

    public static final String SESSION_ID = "session_id";
    public static final String ACCOUNT_KEY = "SP_ACCOUNT_KEY";
    public static final String USER_KEY = "SP_USER_KEY";
    public static final String USER_NAME = "SP_USER_NAME";
    public static final String USER_INFO_KEY = "SP_USER_INFO_KEY";
    public static final String ROOM_KEY = "SP_ROOM_KEY";
    public static final String ROOM_NAME = "SP_ROOM_NAME";
    public static final String MEETING_KEY = "meeting_key";
    public static final String UNIT_ID = "unit_id";
    public static final String SEAT_NUMBER = "seat_number";
    public static final String LANGUAGE = "language";

    public static final String SWITCHABLE_ROOM = "switchable_room";
    public static final String ENABLE_LOG_WINDOW = "enable_log_window";
    public static final String WINDOW_LOG_INTERVAL = "window_log_interval";
    public static final String CRASH_LOG = "CRASH_LOG";
    public static final String CRASH_LOG_UPLOADED = "CRASH_LOG_UPLOADED";
    public static final String MAIN_SCREEN_SWITCH = "main_screen_switch";
    public static final String DISPLAY_SECONDARY_SCREEN = "display_secondary_screen";
    public static final String CONTROL_ACCESS = "control_access";
    public static final String SHOW_SIDEBAR = "SHOW_SIDE_BAR";
    public static final String SHOW_FAST_VOTE = "SHOW_FAST_VOTE";
    public static final String SHOW_STREAM_MEDIA = "SHOW_STREAM_MEDIA";
    public static final String SHOW_USER_FILE = "SHOW_USER_FILE";
    public static final String SHOW_DISK_FILE = "SHOW_DISK_FILE";
    public static final String SHOW_CALL_SERVICE = "SHOW_CALL_SERVICE";
    public static final String SHOW_DRAWING_BOARD = "SHOW_DRAWING_BOARD";
    public static final String SHOW_MIC_SUBTITLES = "SHOW_MIC_SUBTITLES";
    public static final String SHOW_SYSTEM_APPS = "SHOW_SYSTEM_APPS";
    public static final String MICROPHONE_CONTROL = "microphone_control";
    public static final String TRANSLATION_CHANNEL_CONTROL = "translation_channel_control";


    public static final String USE_IMAGE_CODE = "USE_IMAGE_CODE";


    public static final String LANGUAGE_CHINESE = "language_chinese";
    public static final String DISPLAY_QR_CODE = "display_qr_code";

    public static final String CAMERA_RESOLUTION = "CAMERA_RESOLUTION";

    public static final String INITIATOR_RECORD_SOUND = "INITIATOR_RECORD_SOUND";
    public static final String RECEIVER_PLAY_SOUND = "RECEIVER_PLAY_SOUND";
    public static final String SETTING_PASSWORD = "SETTING_PASSWORD";


    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";

    //===================================================================================

    private static final String SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES";

    private final SharedPreferences mSPrefs;

    private static final Prefers INSTANCE = new Prefers();

    public static Prefers ins() {
        return INSTANCE;
    }

    public Prefers() {
        mSPrefs = Frame.getContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }


    public static SharedPreferences prefer() {
        return ins().mSPrefs;
    }

    public static void putBoolean(String key, Boolean value) {
        prefer().edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return prefer().getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return prefer().getBoolean(key, defValue);
    }

    public static void putInt(String key, int value) {
        prefer().edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return prefer().getInt(key, 0);
    }

    public static int getInt(String key, int defValue) {
        return prefer().getInt(key, defValue);
    }

    public static void putFloat(String key, float value) {
        prefer().edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key) {
        return prefer().getFloat(key, 0);
    }

    public static float getFloat(String key, float defValue) {
        return prefer().getFloat(key, defValue);
    }

    public static String getString(String key, String defValue) {
        return prefer().getString(key, defValue);
    }

    public static void putString(String key, String value) {
        prefer().edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        return getString(key, "");
    }


}
