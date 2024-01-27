package com.example.hwhutillibrary;

import android.content.Context;

import com.gonsin.i18n.lib.T;

public class StringUtils {
    private Context context;

    public StringUtils(Context context) {
        this.context = context;
    }

    public String getString(int id) {
        return context.getString(id);
    }

    public String getI18NString(int id) {
        return T.t(context, id);
    }
    /*public String getI18NString(String string) {
        return T.();

    }*/
//    public String getI18NFormatString(int id, String... args) {
//
//    }

}
