package com.william.androidsdk.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(Context appContext, String s) {
        if (toast == null) {
            toast = Toast.makeText(appContext, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }


    public static void showToast(Context appContext, int resId) {
        showToast(appContext, appContext.getString(resId));
    }
}