package com.william.androidsdk.widget.armdialog.manager;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.william.androidsdk.R;
import com.william.androidsdk.utils.StringUtils;
import com.william.androidsdk.widget.armdialog.ArmDialog;
import com.william.androidsdk.widget.armdialog.IDialog;

import java.util.HashMap;

/**
 * Created by lw on 2019/1/10 下午2:21
 */

public class DialogUtil {

    /**
     * @param context               Context
     * @param title                 标题
     * @param content               内容
     * @param btn1Str               button文字
     * @param positiveClickListener 点击事件
     * @param dismissListener       对话框消失的监听
     */
    public static void createDefaultDialog(Context context, String title, String content, boolean cancelable, String btn1Str,
                                           IDialog.OnClickListener positiveClickListener, IDialog.OnDismissListener dismissListener) {
        createDefaultDialog(context, title, content, cancelable, btn1Str, positiveClickListener, "", null, dismissListener);
    }

    /**
     * @param context               Context
     * @param title                 标题
     * @param content               内容
     * @param btn1Str               button文字
     * @param positiveClickListener 点击事件
     * @param dismissListener       对话框消失的监听
     */
    public static void createDefaultDialog(Context context, int title, int content, boolean cancelable, int btn1Str,
                                           IDialog.OnClickListener positiveClickListener, IDialog.OnDismissListener dismissListener) {
        String titleText = context.getString(title);
        String contentText = context.getString(content);
        String btn1Text = context.getString(btn1Str);
        createDefaultDialog(context, titleText, contentText, cancelable, btn1Text, positiveClickListener, "", null, dismissListener);
    }


    /**
     * @param context               Context
     * @param title                 标题
     * @param content               内容
     * @param btn1Str               左边按钮
     * @param negativeClickListener 左边点击事件
     * @param btn2Str               右边按钮
     * @param positiveClickListener 右边点击事件
     * @param dismissListener       消失监听
     */
    public static void createDefaultDialog(Context context, int title, int content, boolean cancelable, int btn1Str,
                                           IDialog.OnClickListener positiveClickListener, int btn2Str, IDialog.OnClickListener negativeClickListener,
                                           IDialog.OnDismissListener dismissListener) {
        String titleText = context.getString(title);
        String contentText = context.getString(content);
        String btn1Text = context.getString(btn1Str);
        String btn2Text = context.getString(btn2Str);
        createDefaultDialog(context, titleText, contentText, cancelable, btn1Text, positiveClickListener, btn2Text, negativeClickListener, dismissListener);
    }


