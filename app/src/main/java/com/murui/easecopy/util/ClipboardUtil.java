package com.murui.easecopy.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ClipboardUtil {
    private static final String TAG = "ClipboardUtil";


    public static List<String> getRecentClipboardTexts(Context context) {
        List<String> list = new ArrayList<>();
        ClipboardManager clipBoard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipBoard.hasPrimaryClip()) {
            ClipData primaryClip = clipBoard.getPrimaryClip();
            int itemCount = primaryClip.getItemCount();
            for (int i = 0; i < itemCount; i++) {
                ClipData.Item itemAt = primaryClip.getItemAt(i);
                CharSequence text = itemAt.getText();
                list.add(text.toString());
            }
            return list;
        } else {
            Log.d(TAG, "getRecentClipboardTexts: ====null=====");
            return list;
        }
    }

    public static void copyTextToClipboard(Context context, String text) {
        ClipboardManager clipBoard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, text);
        if (clipBoard != null) {
            clipBoard.setPrimaryClip(clipData);
        }
    }


}
