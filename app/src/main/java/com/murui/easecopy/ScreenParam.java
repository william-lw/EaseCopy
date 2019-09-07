package com.murui.easecopy;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 获取屏幕参数
 *
 * @author rcm
 */
public class ScreenParam {

    public static int width;
    public static int height;
    public static int densityDpi;
    public static float density;

    private static Activity sActivity;
    private static ScreenParam sInstance;

    private ScreenParam(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        if (sActivity != null) {
            sActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            width = metric.widthPixels; // 屏幕宽度（像素）
            height = metric.heightPixels; // 屏幕高度（像素）
            density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
            densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
            Log.e("通知", "den=" + density + " denDpi=" + densityDpi + " w:" + width + " h:" + height);
        }
    }

    public synchronized static void init(Activity activity) {
        sActivity = activity;
        sInstance = new ScreenParam(sActivity);
    }

    public static ScreenParam getInstance() {
        if (sInstance == null) {
            sInstance = new ScreenParam(sActivity);
        }
        return sInstance;
    }
}