    /**
     * @param context               Context
     * @param title                 标题
     * @param content               内容
     * @param btn1Str               左边按钮
     * @param negativeClickListener 左边点击事件
     * @param btn2Str               右边按钮
     * @param positiveClickListener 右边点击事件
     * @param dismissListener       消失监听
     */
    public static void createDefaultDialog(Context context, String title, String content, boolean cancelable, String btn1Str,
                                           IDialog.OnClickListener positiveClickListener, String btn2Str, IDialog.OnClickListener negativeClickListener,
                                           IDialog.OnDismissListener dismissListener) {
        ArmDialog.Builder builder = new ArmDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(content)) {
            builder.setContent(content);
        }
        if (positiveClickListener != null) {
            if (TextUtils.isEmpty(btn1Str)) {
                builder.setPositiveButton(positiveClickListener);
            } else {
                builder.setPositiveButton(btn1Str, positiveClickListener);
            }
        }
        if (negativeClickListener != null) {
            if (TextUtils.isEmpty(btn2Str)) {
                builder.setNegativeButton(negativeClickListener);
            } else {
                builder.setNegativeButton(btn2Str, negativeClickListener);
            }
        }
        if (dismissListener != null) {
            builder.setOnDismissListener(dismissListener);
        }
        builder.setCancelableOutSide(cancelable);
        builder.setCancelable(cancelable);
        builder.show();
    }

    private static HashMap<String, ArmDialog> loadingHashMap = new HashMap<>();
    private static HashMap<String, ArmDialog> imageTextHashMap = new HashMap<>();


    /**
     * 创建Loading dialog
     *
     * @param context Context
     */
    public static void createLoadingDialog(Context context, final int loadingText) {
        String string = context.getString(loadingText);
        createLoadingDialog(context, string);
    }

    /**
     * 创建Loading dialog
     *
     * @param context Context
     */
    public static void createLoadingDialog(Context context, final String loadingText) {
        closeLoadingDialog(context);
        ArmDialog.Builder builder = new ArmDialog.Builder(context);
        ArmDialog dialog = builder.setDialogView(R.layout.dialog_loading)
                .setBuildChildListener(new IDialog.OnBuildListener() {
                    @Override
                    public void onBuildChildView(IDialog dialog, View view, int layoutRes) {
                        TextView message = view.findViewById(R.id.txt_msg);
                        message.setText(loadingText);
                    }
                })
                .setWindowBackgroundP(0.5f)
                .setCancelableOutSide(false)
                .setCancelable(false)
                .show();
        loadingHashMap.put(context.getClass().getSimpleName(), dialog);
    }

    /**
     * 关闭loading dialog
     *
     * @param context Context
     */
    public static void closeLoadingDialog(Context context) {
        String dialogKey = context.getClass().getSimpleName();
        ArmDialog dialog = loadingHashMap.get(dialogKey);
        if (dialog != null) {
            loadingHashMap.remove(dialogKey);
            dialog.dismiss();
        }
    }


    public interface EditTextOkBtnClickListener {
        void onClick(IDialog dialog, String editText);
    }

    /**
     * @param activity
     * @param title            标题，可为空，为空则不显示
     * @param content          对话框内容，可为空，为空则不显示
     * @param cancelable
     * @param positiveListener 输入完成，点击确认后的回调
     */
    public static void createEditDialog(Activity activity, @Nullable int title, @Nullable int content, int positiveButtonText,
                                        int hintText, boolean cancelable,
                                        EditTextOkBtnClickListener positiveListener, IDialog.OnClickListener negativeClickListener) {
        String t = activity.getString(title);
        String c = activity.getString(content);
        String btn = activity.getString(positiveButtonText);
        String hint = activity.getString(hintText);
        createEditDialog(activity, t, c, btn, hint, cancelable, positiveListener, negativeClickListener);
    }


    /**
     * @param activity
     * @param title            标题，可为空，为空则不显示
     * @param content          对话框内容，可为空，为空则不显示
     * @param cancelable
     * @param positiveListener 输入完成，点击确认后的回调
     */
    public static void createEditDialog(Activity activity, @Nullable final String title, @Nullable final String content, final String positiveButtonText,
                                        final String hintText, boolean cancelable,
                                        final EditTextOkBtnClickListener positiveListener, final IDialog.OnClickListener negativeClickListener) {
        closeLoadingDialog(activity);
        ArmDialog.Builder builder = new ArmDialog.Builder(activity);
        ArmDialog dialog = builder.setDialogView(R.layout.dialog_edit)
                .setBuildChildListener(new IDialog.OnBuildListener() {
                    @Override
                    public void onBuildChildView(IDialog dialog, View view, int layoutRes) {
                        initEditDialog(dialog, view, title, content, positiveButtonText, hintText, positiveListener, negativeClickListener);
                    }
                })
                .setWindowBackgroundP(0.5f)
                .setCancelable(cancelable)
                .setCancelableOutSide(cancelable)
                .setWidth((int) (ScreenUtil.getScreenWidth(activity) * 0.85f))
                .setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
                .show();
    }

    private static void initEditDialog(final IDialog dialog, View view, String title, String content, String positiveButtonText, final String hintText,
                                       final EditTextOkBtnClickListener positiveListener, final IDialog.OnClickListener negativeClickListener) {
        TextView titleView = view.findViewById(R.id.dialog_edit_title);
        View dividingLine = view.findViewById(R.id.v_edit_dividing);
        TextView contentView = view.findViewById(R.id.dialog_edit_content);
        final EditText editText = view.findViewById(R.id.dialog_edit_text);
        Button btnCancel = view.findViewById(R.id.btn_edit_cancel);
        Button btnOk = view.findViewById(R.id.btn_edit_ok);
        if (!StringUtils.isNullOrEmpty(title)) {
            titleView.setText(title);
        } else {
            titleView.setVisibility(View.GONE);
            dividingLine.setVisibility(View.GONE);
        }

        if (!StringUtils.isNullOrEmpty(hintText)) {
            editText.setHint(hintText);
        }

        if (!StringUtils.isNullOrEmpty(content)) {
            contentView.setText(content);
        } else {
            contentView.setVisibility(View.GONE);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negativeClickListener.onClick(dialog);
            }
        });

        btnOk.setText(positiveButtonText);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positiveListener.onClick(dialog, editText.getText().toString());
            }
        });
    }

    /**
     * 创建图片，文字 dialog
     * 用来作为警告，或者展示性的对话框
     *
     * @param context Context
     */
    public static void createImageTextDialog(Context context, final int imageId, final int loadingText, boolean cancelable) {
        String string = context.getString(loadingText);
        createImageTextDialog(context, imageId, string, cancelable);
    }

    /**
     * 创建图片，文字 dialog
     * 用来作为警告，或者展示性的对话框
     *
     * @param context Context
     */
    public static void createImageTextDialog(Context context, final int imageId, final String loadingText, boolean cancelable) {
        closeImageTextDialog(context);
        final String dialogKey = context.getClass().getSimpleName();
        ArmDialog.Builder builder = new ArmDialog.Builder(context);
        ArmDialog dialog = builder
                .setDialogView(R.layout.dialog_image_text)
                .setBuildChildListener(new IDialog.OnBuildListener() {
                    @Override
                    public void onBuildChildView(IDialog dialog, View view, int layoutRes) {
                        TextView message = view.findViewById(R.id.txt_msg);
                        if (!StringUtils.isNullOrEmpty(loadingText)) {
                            message.setText(loadingText);
                        } else {
                            message.setVisibility(View.GONE);
                        }

                        ImageView image = view.findViewById(R.id.img_dialog_image);
                        if (imageId != 0) {
                            image.setImageResource(imageId);
                        } else {
                            image.setVisibility(View.GONE);
                        }
                    }
                })
                .setOnDismissListener(new IDialog.OnDismissListener() {
                    @Override
                    public void onDismiss(IDialog dialog) {
                        if (imageTextHashMap.size() > 0 && imageTextHashMap.containsKey(dialogKey)) {
                            imageTextHashMap.remove(dialogKey);
                        }
                    }
                })
                .setWindowBackgroundP(0.5f)
                .setCancelableOutSide(cancelable)
                .setCancelable(cancelable)
                .show();
        imageTextHashMap.put(dialogKey, dialog);
    }

    /**
     * 关闭loading dialog
     *
     * @param context Context
     */
    public static void closeImageTextDialog(Context context) {
        String dialogKey = context.getClass().getSimpleName();
        ArmDialog dialog = imageTextHashMap.get(dialogKey);
        if (dialog != null && imageTextHashMap.containsKey(dialogKey)) {
            imageTextHashMap.remove(dialogKey);
            dialog.dismiss();
        }
    }
}
