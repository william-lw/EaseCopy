package com.william.androidsdk.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;


import java.io.File;
import java.util.List;

/**
 * Created by lw on 2018/7/28.
 */

public class AppInfoUtils {

    public static AppInfo getAppInfoByFile(PackageManager packageManager, File item) {
        AppInfo appInfo = null;
        try {
            appInfo = new AppInfo();
            String path = item.getAbsolutePath();
            PackageInfo packageInfo = getPackageInfo(packageManager, path);
            if (packageInfo == null) {
                return null;
            }
            appInfo.setIconId(getAppIcon(packageManager, path));
            appInfo.setFileName(getAppLabel(packageManager, path).toString());
            appInfo.setFileSize(item.length());
            appInfo.setFilePath(item.getAbsolutePath());
            appInfo.setInstalled(isApplicationAvilible(packageManager, packageInfo.packageName));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return appInfo;
    }

    /**
     * 判断手机是否安装某个应用
     * @param appPackageName  应用包名
     * @return   true：安装，false：未安装
     */
    public static boolean isApplicationAvilible(PackageManager pm, String appPackageName) {
        List<PackageInfo> pinfo = pm.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                PackageInfo packageInfo = pinfo.get(i);
                if (packageInfo != null) {
                    String pn = packageInfo.packageName;
                    if (appPackageName.equals(pn)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static Drawable getAppIcon(PackageManager pm, String apkFilepath) {
        PackageInfo pkgInfo = getPackageInfo(pm, apkFilepath);
        if (pkgInfo == null) {
            return null;
        }

        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            appInfo.sourceDir = apkFilepath;
            appInfo.publicSourceDir = apkFilepath;
        }
        return pm.getApplicationIcon(appInfo);
    }

    //得到PackageInfo对象，其中包含了该apk包含的activity和service
    public static PackageInfo getPackageInfo(PackageManager pm, String apkFilepath) {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        } catch (Exception e) {
            // should be something wrong with parse
            e.printStackTrace();
        }
        return pkgInfo;
    }

    private static CharSequence getAppLabel(PackageManager pm, String apkFilepath) {
        PackageInfo pkgInfo = getPackageInfo(pm, apkFilepath);
        if (pkgInfo == null) {
            return " ";
        }
        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            appInfo.sourceDir = apkFilepath;
            appInfo.publicSourceDir = apkFilepath;
        }

        return pm.getApplicationLabel(appInfo);
    }

    public static String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return String.valueOf(packageInfo.getLongVersionCode());
            } else {
                return String.valueOf(packageInfo.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
