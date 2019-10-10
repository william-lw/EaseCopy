package com.william.androidsdk.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class LogInFileUtil {
    private static String PATH_LOGCAT;
    private static File file;

    public static File init(Context context) {
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
//            PATH_LOGCAT = Environment.getExternalStorageDirectory()
//                    .getAbsolutePath() + File.separator + "app-storelog";
//        } else {

        // 如果SD卡不存在，就保存到本应用的目录下
        PATH_LOGCAT = context.getFilesDir().getAbsolutePath()
                + File.separator + "app-storelog";
//        }

        file = new File(PATH_LOGCAT);
        Log.d("tag", "init: ==========" + PATH_LOGCAT);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    public static void logInFile(String tag, String msg) {
        msg = DateTimeUtil.formatDate(new Date()) + " : " + msg + "\n";
        Log.d(tag, msg);
        if (file.exists()) {
            FileUtils.appendContentToFile(msg, file);
        }
    }
}
