package com.example.jizhangben.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "user_token";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_PHONE = "phone";

    public static void saveToken(Context context, String token, String phone){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_PHONE, phone)
                .apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(KEY_TOKEN, null);
    }

    public static String getPhone(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(KEY_PHONE, null);
    }

    public static boolean isLoggedIn(Context context) {
        return getToken(context) != null;
    }

    public static void clearToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
