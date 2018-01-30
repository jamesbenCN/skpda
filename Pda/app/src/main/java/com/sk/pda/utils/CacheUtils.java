package com.sk.pda.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class CacheUtils {

    private static FileOutputStream fos;
    private static ByteArrayOutputStream baos;
    private static FileInputStream fis;


    public static boolean getBoolean(Context context,String spname, String key) {
        SharedPreferences sp = context.getSharedPreferences(spname, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void putBoolean(Context context,String spname,  String key, boolean b) {
        SharedPreferences sp = context.getSharedPreferences(spname, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, b).commit();

    }

    public static void putString(Context context,String spname,  String key, String value) {

        SharedPreferences sp = context.getSharedPreferences(spname, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context mContext,String spname,  String key) {
       String result = "";
        SharedPreferences sp = mContext.getSharedPreferences(spname, Context.MODE_PRIVATE);
        result = sp.getString(key, "");
        return result;
    }
}
