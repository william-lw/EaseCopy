package com.murui.easecopy.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.murui.easecopy.R;
import com.murui.easecopy.bean.ClipboardBean;
import com.murui.easecopy.bean.ClipboardDBModel;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.william.androidsdk.utils.StringUtils;
import com.william.androidsdk.widget.armdialog.ArmDialog;
import com.william.androidsdk.widget.armdialog.IDialog;
import com.william.androidsdk.widget.armdialog.manager.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClipboardUtil {
    private static final String TAG = "ClipboardUtil";
    private static ExecutorService executorService;
    private static final int MAX_DOWNLOAD_COUNT = 3;

    static {
        executorService = Executors.newFixedThreadPool(MAX_DOWNLOAD_COUNT);
    }

    private static void postTask(Runnable runnable) {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.execute(runnable);
        }
    }

//    private static void checkExecutor() {
//        if (executorService == null) {
//            executorService = Executors.newFixedThreadPool(3);
//        }
//    }

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

    public interface GetDataDoneCallback {
        void onGetDone(List<ClipboardDBModel> list);
    }


    public static void getAllRecords(final GetDataDoneCallback callback) {
        postTask(new Runnable() {
            @Override
            public void run() {
                List<ClipboardDBModel> clipboardBeans = new Select().from(ClipboardDBModel.class).queryList();
                callback.onGetDone(clipboardBeans);
            }
        });
    }


    public static void showEditDialog(final Activity activity, final String title, final String summery, final String content,
                                      final IDialog.EditTextOkBtnClickListener positiveListener,
                                      final IDialog.OnClickListener negativeClickListener) {
        ArmDialog.Builder builder = new ArmDialog.Builder(activity);
        builder
                .setDialogView(R.layout.dialog_edit_record)
                .setBuildChildListener(new IDialog.OnBuildListener() {
                    @Override
                    public void onBuildChildView(IDialog dialog, View view, int layoutRes) {
                        initEditDialog(activity, dialog, view, title, content, summery, positiveListener, negativeClickListener);
                    }
                })
                .setWindowBackgroundP(0.5f)
                .setCancelable(false)
                .setCancelableOutSide(false)
                .setWidth((int) (ScreenUtil.getScreenWidth(activity) * 0.85f))
                .setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
                .show();
    }

    private static void initEditDialog(final Activity activity, final IDialog dialog, View view, String title, String content, String summary,
                                       final IDialog.EditTextOkBtnClickListener positiveListener,
                                       final IDialog.OnClickListener negativeClickListener) {
        TextView dialogTitle = view.findViewById(R.id.dialog_edit_title);
        final EditText summaryView = view.findViewById(R.id.dialog_clipboard_summary);
        final EditText contentView = view.findViewById(R.id.dialog_clipboard_content);
        Button cancelBtn = view.findViewById(R.id.btn_edit_cancel);
        Button commitBtn = view.findViewById(R.id.btn_edit_ok);

        dialogTitle.setText(title);
        if (!StringUtils.isNullOrEmpty(summary)) {
            summaryView.setText(summary);
        }
        if (!StringUtils.isNullOrEmpty(content)) {
            contentView.setText(content);
        }

//        summaryView.setFocusableInTouchMode(true);
//        summaryView.setFocusable(true);
//        summaryView.requestFocus();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                negativeClickListener.onClick(dialog);
            }
        });
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sum = summaryView.getText().toString();
                String con = contentView.getText().toString();
                if (StringUtils.isNullOrEmpty(sum)) {
                    Toast.makeText(activity, "请输入概述", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(con)) {
                    Toast.makeText(activity, "请输入具体文本内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] texts = {sum, con};
                positiveListener.onClick(dialog, texts);
            }
        });

    }


    public static List<ClipboardBean> copyToClipBoardBean(List<ClipboardDBModel> list){
        List<ClipboardBean> result = new ArrayList<>();
        if (list == null || list.size() ==0) {
            return result;
        }
        for (int i = 0; i < list.size(); i++) {
            ClipboardBean bean = new ClipboardBean();
            ClipboardDBModel model = list.get(i);
            bean.setClipContent(model.getClipContent());
            bean.setDateTxt(model.getDateTxt());
            bean.setDateLong(model.getDateLong());
            bean.setDescription(model.getDescription());
            bean.setTag(model.getTag());
            result.add(bean);
        }
        return result;
    }
}
