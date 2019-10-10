package com.murui.easecopy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.murui.easecopy.bean.ClipboardBean;
import com.murui.easecopy.bean.ClipboardDBModel;
import com.murui.easecopy.bean.ClipboardDBModel_Table;
import com.murui.easecopy.util.ClipboardUtil;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.william.androidsdk.utils.DateTimeUtil;
import com.william.androidsdk.widget.armdialog.IDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnClipBoardItemClickListener {
    private final String TAG = "MainActivity";

    private TextView textView;
    private RecyclerView contentRecyclerView;
    private ClipContentAdapter adapter;
    private FloatingActionMenu actionMenu;
    private boolean showedFloatMenu = false;
    private FloatingActionButton toggleFloatWindow;
    private int REQUEST_DIALOG_PERMISSION = 23;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler(getMainLooper());
        ScreenParam.getInstance().init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorMainTheme));
        }
        initViews();
    }

    private void initViews() {
        contentRecyclerView = findViewById(R.id.show_clipboard);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        actionMenu = findViewById(R.id.action_menu);
        toggleFloatWindow = findViewById(R.id.fab_float_window);
        final FloatingActionButton addRecord = findViewById(R.id.fab_add_record);
        toggleFloatWindow.setOnClickListener(this);
        addRecord.setOnClickListener(this);

    }

    private void hideFloatButton() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(this, FloatViewService.class));
        stopService(intent);
    }

    private void showFloatButton() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(this, FloatViewService.class));
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ClipContentAdapter();
        contentRecyclerView.setAdapter(adapter);
        adapter.setOnClipBoardItemClickListener(this);
        ClipboardUtil.getAllRecords(new ClipboardUtil.GetDataDoneCallback() {
            @Override
            public void onGetDone(List<ClipboardDBModel> list) {
                final List<ClipboardBean> clipboardBeans = ClipboardUtil.copyToClipBoardBean(list);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setDateList(clipboardBeans);
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_float_window:
                if (showedFloatMenu) {
                    showedFloatMenu = false;
                    hideFloatButton();
                    toggleFloatWindow.setLabelText("显示浮窗");
                } else {
                    boolean hasPerMission = checkFloatPermission(this);
                    if (!hasPerMission) {
                        Toast.makeText(this, "请打开显示悬浮窗开关!", Toast.LENGTH_LONG).show();
                        requestSettingCanDrawOverlays();
                    } else {
                        showFloat();
                    }
                }
                break;
            case R.id.fab_add_record:
                ClipboardUtil.showEditDialog(this, "添加文本记录", null, null, new IDialog.EditTextOkBtnClickListener() {
                    @Override
                    public void onClick(IDialog dialog, String[] texts) {
                        dialog.dismiss();
                        saveDateToDB(texts);
                    }
                }, new IDialog.OnClickListener() {
                    @Override
                    public void onClick(IDialog dialog) {
                        dialog.dismiss();
                    }
                });
                break;
        }
        actionMenu.close(true);
    }

    private void requestSettingCanDrawOverlays() {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.O) {//8.0以上
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
        } else if (sdkInt >= Build.VERSION_CODES.M) {//6.0-8.0
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
        } else {//4.4-6.0一下
            //无需处理了
        }
    }

    public static boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//                if (appOpsMgr == null)
//                    return false;
//                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
//                        .getPackageName());
//                return mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
//            } else {
            return Settings.canDrawOverlays(context);
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DIALOG_PERMISSION && resultCode == 0) {
            boolean hasPermission = checkFloatPermission(this);
            if (hasPermission) {
                showFloat();
            } else {
                Toast.makeText(this, "未打开悬浮窗开关!", Toast.LENGTH_LONG).show();
                showedFloatMenu = false;
            }
        }
    }

    private void showFloat() {
        showedFloatMenu = true;
        showFloatButton();
        toggleFloatWindow.setLabelText("关闭浮窗");
    }

    private void saveDateToDB(String[] texts) {
        String sum = texts[0];
        String con = texts[1];
        long currentTimeMillis = System.currentTimeMillis();
        String date = DateTimeUtil.formatDate(new Date(currentTimeMillis), new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()));
        ClipboardDBModel bean = new ClipboardDBModel();
        bean.setClipContent(con);
        bean.setDateLong(currentTimeMillis);
        bean.setDescription(sum);
        bean.setDateTxt(date);
        bean.save();
        ClipboardBean clipboardBean = new ClipboardBean(bean);
        adapter.addDateToLast(clipboardBean);

    }

    private void updateDbDate(ClipboardBean clipboardBean) {
        ClipboardDBModel clipboardDBModel = new Select().from(ClipboardDBModel.class).where(ClipboardDBModel_Table.dateLong.eq(clipboardBean.getDateLong())).querySingle();
        clipboardDBModel.setClipContent(clipboardBean.getClipContent());
        clipboardDBModel.setDescription(clipboardBean.getDescription());
        clipboardDBModel.save();
    }

    @Override
    public void onClipItemClick(ClipboardBean clipboardBean) {
        ClipboardUtil.copyTextToClipboard(this, clipboardBean.getClipContent());
        Toast.makeText(this, R.string.copy_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditButtonClick(final ClipboardBean clipboardBean) {
        ClipboardUtil.showEditDialog(this, "修改文本记录", clipboardBean.getDescription(), clipboardBean.getClipContent(),
                new IDialog.EditTextOkBtnClickListener() {
                    @Override
                    public void onClick(IDialog dialog, String[] texts) {
                        dialog.dismiss();
                        clipboardBean.setDescription(texts[0]);
                        clipboardBean.setClipContent(texts[1]);
                        updateDbDate(clipboardBean);
                    }
                }, new IDialog.OnClickListener() {
                    @Override
                    public void onClick(IDialog dialog) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onDeleteButtonClick(ClipboardBean clipboardBean) {
        new Delete().from(ClipboardDBModel.class).where(ClipboardDBModel_Table.dateLong.eq(clipboardBean.getDateLong())).execute();
    }
}
